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

package javafx.geometry;

/**
Builder class for javafx.geometry.Insets
@see javafx.geometry.Insets
@deprecated This class is deprecated and will be removed in the next version
* @since JavaFX 2.0
*/
@javax.annotation.Generated("Generated by javafx.builder.processor.BuilderProcessor")
@Deprecated
public class InsetsBuilder<B extends javafx.geometry.InsetsBuilder<B>> implements javafx.util.Builder<javafx.geometry.Insets> {
    protected InsetsBuilder() {
    }
    
    /** Creates a new instance of InsetsBuilder. */
    @SuppressWarnings({"deprecation", "rawtypes", "unchecked"})
    public static javafx.geometry.InsetsBuilder<?> create() {
        return new javafx.geometry.InsetsBuilder();
    }
    
    private double bottom;
    /**
    Set the value of the {@link javafx.geometry.Insets#getBottom() bottom} property for the instance constructed by this builder.
    */
    @SuppressWarnings("unchecked")
    public B bottom(double x) {
        this.bottom = x;
        return (B) this;
    }
    
    private double left;
    /**
    Set the value of the {@link javafx.geometry.Insets#getLeft() left} property for the instance constructed by this builder.
    */
    @SuppressWarnings("unchecked")
    public B left(double x) {
        this.left = x;
        return (B) this;
    }
    
    private double right;
    /**
    Set the value of the {@link javafx.geometry.Insets#getRight() right} property for the instance constructed by this builder.
    */
    @SuppressWarnings("unchecked")
    public B right(double x) {
        this.right = x;
        return (B) this;
    }
    
    private double top;
    /**
    Set the value of the {@link javafx.geometry.Insets#getTop() top} property for the instance constructed by this builder.
    */
    @SuppressWarnings("unchecked")
    public B top(double x) {
        this.top = x;
        return (B) this;
    }
    
    /**
    Make an instance of {@link javafx.geometry.Insets} based on the properties set on this builder.
    */
    public javafx.geometry.Insets build() {
        javafx.geometry.Insets x = new javafx.geometry.Insets(this.top, this.right, this.bottom, this.left);
        return x;
    }
}
