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

package com.sun.javafx.css;

import com.sun.javafx.css.converters.FontConverter;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.tk.Toolkit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.css.CssMetaData;
import javafx.css.ParsedValue;
import javafx.css.StyleOrigin;
import javafx.css.StyleableProperty;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

public class Node_cssStyleMap_Test {
    
    public Node_cssStyleMap_Test() {
    }

    boolean disabled = false;
    int nadds = 0;
    int nremoves = 0;

    static List<CascadingStyle> createStyleList(List<Declaration> decls) {
        
        final List<CascadingStyle> styles = new ArrayList<CascadingStyle>();
        
        for (Declaration decl : decls) {
            styles.add(
                new CascadingStyle(
                    new Style(decl.rule.getUnobservedSelectorList().get(0), decl),
                    new PseudoClassState(),
                    0, 
                    0
                )
            );
        }
        
        return styles;
    }
    
    static Map<String, List<CascadingStyle>> createStyleMap(List<CascadingStyle> styles) {
        
        final Map<String, List<CascadingStyle>> smap = 
            new HashMap<String, List<CascadingStyle>>();
        
        final int max = styles != null ? styles.size() : 0;
        for (int i=0; i<max; i++) {
            final CascadingStyle style = styles.get(i);
            final String property = style.getProperty();
            // This is carefully written to use the minimal amount of hashing.
            List<CascadingStyle> list = smap.get(property);
            if (list == null) {
                list = new ArrayList<CascadingStyle>(5);
                smap.put(property, list);
            }
            list.add(style);
        }
        return smap;
    }
    
    @Test @Ignore ("Pending RT-34463")
    public void testStyleMapTracksChanges() {
                
        final List<Declaration> declsNoState = new ArrayList<Declaration>();
        Collections.addAll(declsNoState, 
            new Declaration("-fx-fill", new ParsedValueImpl<Color,Color>(Color.RED, null), false),
            new Declaration("-fx-stroke", new ParsedValueImpl<Color,Color>(Color.YELLOW, null), false),
            new Declaration("-fx-stroke-width", new ParsedValueImpl<ParsedValue<?,Size>,Number>(
                new ParsedValueImpl<Size,Size>(new Size(3d, SizeUnits.PX), null), 
                SizeConverter.getInstance()), false)
        );
        
        
        final List<Selector> selsNoState = new ArrayList<Selector>();
        Collections.addAll(selsNoState, 
            Selector.createSelector(".rect")
        );
        
        Rule rule = new Rule(selsNoState, declsNoState);        
        
        Stylesheet stylesheet = new Stylesheet("testStyleMapTracksChanges");
        stylesheet.setOrigin(StyleOrigin.USER_AGENT);
        stylesheet.getRules().add(rule);
        
        final List<Declaration> declsDisabledState = new ArrayList<Declaration>();
        Collections.addAll(declsDisabledState, 
            new Declaration("-fx-fill", new ParsedValueImpl<Color,Color>(Color.GRAY, null), false),
            new Declaration("-fx-stroke", new ParsedValueImpl<Color,Color>(Color.DARKGRAY, null), false)
        );
        
        final List<Selector> selsDisabledState = new ArrayList<Selector>();
        Collections.addAll(selsDisabledState, 
            Selector.createSelector(".rect:disabled")
        );
        
        rule = new Rule(selsDisabledState, declsDisabledState);        
        stylesheet.getRules().add(rule);
        
        final List<CascadingStyle> stylesNoState = createStyleList(declsNoState);
        final List<CascadingStyle> stylesDisabledState = createStyleList(declsDisabledState);
        
        // add to this list on wasAdded, check bean on wasRemoved.
        final List<StyleableProperty<?>> beans = new ArrayList<StyleableProperty<?>>();
        Rectangle rect = new Rectangle(50,50);
        rect.getStyleClass().add("rect");
        rect.impl_setStyleMap(FXCollections.observableMap(new HashMap<StyleableProperty<?>, List<Style>>()));
        rect.impl_getStyleMap().addListener(new MapChangeListener<StyleableProperty, List<Style>>() {

            public void onChanged(MapChangeListener.Change<? extends StyleableProperty, ? extends List<Style>> change) {

                if (change.wasAdded()) {
                    
                    List<Style> styles = change.getValueAdded();
                    for (Style style : styles) {

                        // stroke width comes from ".rect" even for disabled state.
                        if (disabled == false || "-fx-stroke-width".equals(style.getDeclaration().getProperty())) {
                            assertTrue(style.getDeclaration().toString(),declsNoState.contains(style.getDeclaration()));
                            assertTrue(style.getSelector().toString(),selsNoState.contains(style.getSelector()));
                        } else {
                            assertTrue(style.getDeclaration().toString(),declsDisabledState.contains(style.getDeclaration()));
                            assertTrue(style.getSelector().toString(),selsDisabledState.contains(style.getSelector()));                            
                        }
                        Object value = style.getDeclaration().parsedValue.convert(null);
                        StyleableProperty styleableProperty = change.getKey();
                        beans.add(styleableProperty);
                        assertEquals(styleableProperty.getValue(), value);
                        nadds += 1;                        
                    }
                    
                } if (change.wasRemoved()) {
                    StyleableProperty styleableProperty = change.getKey();
                    assert(beans.contains(styleableProperty));
                    nremoves += 1;
                }
            }
        });

        Group root = new Group();
        root.getChildren().add(rect);
        StyleManager.getInstance().setDefaultUserAgentStylesheet(stylesheet);        
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();

        // The three no state styles should be applied
        assertEquals(3, nadds);
        assertEquals(0, nremoves);

        rect.setDisable(true);
        disabled = true;
        nadds = 0;
        nremoves = 0;
        
        Toolkit.getToolkit().firePulse();
        
        // The three no state styles should be removed and the 
        // two disabled state styles plus the stroke width style 
        // should be applied. 
        assertEquals(3, nadds);
        assertEquals(3, nremoves);
        
    }
    
    @Test @Ignore ("Pending RT-34463")
    public void testRT_21212() {

        final List<Declaration> rootDecls = new ArrayList<Declaration>();
        Collections.addAll(rootDecls, 
            new Declaration("-fx-font-size", new ParsedValueImpl<ParsedValue<?,Size>,Number>(
                new ParsedValueImpl<Size,Size>(new Size(12, SizeUnits.PX), null), 
                SizeConverter.getInstance()), false)
        );
        
        final List<Selector> rootSels = new ArrayList<Selector>();
        Collections.addAll(rootSels, 
            Selector.createSelector(".root")
        );
        
        Rule rootRule = new Rule(rootSels, rootDecls);        
        
        Stylesheet stylesheet = new Stylesheet("testRT_21212");
        stylesheet.setOrigin(StyleOrigin.USER_AGENT);
        stylesheet.getRules().add(rootRule);

        final List<CascadingStyle> rootStyles = createStyleList(rootDecls);
        final Map<String,List<CascadingStyle>> rootStyleMap = createStyleMap(rootStyles);
        final Map<StyleCache.Key, StyleCache> styleCache = 
            new HashMap<StyleCache.Key, StyleCache>();
        
        Group group = new Group();
        group.getStyleClass().add("root");
        
        
        final ParsedValue[] fontValues = new ParsedValue[] {
            new ParsedValueImpl<String,String>("system", null),
            new ParsedValueImpl<ParsedValue<?,Size>,Number>(
                new ParsedValueImpl<Size,Size>(new Size(1.5, SizeUnits.EM), null),
                SizeConverter.getInstance()
            ), 
            null,
            null
        };
        final List<Declaration> textDecls = new ArrayList<Declaration>();
        Collections.addAll(textDecls, 
            new Declaration("-fx-font", new ParsedValueImpl<ParsedValue[], Font>(
                fontValues, FontConverter.getInstance()), false)
        );
        
        final List<Selector> textSels = new ArrayList<Selector>();
        Collections.addAll(textSels, 
            Selector.createSelector(".text")
        );
        
        Rule textRule = new Rule(textSels, textDecls);        
        stylesheet.getRules().add(textRule);
                
        final List<CascadingStyle> styles = createStyleList(textDecls);
        final Map<String,List<CascadingStyle>> styleMap = createStyleMap(styles);
        final Map<String,List<CascadingStyle>> emptyMap = createStyleMap(null);

        Text text = new Text("HelloWorld");
        group.getChildren().add(text);

        final List<Declaration> expecteds = new ArrayList<Declaration>();
        expecteds.addAll(rootDecls);
        expecteds.addAll(textDecls);
        text.getStyleClass().add("text");
        text.impl_setStyleMap(FXCollections.observableMap(new HashMap<StyleableProperty<?>, List<Style>>()));
        text.impl_getStyleMap().addListener(new MapChangeListener<StyleableProperty, List<Style>>() {

            // a little different than the other tests since we should end up 
            // with font and font-size in the map and nothing else. After all 
            // the changes have been handled, the expecteds list should be empty.
            public void onChanged(MapChangeListener.Change<? extends StyleableProperty, ? extends List<Style>> change) {
                if (change.wasAdded()) {
                    List<Style> styles = change.getValueAdded();
                    for (Style style : styles) {
                        assertTrue(expecteds.contains(style.getDeclaration()));
                        expecteds.remove(style.getDeclaration());
                    }
                }
            }
        });
             
        StyleManager.getInstance().setDefaultUserAgentStylesheet(stylesheet);        
        Scene scene = new Scene(group);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();

        assertEquals(18, text.getFont().getSize(),0);
        assertTrue(Integer.toString(expecteds.size()), expecteds.isEmpty());

    }

    boolean containsProperty(CssMetaData key, Map<String,List<CascadingStyle>> map) {

        if (map.containsKey(key)) return true;
        List<CssMetaData> subProperties = key.getSubProperties();
        if (subProperties != null && !subProperties.isEmpty()) {
            for (CssMetaData subKey: subProperties) {
                if (map.containsKey(subKey)) return true;
            }
        }
        return false;
    }

    @Test
    public void testRT_34799() {

        Stylesheet stylesheet = new Stylesheet("testRT_34799");
        stylesheet.setOrigin(StyleOrigin.USER_AGENT);

        final List<Declaration> txtDecls = new ArrayList<Declaration>();
        Collections.addAll(txtDecls,
                new Declaration("-fx-fill", new ParsedValueImpl<Color,Color>(Color.RED, null), false)
        );

        final List<Selector> textSels = new ArrayList<Selector>();
        Collections.addAll(textSels,
                Selector.createSelector(".rt-34799")
        );

        Rule txtRules = new Rule(textSels, txtDecls);
        stylesheet.getRules().add(txtRules);

        final List<Style> expectedStyles = new ArrayList<>();
        for (Rule rule : stylesheet.getRules()) {
            for (Selector selector : rule.getSelectors()) {
                for (Declaration declaration : rule.getUnobservedDeclarationList()) {
                    expectedStyles.add(
                            new Style(selector, declaration)
                    );
                }
            }
        }

        Text text = new Text("HelloWorld");
        text.getStyleClass().add("rt-34799");

        Group group = new Group();
        group.getStyleClass().add("root");

        group.getChildren().add(text);

        StyleManager.getInstance().setDefaultUserAgentStylesheet(stylesheet);
        Scene scene = new Scene(group);

        group.applyCss(); // TODO: force StyleHelper to be created, remove pending RT-34812

        int nExpected = expectedStyles.size();
        assert(nExpected > 0);

        for(CssMetaData cssMetaData : text.getCssMetaData()) {
            List<Style> styles = Node.impl_getMatchingStyles(cssMetaData, text);
            if (styles != null && !styles.isEmpty()) {
                assertTrue(expectedStyles.containsAll(styles));
                assertTrue(styles.containsAll(expectedStyles));
                nExpected -= 1;
            }
        }

        assertEquals(nExpected, 0);

    }

}
