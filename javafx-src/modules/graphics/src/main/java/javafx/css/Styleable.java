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

package javafx.css;

import java.util.List;

import javafx.beans.property.Property;
import javafx.beans.value.WritableValue;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.scene.text.Font;

/** 
 * Styleable comprises the minimal interface required for an object to be styled by CSS.
 * @see <a href="../scene/doc-files/cssref.html">CSS Reference Guide</a>.
 * @since JavaFX 8.0
 */
public interface Styleable { 

    /** 
     * The type of this {@code Styleable} that is to be used in selector matching. 
     * This is analogous to an "element" in HTML. 
     * (<a href="http://www.w3.org/TR/CSS2/selector.html#type-selectors">CSS Type Selector</a>). 
     */ 
    String getTypeSelector(); 

    /** 
     * The id of this {@code Styleable}. This simple string identifier is useful for 
     * finding a specific Node within the scene graph. While the id of a Node 
     * should be unique within the scene graph, this uniqueness is not enforced. 
     * This is analogous to the "id" attribute on an HTML element 
     * (<a href="http://www.w3.org/TR/CSS21/syndata.html#value-def-identifier">CSS ID Specification</a>). 
     * <p> 
     * For example, if a Node is given the id of "myId", then the lookup method can 
     * be used to find this node as follows: <code>scene.lookup("#myId");</code>. 
     * </p> 
     */ 
    String getId(); 

    /** 
     * A list of String identifiers which can be used to logically group 
     * Nodes, specifically for an external style engine. This variable is 
     * analogous to the "class" attribute on an HTML element and, as such, 
     * each element of the list is a style class to which this Node belongs. 
     * 
     * @see <a href="http://www.w3.org/TR/css3-selectors/#class-html">CSS3 class selectors</a> 
     */ 
   ObservableList<String> getStyleClass(); 

    /** 
     * A string representation of the CSS style associated with this 
     * specific {@code Node}. This is analogous to the "style" attribute of an 
     * HTML element. Note that, like the HTML style attribute, this 
     * variable contains style properties and values and not the 
     * selector portion of a style rule. 
     */ 
   String getStyle(); 
     
    /**
     * The CssMetaData of this Styleable. This may be returned as
     * an unmodifiable list.
     *
     */
    List<CssMetaData<? extends Styleable, ?>> getCssMetaData(); 
            
    /** 
     * Return the parent of this Styleable, or null if there is no parent. 
     */ 
    Styleable getStyleableParent();

    /**
     * Return the pseudo-class state of this Styleable. CSS assumes this set is read-only.
     */
    ObservableSet<PseudoClass> getPseudoClassStates();

}
