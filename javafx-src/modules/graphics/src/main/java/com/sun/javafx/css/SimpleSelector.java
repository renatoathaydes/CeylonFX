/*
 * Copyright (c) 2008, 2013, Oracle and/or its affiliates. All rights reserved.
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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javafx.css.PseudoClass;
import javafx.css.Styleable;
import javafx.geometry.NodeOrientation;
import static javafx.geometry.NodeOrientation.*;
import javafx.scene.Node;
import javafx.scene.Scene;

/**
 * A simple selector which behaves according to the CSS standard.
 *
 */
final public class SimpleSelector extends Selector {
    
    /**
     * If specified in the CSS file, the name of the java class to which
     * this selector is applied. For example, if the CSS file had:
     * <code><pre>
     *   Rectangle { }
     * </pre></code>
     * then name would be "Rectangle".
     */
    final private String name;
    /**
     * @return The name of the java class to which this selector is applied, or *.
     */
    public String getName() {
        return name;
    }
    
    /**
     * @return Immutable List&lt;String&gt; of style-classes of the selector
     */
    public List<String> getStyleClasses() {
        
        final List<String> names = new ArrayList<String>();
        
        Iterator<StyleClass> iter = styleClassSet.iterator();
        while (iter.hasNext()) {
            names.add(iter.next().getStyleClassName());
        }
        
        return Collections.unmodifiableList(names);
    }

    Set<StyleClass> getStyleClassSet() {
        return styleClassSet;
    }
    
    /** styleClasses converted to a set of bit masks */
    final private StyleClassSet styleClassSet;
    
    final private String id;
    /*
     * @return The value of the selector id, which may be an empty string.
     */
    public String getId() {
        return id;
    }
    
    // a mask of bits corresponding to the pseudoclasses
    final private PseudoClassState pseudoClassState;
    
    Set<PseudoClass> getPseudoClassStates() {
        return pseudoClassState;
    }

    /**
     * @return Immutable List&lt;String&gt; of pseudo-classes of the selector
     */
    public List<String> getPseudoclasses() {
        
        final List<String> names = new ArrayList<String>();
        
        Iterator<PseudoClass> iter = pseudoClassState.iterator();
        while (iter.hasNext()) {
            names.add(iter.next().getPseudoClassName());
        }
        
        if (nodeOrientation == RIGHT_TO_LEFT) {
            names.add("dir(rtl)");
        } else if (nodeOrientation == LEFT_TO_RIGHT) {
            names.add("dir(ltr)");
        }
                    
        return Collections.unmodifiableList(names);
    }
        
    // true if name is not a wildcard
    final private boolean matchOnName;

    // true if id given
    final private boolean matchOnId;

    // true if style class given
    final private boolean matchOnStyleClass;

    // dir(ltr) or dir(rtl), otherwise inherit
    final private NodeOrientation nodeOrientation;
    
    // Used in Match. If nodeOrientation is ltr or rtl, 
    // then count it as a pseudoclass
    NodeOrientation getNodeOrientation() {
        return nodeOrientation;
    }
    
    // TODO: The parser passes styleClasses as a List. Should be array?
    public SimpleSelector(final String name, final List<String> styleClasses,
            final List<String> pseudoClasses, final String id)
    {
        this.name = name == null ? "*" : name;
        // if name is not null and not empty or wildcard, 
        // then match needs to check name
        this.matchOnName = (name != null && !("".equals(name)) && !("*".equals(name)));

        this.styleClassSet = new StyleClassSet();
        
        int nMax = styleClasses != null ? styleClasses.size() : 0;
        for(int n=0; n<nMax; n++) { 
            
            final String styleClassName = styleClasses.get(n);
            if (styleClassName == null || styleClassName.isEmpty()) continue;
            
            final StyleClass styleClass = StyleClassSet.getStyleClass(styleClassName);
            this.styleClassSet.add(styleClass);
        }
        
        this.matchOnStyleClass = (this.styleClassSet.size() > 0);

        this.pseudoClassState = new PseudoClassState();
        
        nMax = pseudoClasses != null ? pseudoClasses.size() : 0;

        NodeOrientation dir = NodeOrientation.INHERIT;
        for(int n=0; n<nMax; n++) { 
            
            final String pclass = pseudoClasses.get(n);
            if (pclass == null || pclass.isEmpty()) continue;
            
            // TODO: This is not how we should handle functional pseudo-classes in the long-run!
            if ("dir(".regionMatches(true, 0, pclass, 0, 4)) {
                final boolean rtl = "dir(rtl)".equalsIgnoreCase(pclass);
                dir = rtl ? RIGHT_TO_LEFT : LEFT_TO_RIGHT;
                continue;
            }
            
            final PseudoClass pseudoClass = PseudoClassState.getPseudoClass(pclass);
            this.pseudoClassState.add(pseudoClass);
        }
        
        this.nodeOrientation = dir;
        this.id = id == null ? "" : id;
        // if id is not null and not empty, then match needs to check id
        this.matchOnId = (id != null && !("".equals(id)));

    }

    Match createMatch() {
        final int idCount = (matchOnId) ? 1 : 0;
        int styleClassCount = styleClassSet.size();
        return new Match(this, pseudoClassState, idCount, styleClassCount);
    }

    @Override 
    public boolean applies(Styleable styleable) {
        
        // handle functional pseudo-class :dir()
        // INHERIT applies to both :dir(rtl) and :dir(ltr)
        if (nodeOrientation != INHERIT && styleable instanceof Node) {
            
            final Scene scene = ((Node)styleable).getScene();
            final NodeOrientation effectiveNodeOrientation =
                    scene.getEffectiveNodeOrientation();
            
            if (effectiveNodeOrientation != INHERIT &&
                    effectiveNodeOrientation != nodeOrientation) {
                return false;
            }
        }
        
        // if the selector has an id,
        // then bail if it doesn't match the node's id
        // (do this first since it is potentially the cheapest check)
        if (matchOnId) {
            final String otherId = styleable.getId();
            final boolean idMatch = id.equals(otherId);
            if (!idMatch) return false;
        }

        // If name is not a wildcard,
        // then bail if it doesn't match the node's class name
        // if not wildcard, then match name with node's class name
        if (matchOnName) {
            final String otherName = styleable.getClass().getName();
            final boolean classMatch = nameMatchesAtEnd(otherName);
            if (!classMatch) return false;
        }

        if (matchOnStyleClass) {
            
            final StyleClassSet otherStyleClassSet = new StyleClassSet();
            final List<String> styleClasses = styleable.getStyleClass();
            for(int n=0, nMax = styleClasses.size(); n<nMax; n++) { 

                final String styleClassName = styleClasses.get(n);
                if (styleClassName == null || styleClassName.isEmpty()) continue;

                final StyleClass styleClass = StyleClassSet.getStyleClass(styleClassName);
                otherStyleClassSet.add(styleClass);
            }
            
            boolean styleClassMatch = matchStyleClasses(otherStyleClassSet); 
            if (!styleClassMatch) return false;
        }
        
        return true;
    }
    
    @Override 
    boolean applies(Styleable styleable, Set<PseudoClass>[] pseudoClasses, int depth) {

        
        final boolean applies = applies(styleable);
        
        //
        // We only need the pseudo-classes if the selector applies to the node.
        // 
        if (applies && pseudoClasses != null && depth < pseudoClasses.length) {

            if (pseudoClasses[depth] == null) {
                pseudoClasses[depth] = new PseudoClassState();
            }
            
            pseudoClasses[depth].addAll(pseudoClassState);
            
        }
        return applies;
    }

    // todo - is this used?
    private boolean mightApply(final String className, final String id, StyleClassSet styleClasses) {
        if (matchOnName && nameMatchesAtEnd(className)) return true;
        if (matchOnId   && this.id.equals(id)) return true;
        if (matchOnStyleClass) return matchStyleClasses(styleClasses);
        return false;
    }
    
    @Override
    public boolean stateMatches(final Styleable styleable, Set<PseudoClass> states) {
        // [foo bar] matches [foo bar bang], 
        // but [foo bar bang] doesn't match [foo bar]
        return states != null ? states.containsAll(pseudoClassState) : false;
    }

    /*
     * Optimized className.equals(name) || className.endsWith(".".concat(name)).
     */
    private boolean nameMatchesAtEnd(final String className) {
        // if name is null or empty, why bother?
        if (!matchOnName) return false;

        final int nameLen = this.name.length();

        // take a guess that '.name' starts at this offset in className
        final int dotPos = className.length() - nameLen - 1;

        // If dotPos is -1, then className and name are
        // equal length and may match. Anything less than -1 and
        // className is shorter than name and no match is possible.
        if ((dotPos == -1) || (dotPos > -1 && className.charAt(dotPos) == '.')) {
            return className.regionMatches(dotPos+1, this.name, 0, nameLen);
        }
        return false;
    }

    // Are the Selector's style classes a subset of the Node's style classes?
    //
    // http://www.w3.org/TR/css3-selectors/#class-html
    // The following selector matches any P element whose class attribute has been
    // assigned a list of whitespace-separated values that includes both
    // pastoral and marine:
    //
    //     p.pastoral.marine { color: green }
    //
    // This selector matches when class="pastoral blue aqua marine" but does not
    // match for class="pastoral blue".
    private boolean matchStyleClasses(StyleClassSet otherStyleClasses) {
        return otherStyleClasses.containsAll(styleClassSet);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SimpleSelector other = (SimpleSelector) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
            return false;
        }
        if (this.styleClassSet.equals(other.styleClassSet) == false) {
            return false;
        }
        if (this.pseudoClassState.equals(other.pseudoClassState) == false) {
            return false;
        } 
        
        return true;
    }

    /* Hash code is used in Style's hash code and Style's hash 
       code is used by StyleHelper */
    @Override public int hashCode() {
        int hash = 7;
        hash = 31 * (hash + name.hashCode());
        hash = 31 * (hash + styleClassSet.hashCode());
        hash = 31 * (hash + styleClassSet.hashCode());
        hash = (id != null) ? 31 * (hash + id.hashCode()) : 0;
        hash = 31 * (hash + pseudoClassState.hashCode());
        return hash;
    }

    /** Converts this object to a string. */
    @Override public String toString() {
        
        StringBuilder sbuf = new StringBuilder();
        if (name != null && name.isEmpty() == false) sbuf.append(name);
        else sbuf.append("*");
        Iterator<StyleClass> iter1 = styleClassSet.iterator();
        while(iter1.hasNext()) {
            final StyleClass styleClass = iter1.next();
            sbuf.append('.').append(styleClass.getStyleClassName());
        }
        if (id != null && id.isEmpty() == false) {
            sbuf.append('#');
            sbuf.append(id);
        }
        Iterator<PseudoClass> iter2 = pseudoClassState.iterator();
        while(iter2.hasNext()) {
            final PseudoClass pseudoClass = iter2.next();
            sbuf.append(':').append(pseudoClass.getPseudoClassName());
        }
            
        return sbuf.toString();
    }

    public final void writeBinary(final DataOutputStream os, final StringStore stringStore)
        throws IOException
    {
        super.writeBinary(os, stringStore);
        os.writeShort(stringStore.addString(name));
        os.writeShort(styleClassSet.size());
        Iterator<StyleClass> iter1 = styleClassSet.iterator();
        while(iter1.hasNext()) {
            final StyleClass sc = iter1.next();
            os.writeShort(stringStore.addString(sc.getStyleClassName()));
        }
        os.writeShort(stringStore.addString(id));
        os.writeShort(pseudoClassState.size());
        Iterator<PseudoClass> iter2 = pseudoClassState.iterator();
        while(iter2.hasNext()) {
            final PseudoClass pc = iter2.next();
            os.writeShort(stringStore.addString(pc.getPseudoClassName()));
        }
    }

    static SimpleSelector readBinary(int bssVersion, final DataInputStream is, final String[] strings)
        throws IOException
    {
        final String name = strings[is.readShort()];
        final int nStyleClasses = is.readShort();
        final List<String> styleClasses = new ArrayList<String>();
        for (int n=0; n < nStyleClasses; n++) {
            styleClasses.add(strings[is.readShort()]);
        }
        final String id = strings[is.readShort()];
        final int nPseudoclasses = is.readShort();
        final List<String> pseudoclasses = new ArrayList<String>();
        for(int n=0; n < nPseudoclasses; n++) {
            pseudoclasses.add(strings[is.readShort()]);
        }
        return new SimpleSelector(name, styleClasses, pseudoclasses, id);
    }
}
