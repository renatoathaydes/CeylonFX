/*
 * Copyright (c) 2013, Oracle and/or its affiliates. All rights reserved.
 */
package javafx.scene.web;

import java.io.File;
import java.io.FilePermission;
import java.io.IOException;
import java.io.RandomAccessFile;
import static java.lang.String.format;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.security.Permission;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.event.EventType;
import static javafx.scene.web.WebErrorEvent.USER_DATA_DIRECTORY_ALREADY_IN_USE;
import static javafx.scene.web.WebErrorEvent.USER_DATA_DIRECTORY_IO_ERROR;
import static javafx.scene.web.WebErrorEvent.USER_DATA_DIRECTORY_SECURITY_ERROR;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class UserDataDirectoryTest extends TestBase {

    private static final File FOO = new File("foo");
    private static final File BAR = new File("bar");
    private static final File PRE_LOCKED = new File("baz");
    private static final File[] DIRS = new File[] {FOO, BAR, PRE_LOCKED};


    private static RandomAccessFile preLockedRaf;
    private static FileLock preLockedLock;
    private static final Random random = new Random();


    private final ArrayList<WebEngine> createdWebEngines = new ArrayList<>();
    private WebEngine webEngine;


    @BeforeClass
    public static void beforeClass() throws IOException {
        for (File dir : DIRS) {
            dir.mkdirs();
        }
        File preLockedFile = new File(PRE_LOCKED, ".lock");
        preLockedRaf = new RandomAccessFile(preLockedFile, "rw");
        preLockedLock = preLockedRaf.getChannel().tryLock();
        if (preLockedLock == null) {
            fail(format("Directory [%s] is already locked "
                    + "externally", PRE_LOCKED));
        }
    }

    @AfterClass
    public static void afterClass() throws IOException {
        preLockedLock.release();
        preLockedRaf.close();
        sleep(500); // Give WebKit some time to close SQLite files
        for (File dir : DIRS) {
            deleteRecursively(dir);
        }
    }


    @Before
    public void before() {
        webEngine = createWebEngine();
    }

    @After
    public void after() {
        for (WebEngine webEngine : createdWebEngines) {
            dispose(webEngine);
        }
    }


    @Test
    public void testDefaultValue() {
        assertSame(null, webEngine.getUserDataDirectory());
        // The default directory may be and often is locked by
        // a left-over engine from another test
        // assertLocked(defaultDirectory());
        load(webEngine, new File("src/test/resources/html/ipsum.html"));
        assertSame(null, webEngine.getUserDataDirectory());
        assertLocked(defaultDirectory());
        assertHasLocalStorage(webEngine);
    }

    @Test
    public void testSimpleModification() {
        assertSame(null, webEngine.getUserDataDirectory());
        assertNotLocked(FOO);
        webEngine.setUserDataDirectory(FOO);
        assertSame(FOO, webEngine.getUserDataDirectory());
        assertNotLocked(FOO);
        load(webEngine, new File("src/test/resources/html/ipsum.html"));
        assertSame(FOO, webEngine.getUserDataDirectory());
        assertLocked(FOO);
        assertHasLocalStorage(webEngine);
    }

    @Test
    public void testSetNullValue() {
        webEngine.setUserDataDirectory(FOO);
        assertSame(FOO, webEngine.getUserDataDirectory());
        assertNotLocked(FOO);
        webEngine.setUserDataDirectory(null);
        assertSame(null, webEngine.getUserDataDirectory());
        assertNotLocked(FOO);
        load(webEngine, new File("src/test/resources/html/ipsum.html"));
        assertSame(null, webEngine.getUserDataDirectory());
        assertLocked(defaultDirectory());
        assertNotLocked(FOO);
        assertHasLocalStorage(webEngine);
    }

    @Test
    public void testMultipleEnginesSharingOneDirectory() {
        ArrayList<WebEngine> webEngines = new ArrayList<>();
        assertNotLocked(FOO);
        for (int i = 0; i < 5; i++) {
            WebEngine webEngine = createWebEngine();
            webEngines.add(webEngine);
            webEngine.setUserDataDirectory(FOO);
            assertSame(FOO, webEngine.getUserDataDirectory());
            assertNotLocked(FOO);
        }
        for (WebEngine webEngine : webEngines) {
            load(webEngine, new File("src/test/resources/html/ipsum.html"));
            assertLocked(FOO);
        }
        assertHaveSharedLocalStorage(webEngines);
        for (WebEngine webEngine : webEngines) {
            assertLocked(FOO);
            dispose(webEngine);
        }
        assertNotLocked(FOO);
    }

    @Test
    public void testMultipleEnginesSharingTwoDirectories() {
        ArrayList<WebEngine> webEnginesA = new ArrayList<>();
        ArrayList<WebEngine> webEnginesB = new ArrayList<>();
        assertNotLocked(FOO);
        assertNotLocked(BAR);
        for (int i = 0; i < 5; i++) {
            WebEngine webEngineA = createWebEngine();
            webEnginesA.add(webEngineA);
            webEngineA.setUserDataDirectory(FOO);
            assertSame(FOO, webEngineA.getUserDataDirectory());
            assertNotLocked(FOO);

            WebEngine webEngineB = createWebEngine();
            webEnginesB.add(webEngineB);
            webEngineB.setUserDataDirectory(BAR);
            assertSame(BAR, webEngineB.getUserDataDirectory());
            assertNotLocked(BAR);
        }
        for (WebEngine webEngineA : webEnginesA) {
            load(webEngineA, new File("src/test/resources/html/ipsum.html"));
            assertLocked(FOO);
        }
        assertHaveSharedLocalStorage(webEnginesA);
        for (WebEngine webEngineB : webEnginesB) {
            load(webEngineB, new File("src/test/resources/html/ipsum.html"));
            assertLocked(BAR);
        }
        assertHaveSharedLocalStorage(webEnginesB);
        for (WebEngine webEngineA : webEnginesA) {
            assertLocked(FOO);
            dispose(webEngineA);
        }
        assertNotLocked(FOO);
        for (WebEngine webEngineB : webEnginesB) {
            assertLocked(BAR);
            dispose(webEngineB);
        }
        assertNotLocked(BAR);
    }

    @Test
    public void testLoadEffect() {
        webEngine.setUserDataDirectory(FOO);
        assertSame(FOO, webEngine.getUserDataDirectory());
        assertNotLocked(FOO);
        assertNotLocked(BAR);
        load(webEngine, new File("src/test/resources/html/h1.html"));
        assertSame(FOO, webEngine.getUserDataDirectory());
        assertLocked(FOO);
        assertNotLocked(BAR);
        assertHasLocalStorage(webEngine);

        webEngine.setUserDataDirectory(BAR);
        assertSame(BAR, webEngine.getUserDataDirectory());
        assertLocked(FOO);
        assertNotLocked(BAR);
        load(webEngine, new File("src/test/resources/html/ipsum.html"));
        assertSame(BAR, webEngine.getUserDataDirectory());
        assertLocked(FOO);
        assertNotLocked(BAR);
        assertHasLocalStorage(webEngine);
    }

    @Test
    public void testLoadContent1Effect() {
        webEngine.setUserDataDirectory(FOO);
        assertSame(FOO, webEngine.getUserDataDirectory());
        assertNotLocked(FOO);
        assertNotLocked(BAR);
        loadContent(webEngine, "<html/>");
        assertSame(FOO, webEngine.getUserDataDirectory());
        assertLocked(FOO);
        assertNotLocked(BAR);

        webEngine.setUserDataDirectory(BAR);
        assertSame(BAR, webEngine.getUserDataDirectory());
        assertLocked(FOO);
        assertNotLocked(BAR);
        load(webEngine, new File("src/test/resources/html/ipsum.html"));
        assertSame(BAR, webEngine.getUserDataDirectory());
        assertLocked(FOO);
        assertNotLocked(BAR);
        assertHasLocalStorage(webEngine);
    }

    @Test
    public void testLoadContent2Effect() {
        webEngine.setUserDataDirectory(FOO);
        assertSame(FOO, webEngine.getUserDataDirectory());
        assertNotLocked(FOO);
        assertNotLocked(BAR);
        loadContent(webEngine, "<html/>", "text/plain");
        assertSame(FOO, webEngine.getUserDataDirectory());
        assertLocked(FOO);
        assertNotLocked(BAR);

        webEngine.setUserDataDirectory(BAR);
        assertSame(BAR, webEngine.getUserDataDirectory());
        assertLocked(FOO);
        assertNotLocked(BAR);
        load(webEngine, new File("src/test/resources/html/ipsum.html"));
        assertSame(BAR, webEngine.getUserDataDirectory());
        assertLocked(FOO);
        assertNotLocked(BAR);
        assertHasLocalStorage(webEngine);
    }

    @Test
    public void testExecuteScriptEffect() {
        webEngine.setUserDataDirectory(FOO);
        assertSame(FOO, webEngine.getUserDataDirectory());
        assertNotLocked(FOO);
        assertNotLocked(BAR);
        submit(new Runnable() {@Override public void run() {
            webEngine.executeScript("alert()");
        }});
        assertSame(FOO, webEngine.getUserDataDirectory());
        assertLocked(FOO);
        assertNotLocked(BAR);

        webEngine.setUserDataDirectory(BAR);
        assertSame(BAR, webEngine.getUserDataDirectory());
        assertLocked(FOO);
        assertNotLocked(BAR);
        load(webEngine, new File("src/test/resources/html/ipsum.html"));
        assertSame(BAR, webEngine.getUserDataDirectory());
        assertLocked(FOO);
        assertNotLocked(BAR);
        assertHasLocalStorage(webEngine);
    }

    @Test
    public void testAlreadyInUseError() {
        webEngine.setUserDataDirectory(PRE_LOCKED);
        load(webEngine, new File("src/test/resources/html/ipsum.html"));
        assertSame(PRE_LOCKED, webEngine.getUserDataDirectory());
        assertLocked(PRE_LOCKED);
        assertHasNoLocalStorage(webEngine);
    }

    @Test
    public void testAlreadyInUseErrorWithPassiveHandler() {
        webEngine.setUserDataDirectory(PRE_LOCKED);
        ErrorHandler handler = new ErrorHandler();
        webEngine.setOnError(handler);
        load(webEngine, new File("src/test/resources/html/ipsum.html"));
        assertSame(PRE_LOCKED, webEngine.getUserDataDirectory());
        assertLocked(PRE_LOCKED);
        assertHasNoLocalStorage(webEngine);
        assertOccurred(USER_DATA_DIRECTORY_ALREADY_IN_USE, handler);
    }

    @Test
    public void testAlreadyInUseErrorWithRecoveringHandler() {
        webEngine.setUserDataDirectory(PRE_LOCKED);
        EventHandler<WebErrorEvent> h = new EventHandler<WebErrorEvent>() {
            @Override public void handle(WebErrorEvent event) {
                webEngine.setUserDataDirectory(BAR);
            }
        };
        webEngine.setOnError(h);
        assertSame(PRE_LOCKED, webEngine.getUserDataDirectory());
        load(webEngine, new File("src/test/resources/html/ipsum.html"));
        assertSame(BAR, webEngine.getUserDataDirectory());
        assertLocked(BAR);
        assertHasLocalStorage(webEngine);
    }

    @Test
    public void testIOError() throws IOException {
        File f = new File("qux");
        f.createNewFile();
        try {
            webEngine.setUserDataDirectory(f);
            load(webEngine, new File("src/test/resources/html/ipsum.html"));
            assertSame(f, webEngine.getUserDataDirectory());
            assertHasNoLocalStorage(webEngine);
        } finally {
            f.delete();
        }
    }

    @Test
    public void testIOErrorWithPassiveHandler() throws IOException {
        File f = new File("qux");
        f.createNewFile();
        try {
            webEngine.setUserDataDirectory(f);
            ErrorHandler handler = new ErrorHandler();
            webEngine.setOnError(handler);
            load(webEngine, new File("src/test/resources/html/ipsum.html"));
            assertSame(f, webEngine.getUserDataDirectory());
            assertHasNoLocalStorage(webEngine);
            assertOccurred(USER_DATA_DIRECTORY_IO_ERROR, handler);
        } finally {
            f.delete();
        }
    }

    @Test
    public void testIOErrorWithRecoveringHandler() throws IOException {
        File f = new File("qux");
        f.createNewFile();
        try {
            webEngine.setUserDataDirectory(f);
            EventHandler<WebErrorEvent> h = new EventHandler<WebErrorEvent>() {
                @Override public void handle(WebErrorEvent event) {
                    webEngine.setUserDataDirectory(BAR);
                }
            };
            webEngine.setOnError(h);
            assertSame(f, webEngine.getUserDataDirectory());
            load(webEngine, new File("src/test/resources/html/ipsum.html"));
            assertSame(BAR, webEngine.getUserDataDirectory());
            assertLocked(BAR);
            assertHasLocalStorage(webEngine);
        } finally {
            f.delete();
        }
    }

    @Test
    public void testSecurityError() {
        String url = new File("src/test/resources/html/ipsum.html")
                .toURI().toASCIIString();
        SecurityManager oldSecurityManager = System.getSecurityManager();
        System.setSecurityManager(new CustomSecurityManager(FOO));
        try {
            webEngine.setUserDataDirectory(FOO);
            load(webEngine, url);
        } finally {
            System.setSecurityManager(oldSecurityManager);
        }
        assertSame(FOO, webEngine.getUserDataDirectory());
        assertNotLocked(FOO);
        assertHasNoLocalStorage(webEngine);
    }

    @Test
    public void testSecurityErrorWithPassiveHandler() {
        String url = new File("src/test/resources/html/ipsum.html")
                .toURI().toASCIIString();
        SecurityManager oldSecurityManager = System.getSecurityManager();
        System.setSecurityManager(new CustomSecurityManager(FOO));
        ErrorHandler handler = new ErrorHandler();
        try {
            webEngine.setUserDataDirectory(FOO);
            webEngine.setOnError(handler);
            load(webEngine, url);
        } finally {
            System.setSecurityManager(oldSecurityManager);
        }
        assertSame(FOO, webEngine.getUserDataDirectory());
        assertNotLocked(FOO);
        assertHasNoLocalStorage(webEngine);
        assertOccurred(USER_DATA_DIRECTORY_SECURITY_ERROR, handler);
    }

    @Test
    public void testSecurityErrorWithRecoveringHandler() {
        String url = new File("src/test/resources/html/ipsum.html")
                .toURI().toASCIIString();
        SecurityManager oldSecurityManager = System.getSecurityManager();
        System.setSecurityManager(new CustomSecurityManager(FOO));
        try {
            webEngine.setUserDataDirectory(FOO);
            EventHandler<WebErrorEvent> h = new EventHandler<WebErrorEvent>() {
                @Override public void handle(WebErrorEvent event) {
                    webEngine.setUserDataDirectory(BAR);
                }
            };
            webEngine.setOnError(h);
            assertSame(FOO, webEngine.getUserDataDirectory());
            load(webEngine, url);
        } finally {
            System.setSecurityManager(oldSecurityManager);
        }
        assertSame(BAR, webEngine.getUserDataDirectory());
        assertLocked(BAR);
        assertHasLocalStorage(webEngine);
    }

    @Test
    public void testDisposal() {
        webEngine.setUserDataDirectory(FOO);
        load(webEngine, new File("src/test/resources/html/ipsum.html"));
        assertLocked(FOO);
        dispose(webEngine);
        assertNotLocked(FOO);
    }

    @Test
    public void testPropertyObjectSanity() {
        ObjectProperty<File> property = webEngine.userDataDirectoryProperty();
        assertNotNull(property);
        assertSame(property, webEngine.userDataDirectoryProperty());
        assertSame(null, property.get());
        webEngine.setUserDataDirectory(FOO);
        assertSame(FOO, property.get());
    }

    @Test
    public void testPropertyBinding() {
        ObjectProperty<File> otherProperty = new SimpleObjectProperty<>();
        otherProperty.bind(webEngine.userDataDirectoryProperty());
        assertSame(webEngine.getUserDataDirectory(), otherProperty.get());
        webEngine.setUserDataDirectory(FOO);
        assertSame(webEngine.getUserDataDirectory(), otherProperty.get());
    }

    @Test
    public void testNoFxThreadCheck() throws IOException {
        webEngine.getUserDataDirectory();
        webEngine.setUserDataDirectory(FOO);
    }


    private WebEngine createWebEngine() {
        WebEngine result;
        if (Platform.isFxApplicationThread()) {
            result = new WebEngine();
        } else {
            result = submit(new Callable<WebEngine>() {
                @Override public WebEngine call() {
                    return new WebEngine();
                }
            });
        }
        createdWebEngines.add(result);
        return result;
    }

    private void load(WebEngine webEngine, File file) {
        load(webEngine, file.toURI().toASCIIString());
    }

    private void load(final WebEngine webEngine, final String url) {
        executeLoadJob(webEngine, new Runnable() {@Override public void run() {
            webEngine.load(url);
        }});
    }

    private void loadContent(final WebEngine webEngine, final String content) {
        executeLoadJob(webEngine, new Runnable() {@Override public void run() {
            webEngine.loadContent(content);
        }});
    }

    private void loadContent(final WebEngine webEngine, final String content,
                             final String contentType)
    {
        executeLoadJob(webEngine, new Runnable() {@Override public void run() {
            webEngine.loadContent(content, contentType);
        }});
    }

    private void executeLoadJob(final WebEngine webEngine, final Runnable job) {
        final CountDownLatch latch = new CountDownLatch(1);
        submit(new Runnable() {@Override public void run() {
            webEngine.getLoadWorker().runningProperty().addListener(
                    new ChangeListener<Boolean>() {
                        @Override public void changed(
                                ObservableValue<? extends Boolean> ov,
                                Boolean oldValue, Boolean newValue)
                        {
                            if (!newValue) {
                                latch.countDown();
                            }
                        }
                    });
            job.run();
        }});
        try {
            latch.await();
        } catch (InterruptedException ex) {
            throw new AssertionError(ex);
        }
    }

    private void dispose(final WebEngine webEngine) {
        Runnable runnable = new Runnable() {@Override public void run() {
            webEngine.dispose();
        }};
        if (Platform.isFxApplicationThread()) {
            runnable.run();
        } else {
            submit(runnable);
        }
    }

    private static void deleteRecursively(File file) throws IOException {
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                deleteRecursively(f);
            }
        }
        if (!file.delete()) {
            throw new IOException(String.format("Error deleting [%s]", file));
        }
    }

    private void assertLocked(File directory) {
        File file = new File(directory, ".lock");
        RandomAccessFile raf = null;
        FileLock fileLock = null;
        try {
            raf = new RandomAccessFile(file, "rw");
            fileLock = raf.getChannel().tryLock();
            if (fileLock == null) {
                fail(format("Directory [%s] is locked externally", directory));
            } else {
                fail(format("Directory [%s] is not locked", directory));
            }
        } catch (OverlappingFileLockException expected) {
        } catch (IOException ex) {
            throw new AssertionError(ex);
        } finally {
            if (fileLock != null) {
                try {
                    fileLock.release();
                } catch (IOException ignore) {}
            }
            if (raf != null) {
                try {
                    raf.close();
                } catch (IOException ignore) {}
            }
        }
    }

    private void assertNotLocked(File directory) {
        File file = new File(directory, ".lock");
        RandomAccessFile raf = null;
        FileLock fileLock = null;
        try {
            raf = new RandomAccessFile(file, "rw");
            fileLock = raf.getChannel().tryLock();
            if (fileLock == null) {
                fail(format("Directory [%s] is locked externally", directory));
            }
        } catch (OverlappingFileLockException ex) {
            fail(format("Directory [%s] is locked", directory));
        } catch (IOException ex) {
            throw new AssertionError(ex);
        } finally {
            if (fileLock != null) {
                try {
                    fileLock.release();
                } catch (IOException ignore) {}
            }
            if (raf != null) {
                try {
                    raf.close();
                } catch (IOException ignore) {}
            }
        }
    }

    private void assertHasLocalStorage(WebEngine webEngine) {
        assertHaveSharedLocalStorage(Arrays.asList(webEngine));
    }

    private void assertHaveSharedLocalStorage(
            final Collection<WebEngine> webEngines)
    {
        Runnable runnable = new Runnable() {@Override public void run() {
            for (WebEngine webEngine : webEngines) {
                assertNotNull(webEngine.executeScript("localStorage"));
            }
            for (WebEngine webEngine : webEngines) {
                String key = "key" + random.nextInt();
                String value1 = "value" + random.nextInt();
                webEngine.executeScript("localStorage.setItem('"
                        + key + "', '" + value1 + "')");
                for (WebEngine otherWebEngine : webEngines) {
                    String value2 = (String) otherWebEngine.executeScript(
                            "localStorage.getItem('" + key + "')");
                    assertEquals(value1, value2);
                }
            }
        }};
        if (Platform.isFxApplicationThread()) {
            runnable.run();
        } else {
            submit(runnable);
        }
    }

    private void assertHasNoLocalStorage(final WebEngine webEngine) {
        Runnable runnable = new Runnable() {@Override public void run() {
            assertNull(webEngine.executeScript("localStorage"));
        }};
        if (Platform.isFxApplicationThread()) {
            runnable.run();
        } else {
            submit(runnable);
        }
    }

    private static void assertOccurred(EventType<WebErrorEvent> eventType,
                                       ErrorHandler handler)
    {
        assertEquals(1, handler.errors.size());
        assertSame(eventType, handler.errors.get(0).getEventType());
    }

    private File defaultDirectory() {
        Callable<String> callable = new Callable<String>() {
            @Override public String call() {
                return com.sun.glass.ui.Application.GetApplication()
                        .getDataDirectory();
            }
        };
        String appDataDir;
        if (Platform.isFxApplicationThread()) {
            try {
                appDataDir = callable.call();
            } catch (Exception ex) {
                throw new AssertionError(ex);
            }
        } else {
            appDataDir = submit(callable);
        }
        return new File(appDataDir, "webview");
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
            throw new AssertionError(ex);
        }
    }

    private static final class ErrorHandler
        implements EventHandler<WebErrorEvent>
    {
        private final ArrayList<WebErrorEvent> errors = new ArrayList<>();

        @Override public void handle(WebErrorEvent event) {
            errors.add(event);
            System.err.println("onError: " + event);
        }
    }

    private static final class CustomSecurityManager extends SecurityManager {
        private final String path;

        private CustomSecurityManager(File path) {
            try {
                this.path = path.getCanonicalPath();
            } catch (IOException ex) {
                throw new AssertionError(ex);
            }
        }

        @Override public void checkPermission(Permission perm) {
            if (perm instanceof FilePermission
                    && perm.getName().startsWith(path))
            {
                super.checkPermission(perm);
            }
        }
    }
}
