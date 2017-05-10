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

package com.sun.javafx.css;

import java.util.List;
import javafx.css.PseudoClass;
import static javafx.geometry.NodeOrientation.*;


/**
 * Used by {@link Rule} to determine whether or not the selector applies to a
 * given object.
 *
/**
 * Returned by {@link Selector#matches} in the event of a match.
 */
final class Match implements Comparable<Match> {

    final Selector selector;
    final PseudoClassState pseudoClasses;
    final int idCount;
    final int styleClassCount;
    
    List<Match> descendantMatches;
    
    // CSS3 spec gives weight to id count, then style class count,
    // then pseudoclass count, and finally matching types (i.e., java name count)
    final int specificity;

    Match(final Selector selector, PseudoClassState pseudoClasses,
            int idCount, int styleClassCount) {
        assert selector != null;
        this.selector = selector;
        this.idCount = idCount;
        this.styleClassCount = styleClassCount;
        this.pseudoClasses = pseudoClasses;
        int nPseudoClasses = pseudoClasses != null ? pseudoClasses.size() : 0;
        if (selector instanceof SimpleSelector) {
            final SimpleSelector simple = (SimpleSelector)selector;
            if (simple.getNodeOrientation() != INHERIT) {
                nPseudoClasses += 1;
            }
        }
        specificity = (idCount << 8) | (styleClassCount << 4) | nPseudoClasses;
    }
        
    @Override
    public int compareTo(Match o) {
        return specificity - o.specificity;
    }

}
