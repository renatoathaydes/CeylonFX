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

package com.sun.scenario.effect.compiler.tree;

import com.sun.scenario.effect.compiler.model.BaseType;
import com.sun.scenario.effect.compiler.model.Type;

/**
 */
public class FieldSelectExpr extends Expr {

    private final Expr expr;
    private final String fields;

    FieldSelectExpr(Expr expr, String fields) {
        super(getType(expr.getResultType(), fields));
        this.expr = expr;
        this.fields = fields;
    }
    
    private static Type getType(Type orig, String fields) {
        BaseType base = orig.getBaseType();
        int len = fields.length();
        for (Type type : Type.values()) {
            if (type.getBaseType() == base && type.getNumFields() == len) {
                return type;
            }
        }
        throw new RuntimeException("Invalid type for field selection");
    }

    public Expr getExpr() {
        return expr;
    }

    public String getFields() {
        return fields;
    }

    public void accept(TreeVisitor tv) {
        tv.visitFieldSelectExpr(this);
    }
}
