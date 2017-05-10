/*
 * Copyright (c) 2010, 2013, Oracle and/or its affiliates. All rights reserved.
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

package javafx.scene.control;

import javafx.geometry.Pos;
import com.sun.javafx.accessible.providers.AccessibleProvider;
import com.sun.javafx.scene.control.accessible.AccessibleRadioButton;
import com.sun.javafx.scene.control.skin.RadioButtonSkin;
import javafx.css.StyleableProperty;

/**
 * <p>RadioButtons create a series of items where only one item can be
 * selected.  RadioButtons are a specialized {@link ToggleButton}.
 * When a RadioButton is pressed and released a {@link javafx.event.ActionEvent}
 * is sent. Your application can perform some action based
 * on this event by implementing an {@link javafx.event.EventHandler} to
 * process the {@link javafx.event.ActionEvent}.</p>
 * 
 * <p>
 * Only one RadioButton can be selected when placed in a {@link ToggleGroup}.
 * Clicking on a selected RadioButton will have no effect.  A RadioButton that is not
 * in a ToggleGroup can be selected and unselected.  By default a RadioButton is
 * not in a ToggleGroup.  Calling {@code ToggleGroup.getSelectedToggle()}
 * will return you the RadioButton that has been selected.
 * </p>
 *
 * <pre>
 * <code>
 *    ToggleGroup group = new ToggleGroup();
 *    RadioButton button1 = new RadioButton("select first");
 *    button1.setToggleGroup(group);
 *    button1.setSelected(true);
 *    RadioButton button2 = new RadioButton("select second");
 *    button2.setToggleGroup(group);
 * </code>
 * </pre>
 * @since JavaFX 2.0
 */
 public class RadioButton extends ToggleButton {

    /***************************************************************************
     *                                                                         *
     * Constructors                                                            *
     *                                                                         *
     **************************************************************************/

    /**
     * Creates a radio button with an empty string for its label.
     */
    public RadioButton() {
        initialize();
    }

    /**
     * Creates a radio button with the specified text as its label.
     *
     * @param text A text string for its label.
     */
    public RadioButton(String text) {
        setText(text);
        initialize();
    }

    private void initialize() {
        getStyleClass().setAll(DEFAULT_STYLE_CLASS);
        // alignment is styleable through css. Calling setAlignment
        // makes it look to css like the user set the value and css will not 
        // override. Initializing alignment by calling set on the 
        // CssMetaData ensures that css will be able to override the value.
        ((StyleableProperty)alignmentProperty()).applyStyle(null, Pos.CENTER_LEFT);
    }

    /***************************************************************************
     *                                                                         *
     * Methods                                                                 *
     *                                                                         *
     **************************************************************************/

    /**
     * Toggles the state of the radio button if and only if the RadioButton
     * has not already selected or is not part of a {@link ToggleGroup}.
     */
    @Override public void fire() {
        // we don't toggle from selected to not selected if part of a group
        if (getToggleGroup() == null || !isSelected()) {
            super.fire();
        }
    }

    /** {@inheritDoc} */
    @Override protected Skin<?> createDefaultSkin() {
        return new RadioButtonSkin(this);
    }


    /***************************************************************************
     *                                                                         *
     * Stylesheet Handling                                                     *
     *                                                                         *
     **************************************************************************/

    private static final String DEFAULT_STYLE_CLASS = "radio-button";
    
    private AccessibleRadioButton accRadioButton ;
    /**
     * @treatAsPrivate implementation detail
     * @deprecated This is an internal API that is not intended for use and will be removed in the next version
     */
    @Deprecated @Override public AccessibleProvider impl_getAccessible() {
        if( accRadioButton == null)
            accRadioButton = new AccessibleRadioButton(this);
        return (AccessibleProvider)accRadioButton ;
    }

    /**
      * Labeled return CENTER_LEFT for alignment, but ToggleButton returns
      * CENTER. RadioButton also returns CENTER_LEFT so we have to override
      * the override in ToggleButton. 
      * @treatAsPrivate implementation detail
      */
    @Deprecated @Override
    protected Pos impl_cssGetAlignmentInitialValue() {
        return Pos.CENTER_LEFT;
    }

}
