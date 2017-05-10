/*
 * Copyright (c) 2011, 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package com.sun.javafx.event;

import javafx.event.Event;
import javafx.event.EventDispatchChain;

public final class EventCountingDispatcher extends LabeledEventDispatcher {
    private int capturingEventCount;
    private int bubblingEventCount;
    private boolean consumeCapturingEvent;
    private boolean consumeBubblingEvent;

    public EventCountingDispatcher() {
    }

    public EventCountingDispatcher(final String label) {
        super(label);
    }

    public int getCapturingEventCount() {
        return capturingEventCount;
    }

    public int getBubblingEventCount() {
        return bubblingEventCount;
    }

    public void setConsumeCapturingEvent(final boolean consume) {
        consumeCapturingEvent = consume;
    }

    public void setConsumeBubblingEvent(final boolean consume) {
        consumeBubblingEvent = consume;
    }

    @Override
    public Event dispatchEvent(final Event event,
                               final EventDispatchChain tail) {
        ++capturingEventCount;
        if (consumeCapturingEvent) {
            return null;
        }

        final Event returnEvent = tail.dispatchEvent(event);
        if (returnEvent != null) {
            ++bubblingEventCount;
        }

        return consumeBubblingEvent ? null : returnEvent;
    }
}
