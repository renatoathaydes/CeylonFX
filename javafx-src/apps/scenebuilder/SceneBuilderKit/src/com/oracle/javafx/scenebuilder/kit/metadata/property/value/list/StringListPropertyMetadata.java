/*
 * Copyright (c) 2012, 2014, Oracle and/or its affiliates.
 * All rights reserved. Use is subject to license terms.
 *
 * This file is available and licensed under the following license:
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  - Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the distribution.
 *  - Neither the name of Oracle Corporation nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.oracle.javafx.scenebuilder.kit.metadata.property.value.list;

import com.oracle.javafx.scenebuilder.kit.metadata.property.value.StringPropertyMetadata;
import com.oracle.javafx.scenebuilder.kit.metadata.util.InspectorPath;
import com.oracle.javafx.scenebuilder.kit.metadata.util.PropertyName;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXMLLoader;

/**
 *
 */
public class StringListPropertyMetadata extends ListValuePropertyMetadata<String> {

    private final static StringPropertyMetadata itemMetadata
            = new StringPropertyMetadata(new PropertyName("unused"), //NOI18N
                    true, null, InspectorPath.UNUSED);

    public StringListPropertyMetadata(PropertyName name, boolean readWrite, 
            List<String> defaultValue, InspectorPath inspectorPath) {
        super(name, String.class, itemMetadata, readWrite, defaultValue, inspectorPath);
    }
    
    
    /*
     * ListValuePropertyMetadata
     */
    
    @Override
    protected boolean canMakeStringFromValue(List<String> value) {
        boolean result = true;
        
        // Returns false if one the string item contains a comma
        for (String s : value) {
            if (s.indexOf(FXMLLoader.ARRAY_COMPONENT_DELIMITER) != -1) {
                result = false;
                break;
            }
        }
        
        return result;
    }
    
    @Override
    protected String makeStringFromValue(List<String> value) {
        assert canMakeStringFromValue(value);
        
        final StringBuilder result = new StringBuilder();
        for (String s : value) {
            assert value.indexOf(FXMLLoader.ARRAY_COMPONENT_DELIMITER) == -1;
            if (result.length() >= 1) {
                result.append(FXMLLoader.ARRAY_COMPONENT_DELIMITER);
            }
            result.append(s);
        }
        
        return result.toString();
    }
    
    @Override
    protected List<String> makeValueFromString(String string) {
        final List<String> result = new ArrayList<>();
        
        final String[] values = string.split(FXMLLoader.ARRAY_COMPONENT_DELIMITER);
        for (int i = 0, count = values.length; i < count; i++) {
            result.add(values[i]);
        }
        
        return result;
    }
}
