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

package javafx.scene.effect;

import java.util.Arrays;
import java.util.Collection;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.sun.javafx.test.PropertiesTestBase;

@RunWith(Parameterized.class)
public final class DisplacementMap_properties_Test extends PropertiesTestBase {
    @Parameters
    public static Collection data() {
        final DisplacementMap testDisplacementMap = new DisplacementMap();

        return Arrays.asList(new Object[] {
            config(testDisplacementMap, "input", null, new BoxBlur()),
            config(testDisplacementMap, "mapData", null, new FloatMap()),
            config(testDisplacementMap, "scaleX", 1.0, 0.5),
            config(testDisplacementMap, "scaleY", 1.0, 0.5),
            config(testDisplacementMap, "offsetX", 0.0, 10.0),
            config(testDisplacementMap, "offsetY", 0.0, 10.0),
            config(testDisplacementMap, "wrap", false, true)
        });
    }

    public DisplacementMap_properties_Test(final Configuration configuration) {
        super(configuration);
    }
}
