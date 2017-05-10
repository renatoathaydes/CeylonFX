/*
 * Copyright (c) 2012, 2013, Oracle and/or its affiliates. All rights reserved.
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

package com.sun.javafx.binding;

import javafx.beans.InvalidationListener;
import javafx.beans.InvalidationListenerMock;
import javafx.beans.Observable;
import javafx.beans.WeakInvalidationListenerMock;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ChangeListenerMock;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.ObservableValueStub;
import javafx.beans.value.WeakChangeListenerMock;
import org.junit.Before;
import org.junit.Test;

public class ExpressionHelperTest {

    private static final Object UNDEFINED = new Object();
    private static final Object DATA_1 = new Object();
    private static final Object DATA_2 = new Object();

    private ExpressionHelper helper;
    private ObservableValueStub observable;
    private InvalidationListenerMock[] invalidationListener;
    private ChangeListenerMock<Object>[] changeListener;

    @Before
    public void setUp() {
        helper = null;
        observable = new ObservableValueStub(DATA_1);
        invalidationListener = new InvalidationListenerMock[] {
                new InvalidationListenerMock(), new InvalidationListenerMock(), new InvalidationListenerMock(), new InvalidationListenerMock()
        };
        changeListener = new ChangeListenerMock[] {
                new ChangeListenerMock<Object>(UNDEFINED), new ChangeListenerMock<Object>(UNDEFINED), new ChangeListenerMock<Object>(UNDEFINED), new ChangeListenerMock<Object>(UNDEFINED)
        };
    }

    @Test (expected = NullPointerException.class)
    public void testAddInvalidation_Null_X() {
        ExpressionHelper.addListener(helper, null, invalidationListener[0]);
    }

    @Test (expected = NullPointerException.class)
    public void testAddInvalidation_X_Null() {
        ExpressionHelper.addListener(helper, observable, (InvalidationListener)null);
    }

    @Test (expected = NullPointerException.class)
    public void testRemoveInvalidation_Null() {
        ExpressionHelper.removeListener(helper, (InvalidationListener) null);
    }

    @Test (expected = NullPointerException.class)
    public void testAddChange_Null_X() {
        ExpressionHelper.addListener(helper, null, changeListener[0]);
    }

    @Test (expected = NullPointerException.class)
    public void testAddChange_X_Null() {
        ExpressionHelper.addListener(helper, observable, (ChangeListener) null);
    }

    @Test (expected = NullPointerException.class)
    public void testRemoveChange_Null() {
        ExpressionHelper.removeListener(helper, (ChangeListener) null);
    }

    @Test
    public void testEmptyHelper() {
        // all of these calls should be no-ops
        ExpressionHelper.removeListener(helper, invalidationListener[0]);
        ExpressionHelper.removeListener(helper, changeListener[0]);
        ExpressionHelper.fireValueChangedEvent(helper);
    }

    @Test
    public void testSingeInvalidation() {
        helper = ExpressionHelper.addListener(helper, observable, invalidationListener[0]);
        ExpressionHelper.fireValueChangedEvent(helper);
        invalidationListener[0].check(observable, 1);

        helper = ExpressionHelper.removeListener(helper, invalidationListener[1]);
        ExpressionHelper.fireValueChangedEvent(helper);
        invalidationListener[0].check(observable, 1);
        invalidationListener[1].check(null, 0);

        helper = ExpressionHelper.removeListener(helper, changeListener[1]);
        ExpressionHelper.fireValueChangedEvent(helper);
        invalidationListener[0].check(observable, 1);
        changeListener[1].check(null, UNDEFINED, UNDEFINED, 0);

        helper = ExpressionHelper.addListener(helper, observable, invalidationListener[1]);
        ExpressionHelper.fireValueChangedEvent(helper);
        invalidationListener[0].check(observable, 1);
        invalidationListener[1].check(observable, 1);

        helper = ExpressionHelper.removeListener(helper, invalidationListener[1]);
        ExpressionHelper.fireValueChangedEvent(helper);
        invalidationListener[0].check(observable, 1);
        invalidationListener[1].check(null, 0);

        helper = ExpressionHelper.addListener(helper, observable, changeListener[1]);
        observable.set(DATA_2);
        ExpressionHelper.fireValueChangedEvent(helper);
        invalidationListener[0].check(observable, 1);
        changeListener[1].check(observable, DATA_1, DATA_2, 1);

        helper = ExpressionHelper.removeListener(helper, changeListener[1]);
        observable.set(DATA_1);
        ExpressionHelper.fireValueChangedEvent(helper);
        invalidationListener[0].check(observable, 1);
        changeListener[1].check(null, UNDEFINED, UNDEFINED, 0);

        helper = ExpressionHelper.removeListener(helper, invalidationListener[0]);
        ExpressionHelper.fireValueChangedEvent(helper);
        invalidationListener[0].check(null, 0);
    }

    @Test
    public void testSingeChange() {
        helper = ExpressionHelper.addListener(helper, observable, changeListener[0]);
        observable.set(DATA_2);
        ExpressionHelper.fireValueChangedEvent(helper);
        changeListener[0].check(observable, DATA_1, DATA_2, 1);

        helper = ExpressionHelper.removeListener(helper, invalidationListener[1]);
        observable.set(DATA_1);
        ExpressionHelper.fireValueChangedEvent(helper);
        changeListener[0].check(observable, DATA_2, DATA_1, 1);
        invalidationListener[1].check(null, 0);

        helper = ExpressionHelper.removeListener(helper, changeListener[1]);
        observable.set(DATA_2);
        ExpressionHelper.fireValueChangedEvent(helper);
        changeListener[0].check(observable, DATA_1, DATA_2, 1);
        changeListener[1].check(null, UNDEFINED, UNDEFINED, 0);

        helper = ExpressionHelper.addListener(helper, observable, invalidationListener[1]);
        observable.set(DATA_1);
        ExpressionHelper.fireValueChangedEvent(helper);
        changeListener[0].check(observable, DATA_2, DATA_1, 1);
        invalidationListener[1].check(observable, 1);

        helper = ExpressionHelper.removeListener(helper, invalidationListener[1]);
        observable.set(DATA_2);
        ExpressionHelper.fireValueChangedEvent(helper);
        changeListener[0].check(observable, DATA_1, DATA_2, 1);
        invalidationListener[1].check(null, 0);

        helper = ExpressionHelper.addListener(helper, observable, changeListener[1]);
        observable.set(DATA_1);
        ExpressionHelper.fireValueChangedEvent(helper);
        changeListener[0].check(observable, DATA_2, DATA_1, 1);
        changeListener[1].check(observable, DATA_2, DATA_1, 1);

        helper = ExpressionHelper.removeListener(helper, changeListener[1]);
        observable.set(DATA_2);
        ExpressionHelper.fireValueChangedEvent(helper);
        changeListener[0].check(observable, DATA_1, DATA_2, 1);
        changeListener[1].check(null, UNDEFINED, UNDEFINED, 0);

        helper = ExpressionHelper.removeListener(helper, changeListener[0]);
        ExpressionHelper.fireValueChangedEvent(helper);
        changeListener[0].check(null, UNDEFINED, UNDEFINED, 0);
    }

    @Test
    public void testAddInvalidation() {
        final InvalidationListener weakListener = new WeakInvalidationListenerMock();

        helper = ExpressionHelper.addListener(helper, observable, changeListener[0]);
        helper = ExpressionHelper.addListener(helper, observable, changeListener[1]);

        helper = ExpressionHelper.addListener(helper, observable, weakListener);
        helper = ExpressionHelper.addListener(helper, observable, invalidationListener[0]);
        ExpressionHelper.fireValueChangedEvent(helper);
        invalidationListener[0].check(observable, 1);

        helper = ExpressionHelper.addListener(helper, observable, weakListener);
        helper = ExpressionHelper.addListener(helper, observable, invalidationListener[1]);
        ExpressionHelper.fireValueChangedEvent(helper);
        invalidationListener[0].check(observable, 1);
        invalidationListener[1].check(observable, 1);

        helper = ExpressionHelper.addListener(helper, observable, weakListener);
        helper = ExpressionHelper.addListener(helper, observable, invalidationListener[2]);
        ExpressionHelper.fireValueChangedEvent(helper);
        invalidationListener[0].check(observable, 1);
        invalidationListener[1].check(observable, 1);
        invalidationListener[2].check(observable, 1);
    }

    @Test
    public void testRemoveInvalidation() {
        helper = ExpressionHelper.addListener(helper, observable, changeListener[0]);
        helper = ExpressionHelper.addListener(helper, observable, changeListener[1]);

        helper = ExpressionHelper.removeListener(helper, invalidationListener[1]);

        helper = ExpressionHelper.addListener(helper, observable, invalidationListener[0]);

        helper = ExpressionHelper.removeListener(helper, invalidationListener[1]);

        helper = ExpressionHelper.addListener(helper, observable, invalidationListener[2]);
        helper = ExpressionHelper.addListener(helper, observable, invalidationListener[1]);

        helper = ExpressionHelper.removeListener(helper, invalidationListener[0]);
        ExpressionHelper.fireValueChangedEvent(helper);
        invalidationListener[0].check(null, 0);
        invalidationListener[1].check(observable, 1);
        invalidationListener[2].check(observable, 1);

        helper = ExpressionHelper.removeListener(helper, invalidationListener[1]);
        ExpressionHelper.fireValueChangedEvent(helper);
        invalidationListener[0].check(null, 0);
        invalidationListener[1].check(null, 0);
        invalidationListener[2].check(observable, 1);

        helper = ExpressionHelper.removeListener(helper, invalidationListener[2]);
        ExpressionHelper.fireValueChangedEvent(helper);
        invalidationListener[0].check(null, 0);
        invalidationListener[1].check(null, 0);
        invalidationListener[2].check(null, 0);
    }

    @Test
    public void testAddInvalidationWhileLocked() {
        final ChangeListener<Object> addingListener = new ChangeListener() {
            int index = 0;
            @Override public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (index < invalidationListener.length) {
                    helper = ExpressionHelper.addListener(helper, ExpressionHelperTest.this.observable, invalidationListener[index++]);
                }
            }
        };
        helper = ExpressionHelper.addListener(helper, observable, addingListener);
        helper = ExpressionHelper.addListener(helper, observable, changeListener[0]);

        observable.set(DATA_2);
        ExpressionHelper.fireValueChangedEvent(helper);

        invalidationListener[0].reset();
        observable.set(DATA_1);
        ExpressionHelper.fireValueChangedEvent(helper);
        invalidationListener[0].check(observable, 1);

        invalidationListener[1].reset();
        observable.set(DATA_2);
        ExpressionHelper.fireValueChangedEvent(helper);
        invalidationListener[0].check(observable, 1);
        invalidationListener[1].check(observable, 1);

        invalidationListener[2].reset();
        observable.set(DATA_1);
        ExpressionHelper.fireValueChangedEvent(helper);
        invalidationListener[0].check(observable, 1);
        invalidationListener[1].check(observable, 1);
        invalidationListener[2].check(observable, 1);

        invalidationListener[3].reset();
        observable.set(DATA_2);
        ExpressionHelper.fireValueChangedEvent(helper);
        invalidationListener[0].check(observable, 1);
        invalidationListener[1].check(observable, 1);
        invalidationListener[2].check(observable, 1);
        invalidationListener[3].check(observable, 1);
    }

    @Test
    public void testRemoveInvalidationWhileLocked() {
        final ChangeListener<Object> removingListener = new ChangeListener() {
            int index = 0;
            @Override public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (index < invalidationListener.length) {
                    helper = ExpressionHelper.removeListener(helper, invalidationListener[index++]);
                }
            }
        };
        helper = ExpressionHelper.addListener(helper, observable, removingListener);
        helper = ExpressionHelper.addListener(helper, observable, changeListener[0]);
        helper = ExpressionHelper.addListener(helper, observable, invalidationListener[0]);
        helper = ExpressionHelper.addListener(helper, observable, invalidationListener[2]);
        helper = ExpressionHelper.addListener(helper, observable, invalidationListener[1]);

        observable.set(DATA_2);
        ExpressionHelper.fireValueChangedEvent(helper);
        invalidationListener[0].reset();
        invalidationListener[1].check(observable, 1);
        invalidationListener[2].check(observable, 1);

        observable.set(DATA_1);
        ExpressionHelper.fireValueChangedEvent(helper);
        invalidationListener[0].check(null, 0);
        invalidationListener[1].reset();
        invalidationListener[2].check(observable, 1);

        observable.set(DATA_2);
        ExpressionHelper.fireValueChangedEvent(helper);
        invalidationListener[0].check(null, 0);
        invalidationListener[1].check(null, 0);
        invalidationListener[2].reset();

        observable.set(DATA_1);
        ExpressionHelper.fireValueChangedEvent(helper);
        invalidationListener[0].check(null, 0);
        invalidationListener[1].check(null, 0);
        invalidationListener[2].check(null, 0);
    }

    @Test
    public void testAddChange() {
        final ChangeListener<Object> weakListener = new WeakChangeListenerMock();

        helper = ExpressionHelper.addListener(helper, observable, invalidationListener[0]);
        helper = ExpressionHelper.addListener(helper, observable, invalidationListener[1]);

        helper = ExpressionHelper.addListener(helper, observable, weakListener);
        helper = ExpressionHelper.addListener(helper, observable, changeListener[0]);
        observable.set(DATA_2);
        ExpressionHelper.fireValueChangedEvent(helper);
        changeListener[0].check(observable, DATA_1, DATA_2, 1);

        helper = ExpressionHelper.addListener(helper, observable, weakListener);
        helper = ExpressionHelper.addListener(helper, observable, changeListener[1]);
        observable.set(DATA_1);
        ExpressionHelper.fireValueChangedEvent(helper);
        changeListener[0].check(observable, DATA_2, DATA_1, 1);
        changeListener[1].check(observable, DATA_2, DATA_1, 1);

        helper = ExpressionHelper.addListener(helper, observable, weakListener);
        helper = ExpressionHelper.addListener(helper, observable, changeListener[2]);
        observable.set(DATA_2);
        ExpressionHelper.fireValueChangedEvent(helper);
        changeListener[0].check(observable, DATA_1, DATA_2, 1);
        changeListener[1].check(observable, DATA_1, DATA_2, 1);
        changeListener[2].check(observable, DATA_1, DATA_2, 1);
    }

    @Test
    public void testRemoveChange() {
        helper = ExpressionHelper.addListener(helper, observable, invalidationListener[0]);
        helper = ExpressionHelper.addListener(helper, observable, invalidationListener[1]);

        helper = ExpressionHelper.removeListener(helper, changeListener[1]);

        helper = ExpressionHelper.addListener(helper, observable, changeListener[0]);

        helper = ExpressionHelper.removeListener(helper, changeListener[1]);

        helper = ExpressionHelper.addListener(helper, observable, changeListener[2]);
        helper = ExpressionHelper.addListener(helper, observable, changeListener[1]);

        helper = ExpressionHelper.removeListener(helper, changeListener[0]);
        observable.set(DATA_2);
        ExpressionHelper.fireValueChangedEvent(helper);
        changeListener[0].check(null, UNDEFINED, UNDEFINED, 0);
        changeListener[1].check(observable, DATA_1, DATA_2, 1);
        changeListener[2].check(observable, DATA_1, DATA_2, 1);

        helper = ExpressionHelper.removeListener(helper, changeListener[1]);
        observable.set(DATA_1);
        ExpressionHelper.fireValueChangedEvent(helper);
        changeListener[0].check(null, UNDEFINED, UNDEFINED, 0);
        changeListener[1].check(null, UNDEFINED, UNDEFINED, 0);
        changeListener[2].check(observable, DATA_2, DATA_1, 1);

        helper = ExpressionHelper.removeListener(helper, changeListener[2]);
        observable.set(DATA_2);
        ExpressionHelper.fireValueChangedEvent(helper);
        changeListener[0].check(null, UNDEFINED, UNDEFINED, 0);
        changeListener[1].check(null, UNDEFINED, UNDEFINED, 0);
        changeListener[2].check(null, UNDEFINED, UNDEFINED, 0);
    }

    @Test
    public void testAddChangeWhileLocked() {
        final InvalidationListener addingListener = new InvalidationListener() {
            int index = 0;
            @Override public void invalidated(Observable observable) {
                if (index < invalidationListener.length) {
                    helper = ExpressionHelper.addListener(helper, ExpressionHelperTest.this.observable, changeListener[index++]);
                }
            }
        };
        helper = ExpressionHelper.addListener(helper, observable, addingListener);
        helper = ExpressionHelper.addListener(helper, observable, invalidationListener[0]);

        observable.set(DATA_2);
        ExpressionHelper.fireValueChangedEvent(helper);

        changeListener[0].reset();
        observable.set(DATA_1);
        ExpressionHelper.fireValueChangedEvent(helper);
        changeListener[0].check(observable, DATA_2, DATA_1, 1);

        changeListener[1].reset();
        observable.set(DATA_2);
        ExpressionHelper.fireValueChangedEvent(helper);
        changeListener[0].check(observable, DATA_1, DATA_2, 1);
        changeListener[1].check(observable, DATA_1, DATA_2, 1);

        changeListener[2].reset();
        observable.set(DATA_1);
        ExpressionHelper.fireValueChangedEvent(helper);
        changeListener[0].check(observable, DATA_2, DATA_1, 1);
        changeListener[1].check(observable, DATA_2, DATA_1, 1);
        changeListener[2].check(observable, DATA_2, DATA_1, 1);

        changeListener[3].reset();
        observable.set(DATA_2);
        ExpressionHelper.fireValueChangedEvent(helper);
        changeListener[0].check(observable, DATA_1, DATA_2, 1);
        changeListener[1].check(observable, DATA_1, DATA_2, 1);
        changeListener[2].check(observable, DATA_1, DATA_2, 1);
        changeListener[3].check(observable, DATA_1, DATA_2, 1);
    }

    @Test
    public void testRemoveChangeWhileLocked() {
        final InvalidationListener removingListener = new InvalidationListener() {
            int index = 0;
            @Override public void invalidated(Observable observable) {
                if (index < invalidationListener.length) {
                    helper = ExpressionHelper.removeListener(helper, changeListener[index++]);
                }
            }
        };
        helper = ExpressionHelper.addListener(helper, observable, removingListener);
        helper = ExpressionHelper.addListener(helper, observable, invalidationListener[0]);
        helper = ExpressionHelper.addListener(helper, observable, changeListener[0]);
        helper = ExpressionHelper.addListener(helper, observable, changeListener[2]);
        helper = ExpressionHelper.addListener(helper, observable, changeListener[1]);

        observable.set(DATA_2);
        ExpressionHelper.fireValueChangedEvent(helper);
        changeListener[0].reset();
        changeListener[1].check(observable, DATA_1, DATA_2, 1);
        changeListener[2].check(observable, DATA_1, DATA_2, 1);

        observable.set(DATA_1);
        ExpressionHelper.fireValueChangedEvent(helper);
        changeListener[0].check(null, UNDEFINED, UNDEFINED, 0);
        changeListener[1].reset();
        changeListener[2].check(observable, DATA_2, DATA_1, 1);

        observable.set(DATA_2);
        ExpressionHelper.fireValueChangedEvent(helper);
        changeListener[0].check(null, UNDEFINED, UNDEFINED, 0);
        changeListener[1].check(null, UNDEFINED, UNDEFINED, 0);
        changeListener[2].reset();

        observable.set(DATA_1);
        ExpressionHelper.fireValueChangedEvent(helper);
        changeListener[0].check(null, UNDEFINED, UNDEFINED, 0);
        changeListener[1].check(null, UNDEFINED, UNDEFINED, 0);
        changeListener[2].check(null, UNDEFINED, UNDEFINED, 0);
    }

    @Test
    public void testFireValueChangedEvent() {
        helper = ExpressionHelper.addListener(helper, observable, invalidationListener[0]);
        helper = ExpressionHelper.addListener(helper, observable, changeListener[0]);

        ExpressionHelper.fireValueChangedEvent(helper);
        invalidationListener[0].check(observable, 1);
        changeListener[0].check(null, UNDEFINED, UNDEFINED, 0);

        observable.set(null);
        ExpressionHelper.fireValueChangedEvent(helper);
        invalidationListener[0].check(observable, 1);
        changeListener[0].check(observable, DATA_1, null, 1);

        ExpressionHelper.fireValueChangedEvent(helper);
        invalidationListener[0].check(observable, 1);
        changeListener[0].check(null, UNDEFINED, UNDEFINED, 0);

        observable.set(DATA_1);
        ExpressionHelper.fireValueChangedEvent(helper);
        invalidationListener[0].check(observable, 1);
        changeListener[0].check(observable, null, DATA_1, 1);

        ExpressionHelper.fireValueChangedEvent(helper);
        invalidationListener[0].check(observable, 1);
        changeListener[0].check(null, UNDEFINED, UNDEFINED, 0);
    }

}
