package ceylonfx.application;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import javafx.application.Application;
import javafx.stage.Stage;

public class JavaApp extends Application {

	private static ArrayBlockingQueue<Stage> stageHolder = new ArrayBlockingQueue<Stage>(1);

	@Override
	public void start(Stage stage) throws Exception {
		stageHolder.add(stage);
		stage.show();
	}

	public static Stage initialize(final String... args) throws InterruptedException {
		if (stageHolder == null)
			throw new RuntimeException("Already initialized!");

		new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("Launching application");
				launch(JavaApp.class, args);
			}
		}).start();
		Stage stage = stageHolder.poll(10, TimeUnit.SECONDS);
		stageHolder = null;
		return stage;
	}

}
