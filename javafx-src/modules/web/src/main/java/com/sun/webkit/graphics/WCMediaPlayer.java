/*
 * Copyright (c) 2011, 2013, Oracle and/or its affiliates. All rights reserved.
 */
package com.sun.webkit.graphics;

import java.util.logging.Level;
import java.util.logging.Logger;
import com.sun.webkit.Invoker;

public abstract class WCMediaPlayer extends Ref {

    protected final static Logger log;
    protected final static boolean verbose;

    static {
        log = Logger.getLogger("webkit.mediaplayer");
        if (log.getLevel() == null) {
            // disable logging if webkit.mediaplayer.level
            // is not specified (explicitly) in log config file
            verbose = false;
            log.setLevel(Level.OFF);
        } else {
            verbose = true;
            log.log(Level.CONFIG, "webkit.mediaplayer logging is ON, level: {0}", log.getLevel());
        }
    }

    // pointer to native Player object;
    // read the value only on FX event thread, check that it has non-zero value;
    // setters (ctor && fwkDispose) are called on event thread.
    private long nPtr;

    protected WCMediaPlayer() {
    }

    void setNativePointer(long nativePointer) {
        if (nativePointer == 0) {
            throw new IllegalArgumentException("nativePointer is 0");
        }
        if (nPtr != 0) {
            throw new IllegalStateException("nPtr is not 0");
        }
        this.nPtr = nativePointer;
    }

    /**
     * Methods to implement
     */
    protected abstract void load(String url, String userAgent);
    protected abstract void cancelLoad();
    protected abstract void disposePlayer();

    // called if preload != PRELOAD_AUTO
    protected abstract void prepareToPlay();
    protected abstract void play();
    protected abstract void pause();

    protected abstract float getCurrentTime();
    // the method _must_ call notifySeeking(true)/notifySeeking(false)
    protected abstract void seek(float time);
    protected abstract void setRate(float rate);
    protected abstract void setVolume(float volume);
    protected abstract void setMute(boolean mute);
    protected abstract void setSize(int w, int h);
    protected abstract void setPreservesPitch(boolean preserve);

    protected abstract void renderCurrentFrame(WCGraphicsContext gc, int x, int y, int w, int h);

    /**
     * Obtains current "preserves pitch" value.
     */
    protected boolean getPreservesPitch() {
        return preserve;
    }

    protected int getNetworkState() {
        return networkState;
    }

    protected int getReadyState() {
        return readyState;
    }

    protected int getPreload() {
        return preload;
    }

    protected boolean isPaused() {
        return paused;
    }

    protected boolean isSeeking() {
        return seeking;
    }


    /* ======================================= */
    /*  Methods to notify webkit about events  */
    /* ======================================= */
    //enum NetworkState { Empty, Idle, Loading, Loaded, FormatError, NetworkError, DecodeError };
    protected final static int NETWORK_STATE_EMPTY              = 0;
    protected final static int NETWORK_STATE_IDLE               = 1;
    protected final static int NETWORK_STATE_LOADING            = 2;
    protected final static int NETWORK_STATE_LOADED             = 3;
    protected final static int NETWORK_STATE_FORMAT_ERROR       = 4;
    protected final static int NETWORK_STATE_NETWORK_ERROR      = 5;
    protected final static int NETWORK_STATE_DECODE_ERROR       = 6;
    //enum ReadyState  { HaveNothing, HaveMetadata, HaveCurrentData, HaveFutureData, HaveEnoughData };
    protected final static int READY_STATE_HAVE_NOTHING         = 0;
    protected final static int READY_STATE_HAVE_METADATA        = 1;
    protected final static int READY_STATE_HAVE_CURRENT_DATA    = 2;
    protected final static int READY_STATE_HAVE_FUTURE_DATA     = 3;
    protected final static int READY_STATE_HAVE_ENOUGH_DATA     = 4;
    //enum Preload { None, MetaData, Auto };
    protected final static int PRELOAD_NONE                     = 0;
    protected final static int PRELOAD_METADATA                 = 1;
    protected final static int PRELOAD_AUTO                     = 2;

    private int networkState = NETWORK_STATE_EMPTY;
    private int readyState = READY_STATE_HAVE_NOTHING;
    private int preload = PRELOAD_AUTO;
    private boolean paused = true;
    private boolean seeking = false;


    protected void notifyNetworkStateChanged(int networkState) {
        if (this.networkState != networkState) {
            this.networkState = networkState;
            final int _networkState = networkState;
            Invoker.getInvoker().invokeOnEventThread(new Runnable() {
                @Override
                public void run() {
                    if (nPtr != 0) {
                        notifyNetworkStateChanged(nPtr, _networkState);
                    }
                }
            });
        }
    }

    protected void notifyReadyStateChanged(int readyState) {
        if (this.readyState != readyState) {
            this.readyState = readyState;
            final int _readyState = readyState;
            Invoker.getInvoker().invokeOnEventThread(new Runnable() {
                @Override
                public void run() {
                    if (nPtr != 0) {
                        notifyReadyStateChanged(nPtr, _readyState);
                    }
                }
            });
        }
    }

    protected void notifyPaused(boolean paused) {
        if (verbose) log.log(Level.FINE, "notifyPaused, {0} => {1}",
                new Object[]{Boolean.valueOf(this.paused), Boolean.valueOf(paused)});
        if (this.paused != paused) {
            this.paused = paused;
            final boolean _paused = paused;
            Invoker.getInvoker().invokeOnEventThread(new Runnable() {
                @Override
                public void run() {
                    if (nPtr != 0) {
                        notifyPaused(nPtr, _paused);
                    }
                }
            });
        }
    }

    // pass -1 as readyState value if the state is not changed
    protected void notifySeeking(boolean seeking, int readyState) {
        if (verbose) log.log(Level.FINE, "notifySeeking, {0} => {1}",
                new Object[]{Boolean.valueOf(this.seeking), Boolean.valueOf(seeking)});
        if (this.seeking != seeking || this.readyState != readyState) {
            this.seeking = seeking;
            this.readyState = readyState;
            final boolean _seeking = seeking;
            final int _readyState = readyState;
            Invoker.getInvoker().invokeOnEventThread(new Runnable() {
                @Override
                public void run() {
                    if (nPtr != 0) {
                        notifySeeking(nPtr, _seeking, _readyState);
                    }
                }
            });
        }
    }

    protected void notifyFinished() {
        Invoker.getInvoker().invokeOnEventThread(new Runnable() {
            @Override
            public void run() {
                if (nPtr != 0) {
                    notifyFinished(nPtr);
                }
            }
        });
    }

    /** got metadata */
    protected void notifyReady(boolean hasVideo, boolean hasAudio, float duration) {
        final boolean _hasVideo = hasVideo;
        final boolean _hasAudio = hasAudio;
        final float _duration = duration;
        Invoker.getInvoker().invokeOnEventThread(new Runnable() {
            @Override
            public void run() {
                if (nPtr != 0) {
                    notifyReady(nPtr, _hasVideo, _hasAudio, _duration);
                }
            }
        });
    }

    protected void notifyDurationChanged(float newDuration) {
        final float _newDuration = newDuration;
        Invoker.getInvoker().invokeOnEventThread(new Runnable() {
            @Override
            public void run() {
                if (nPtr != 0) {
                    notifyDurationChanged(nPtr, _newDuration);
                }
            }
        });
    }

    protected void notifySizeChanged(int width, int height) {
        // notify on event thread to ensure native object is valid (nPtr != 0)
        final int _width = width;
        final int _height = height;
        Invoker.getInvoker().invokeOnEventThread(new Runnable() {
            @Override
            public void run() {
                if (nPtr != 0) {
                    notifySizeChanged(nPtr, _width, _height);
                }
            }
        });
    }

    private Runnable newFrameNotifier = new Runnable() {
        @Override
        public void run() {
            if (nPtr != 0) {
                notifyNewFrame(nPtr);
            }
        }
    };

    protected void notifyNewFrame() {
        Invoker.getInvoker().invokeOnEventThread(newFrameNotifier);
    }

    /** {@code ranges} array contains pairs [start,end] of the buffered times */
    protected void notifyBufferChanged(float[] ranges, int bytesLoaded) {
        // notify on event thread to ensure native object is valid (nPtr != 0)
        final float[] _ranges = ranges;
        final int _bytesLoaded = bytesLoaded;
        Invoker.getInvoker().invokeOnEventThread(new Runnable() {
            @Override
            public void run() {
                if (nPtr != 0) {
                    notifyBufferChanged(nPtr, _ranges, _bytesLoaded);
                }
            }
        });
    }


    /* ======================================= */
    /*  Methods called from webkit             */
    /* ======================================= */

    private void fwkLoad(String url, String userAgent) {
        if (verbose) log.log(Level.FINE, "fwkLoad, url={0}, userAgent={1}", new Object[] {url, userAgent});
        load(url, userAgent);
    }

    private void fwkCancelLoad() {
        if (verbose) log.log(Level.FINE, "fwkCancelLoad");
        cancelLoad();
    }

    private void fwkPrepareToPlay() {
        if (verbose) log.log(Level.FINE, "fwkPrepareToPlay");
        prepareToPlay();
    }

    private void fwkDispose() {
        if (verbose) log.log(Level.FINE, "fwkDispose");
        nPtr = 0;
        cancelLoad();
        disposePlayer();
    }

    private void fwkPlay() {
        if (verbose) log.log(Level.FINE, "fwkPlay");
        play();
    }

    private void fwkPause() {
        if (verbose) log.log(Level.FINE, "fwkPause");
        pause();
    }

    private float fwkGetCurrentTime() {
        float res = getCurrentTime();
        if (verbose) log.log(Level.FINER, "fwkGetCurrentTime(), return {0}", res);
        return res;
    }

    private void fwkSeek(float time) {
        if (verbose) log.log(Level.FINE, "fwkSeek({0})", time);
        seek(time);
    }

    private void fwkSetRate(float rate) {
        if (verbose) log.log(Level.FINE, "fwkSetRate({0})", rate);
        setRate(rate);
    }

    private void fwkSetVolume(float volume) {
        if (verbose) log.log(Level.FINE, "fwkSetVolume({0})", volume);
        setVolume(volume);
    }

    private void fwkSetMute(boolean mute) {
        if (verbose) log.log(Level.FINE, "fwkSetMute({0})", mute);
        setMute(mute);
    }

    private void fwkSetSize(int w, int h) {
        //if (verbose) log.log(Level.FINE, "setSize({0} x {1})", new Object[]{w, h});
        setSize(w, h);
    }

    private boolean preserve = true;

    private void fwkSetPreservesPitch(boolean preserve) {
        if (verbose) log.log(Level.FINE, "setPreservesPitch({0})", preserve);
//        synchronized(renderLock) {
            this.preserve = preserve;
            setPreservesPitch(preserve);
//        }
    }

    private void fwkSetPreload(int preload) {
        if (verbose) {
            log.log(Level.FINE, "fwkSetPreload({0})",
                    preload == PRELOAD_NONE ? "PRELOAD_NONE"
                    : preload == PRELOAD_METADATA ? "PRELOAD_METADATA"
                    : preload == PRELOAD_AUTO ? "PRELOAD_AUTO"
                    : ("INVALID VALUE: " + preload));
        }
        this.preload = preload;
    }

    /* called from GraphicsDecoder */
    void render(WCGraphicsContext gc, int x, int y, int w, int h) {
        if (verbose) {
            log.log(Level.FINER, "render(x={0}, y={1}, w={2}, h={3}",
                    new Object[]{x, y, w, h});
        }
        renderCurrentFrame(gc, x, y, w, h);
    }


    /* native methods */
    private native void notifyNetworkStateChanged(long nPtr, int networkState);
    private native void notifyReadyStateChanged(long nPtr, int readyState);
    private native void notifyPaused(long nPtr, boolean paused);
    private native void notifySeeking(long nPtr, boolean seeking, int readyState);
    private native void notifyFinished(long nPtr);
    private native void notifyReady(long nPtr, boolean hasVideo, boolean hasAudio, float duration);
    private native void notifyDurationChanged(long nPtr, float duration);
    private native void notifySizeChanged(long nPtr, int width, int height);
    private native void notifyNewFrame(long nPtr);
    private native void notifyBufferChanged(long nPtr, float[] ranges, int bytesLoaded);

}
