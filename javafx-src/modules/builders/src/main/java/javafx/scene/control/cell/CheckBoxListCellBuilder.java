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

package javafx.scene.control.cell;

/**
Builder class for javafx.scene.control.cell.CheckBoxListCell
@see javafx.scene.control.cell.CheckBoxListCell
@deprecated This class is deprecated and will be removed in the next version
* @since JavaFX 2.2
*/
@javax.annotation.Generated("Generated by javafx.builder.processor.BuilderProcessor")
@Deprecated
public class CheckBoxListCellBuilder<T, B extends javafx.scene.control.cell.CheckBoxListCellBuilder<T, B>> extends javafx.scene.control.ListCellBuilder<T, B> {
    protected CheckBoxListCellBuilder() {
    }
    
    /** Creates a new instance of CheckBoxListCellBuilder. */
    @SuppressWarnings({"deprecation", "rawtypes", "unchecked"})
    public static <T> javafx.scene.control.cell.CheckBoxListCellBuilder<T, ?> create() {
        return new javafx.scene.control.cell.CheckBoxListCellBuilder();
    }
    
    private int __set;
    public void applyTo(javafx.scene.control.cell.CheckBoxListCell<T> x) {
        super.applyTo(x);
        int set = __set;
        if ((set & (1 << 0)) != 0) x.setConverter(this.converter);
        if ((set & (1 << 1)) != 0) x.setSelectedStateCallback(this.selectedStateCallback);
    }
    
    private javafx.util.StringConverter<T> converter;
    /**
    Set the value of the {@link javafx.scene.control.cell.CheckBoxListCell#getConverter() converter} property for the instance constructed by this builder.
    */
    @SuppressWarnings("unchecked")
    public B converter(javafx.util.StringConverter<T> x) {
        this.converter = x;
        __set |= 1 << 0;
        return (B) this;
    }
    
    private javafx.util.Callback<T,javafx.beans.value.ObservableValue<java.lang.Boolean>> selectedStateCallback;
    /**
    Set the value of the {@link javafx.scene.control.cell.CheckBoxListCell#getSelectedStateCallback() selectedStateCallback} property for the instance constructed by this builder.
    */
    @SuppressWarnings("unchecked")
    public B selectedStateCallback(javafx.util.Callback<T,javafx.beans.value.ObservableValue<java.lang.Boolean>> x) {
        this.selectedStateCallback = x;
        __set |= 1 << 1;
        return (B) this;
    }
    
    /**
    Make an instance of {@link javafx.scene.control.cell.CheckBoxListCell} based on the properties set on this builder.
    */
    public javafx.scene.control.cell.CheckBoxListCell<T> build() {
        javafx.scene.control.cell.CheckBoxListCell<T> x = new javafx.scene.control.cell.CheckBoxListCell<T>();
        applyTo(x);
        return x;
    }
}
