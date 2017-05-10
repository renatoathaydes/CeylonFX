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

package javafx.scene.layout;

import javafx.beans.NamedArg;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.StrokeType;

/**
 * Defines the stroke to use on a Border for styling a Region. The
 * stroke is a vector-based rendering that outlines the border area.
 * It can be inset (or outset) from the Region's edge, and the values
 * of the stroke are taken into account when computing the Region's
 * insets (for defining the content area). The stroke visuals are
 * not used when any BorderImages are in use.
 * <p/>
 * When applied to a Region with a defined shape, the border width
 * and stroking information for the {@code top} is used, while the
 * other attributes are ignored.
 * @since JavaFX 8.0
 */
public class BorderStroke {
    /**
     * The default insets when "thin" is specified.
     */
    public static final BorderWidths THIN = new BorderWidths(1);

    /**
     * The default insets when "medium" is specified
     */
    public static final BorderWidths MEDIUM = new BorderWidths(3);

    /**
     * The default insets when "thick" is specified
     */
    public static final BorderWidths THICK = new BorderWidths(5);

    /**
     * The default Insets to be used with a BorderStroke that does not
     * otherwise define any.
     */
    public static final BorderWidths DEFAULT_WIDTHS = THIN;

    /**
     * Defines the fill of top side of this border.
     *
     * @defaultValue black
     */
    public final Paint getTopStroke() { return topStroke; }
    final Paint topStroke;
    // TODO: The spec says the default color is "currentColor", which appears to mean
    // by default the color is "inherit". So we should file a JIRA on this so that
    // we use inherit. But first I'd like a performance analysis.

    /**
     * Defines the fill of right side of this border. If {@code null} then the
     * topFill is used.
     *
     * @defaultValue null = same as topFill
     */
    public final Paint getRightStroke() { return rightStroke; }
    final Paint rightStroke;

    /**
     * Defines the fill of bottom side of this border. If {@code null} then the
     * topFill is used.
     *
     * @defaultValue null = same as topFill
     */
    public final Paint getBottomStroke() { return bottomStroke; }
    final Paint bottomStroke;

    /**
     * Defines the fill of left side of this border. If {@code null} then the
     * rightFill is used.
     *
     * @defaultValue null = same sa rightFill
     */
    public final Paint getLeftStroke() { return leftStroke; }
    final Paint leftStroke;

    /**
     * Defines the style of top side of this border.
     *
     * @defaultValue none
     */
    public final BorderStrokeStyle getTopStyle() { return topStyle; }
    final BorderStrokeStyle topStyle;

    /**
     * Defines the style of right side of this border. If {@code null} then
     * topStyle is used;
     *
     * @defaultValue null = same as topStyle
     */
    public final BorderStrokeStyle getRightStyle() { return rightStyle; }
    final BorderStrokeStyle rightStyle;

    /**
     * Defines the style of bottom side of this border. If {@code null} then
     * topStyle is used;  Use BorderStyle.NONE to set the border to
     * have no border style.
     *
     * @defaultValue null = same as topStyle
     */
    public final BorderStrokeStyle getBottomStyle() { return bottomStyle; }
    final BorderStrokeStyle bottomStyle;

    /**
     * Defines the style of left side of this border. If {@code null} then
     * rightStyle is used. Use BorderStyle.NONE to set the border to
     * have no border style.
     *
     * @defaultValue null = same as rightStyle
     */
    public final BorderStrokeStyle getLeftStyle() { return leftStyle; }
    final BorderStrokeStyle leftStyle;

    /**
     * Defines the thickness of each side of the BorderStroke. This will never
     * be null, and defaults to DEFAULT_WIDTHS.
     */
    public final BorderWidths getWidths() { return widths; }
    final BorderWidths widths;

    /**
     * Defines the insets of each side of the BorderStroke. This will never
     * be null, and defaults to EMPTY.
     */
    public final Insets getInsets() { return insets; }
    final Insets insets;

    // These two are used by Border to compute the insets and outsets of the border
    final Insets innerEdge;
    final Insets outerEdge;

    /**
     * Defines the radii for each corner of this BorderStroke. This will never
     * be null, and defaults to CornerRadii.EMPTY.
     */
    public final CornerRadii getRadii() { return radii; }
     /* TODO I should change CornerRadii to be 4 properties, one for each corner,
     * where each corner is a horizontal / vertical radius! I think that would
     * be cleaner. */
    private final CornerRadii radii;

    /**
     * An uniform stroke has all (top, bottom, left, right) strokes of
     * same color, width and style
     * @return true if border stroke is uniform as defined above
     */
    public final boolean isStrokeUniform() { return strokeUniform; }
    private final boolean strokeUniform;

    /**
     * A cached hash code
     */
    private final int hash;

    /**
     * Creates a new BorderStroke.
     *
     * @param stroke    The stroke to use for all sides. If null, we default to Color.BLACK.
     * @param style     The style to use for all sides. If null, we default to BorderStrokeStyle.NONE
     * @param radii     The radii to use. If null, we default to CornerRadii.EMPTY
     * @param widths    The widths to use. If null, we default to DEFAULT_WIDTHS
     */
    public BorderStroke(@NamedArg("stroke") Paint stroke, @NamedArg("style") BorderStrokeStyle style, @NamedArg("radii") CornerRadii radii, @NamedArg("widths") BorderWidths widths) {
        // TODO: Note that we default to THIN, not to MEDIUM as the CSS spec says. So it will be
        // up to our CSS converter code to make sure the default is MEDIUM in that case.
        this.leftStroke = this.topStroke = this.rightStroke = this.bottomStroke = stroke == null ? Color.BLACK : stroke;
        this.topStyle = this.rightStyle = this.bottomStyle = this.leftStyle = style == null ? BorderStrokeStyle.NONE : style;
        this.radii = radii == null ? CornerRadii.EMPTY : radii;
        this.widths = widths == null ? DEFAULT_WIDTHS : widths;
        this.insets = Insets.EMPTY;

        // TODO: Our inside / outside should be 0 when stroke type is NONE in that dimension!
        // In fact, we could adjust the widths in such a case so that when you ask for the
        // widths, you get 0 instead of whatever was specified. See 4.3 of the CSS Spec.
        StrokeType type = this.topStyle.getType();
        double inside, outside;
        if (type == StrokeType.OUTSIDE) {
            outside = this.widths.getTop();
            inside = 0;
        } else if (type == StrokeType.CENTERED) {
            final double width = this.getWidths().getTop();
            outside = inside = width / 2.0;
        } else if (type == StrokeType.INSIDE) {
            outside = 0;
            inside = this.widths.getTop();
        } else {
            throw new AssertionError("Unexpected Stroke Type");
        }

        // This constructor enforces that they are all uniform
        strokeUniform = true;

        // Since insets are empty, don't have to worry about it
        innerEdge = new Insets(inside);
        outerEdge = new Insets(outside);
        this.hash = preComputeHash();
    }

    /**
     * Creates a new BorderStroke.
     *
     * @param stroke    The stroke to use for all sides. If null, we default to Color.BLACK.
     * @param style     The style to use for all sides. If null, we default to BorderStrokeStyle.NONE
     * @param radii     The radii to use. If null, we default to CornerRadii.EMPTY
     * @param widths    The widths to use. If null, we default to DEFAULT_WIDTHS
     * @param insets    The insets indicating where to draw the border relative to the region edges.
     */
    public BorderStroke(@NamedArg("stroke") Paint stroke, @NamedArg("style") BorderStrokeStyle style, @NamedArg("radii") CornerRadii radii, @NamedArg("widths") BorderWidths widths, @NamedArg("insets") Insets insets) {
        this(stroke, stroke, stroke, stroke, style, style, style, style, radii, widths, insets);
    }

    /**
     * Create a new BorderStroke, specifying all construction parameters.
     *
     * @param topStroke       The fill to use on the top. If null, defaults to BLACK.
     * @param rightStroke     The fill to use on the right. If null, defaults to the same value as topStroke
     * @param bottomStroke    The fill to use on the bottom. If null, defaults to the same value as bottomStroke
     * @param leftStroke      The fill to use on the left. If null, defaults to the same value as rightStroke
     * @param topStyle        The style to use on the top. If null, defaults to BorderStrokeStyle.NONE
     * @param rightStyle      The style to use on the right. If null, defaults to the same value as topStyle
     * @param bottomStyle     The style to use on the bottom. If null, defaults to the same value as topStyle
     * @param leftStyle       The style to use on the left. If null, defaults to the same value as rightStyle
     * @param radii           The radii. If null, we default to square corners by using CornerRadii.EMPTY
     * @param widths          The thickness of each side. If null, we default to DEFAULT_WIDTHS.
     * @param insets    The insets indicating where to draw the border relative to the region edges.
     */
    public BorderStroke(
            @NamedArg("topStroke") Paint topStroke, @NamedArg("rightStroke") Paint rightStroke, @NamedArg("bottomStroke") Paint bottomStroke, @NamedArg("leftStroke") Paint leftStroke,
            @NamedArg("topStyle") BorderStrokeStyle topStyle, @NamedArg("rightStyle") BorderStrokeStyle rightStyle,
            @NamedArg("bottomStyle") BorderStrokeStyle bottomStyle, @NamedArg("leftStyle") BorderStrokeStyle leftStyle,
            @NamedArg("radii") CornerRadii radii, @NamedArg("widths") BorderWidths widths, @NamedArg("insets") Insets insets)
    {
        this.topStroke = topStroke == null ? Color.BLACK : topStroke;
        this.rightStroke = rightStroke == null ? this.topStroke : rightStroke;
        this.bottomStroke = bottomStroke == null ? this.topStroke : bottomStroke;
        this.leftStroke = leftStroke == null ? this.rightStroke : leftStroke;
        this.topStyle = topStyle == null ? BorderStrokeStyle.NONE : topStyle;
        this.rightStyle = rightStyle == null ? this.topStyle : rightStyle;
        this.bottomStyle = bottomStyle == null ? this.topStyle : bottomStyle;
        this.leftStyle = leftStyle == null ? this.rightStyle : leftStyle;
        this.radii = radii == null ? CornerRadii.EMPTY : radii;
        this.widths = widths == null ? DEFAULT_WIDTHS : widths;
        this.insets = insets == null ? Insets.EMPTY : insets;


        final boolean colorsSame =
                this.leftStroke.equals(this.topStroke) &&
                this.leftStroke.equals(this.rightStroke) &&
                this.leftStroke.equals(this.bottomStroke);
        final boolean widthsSame =
                this.widths.left == this.widths.top &&
                this.widths.left == this.widths.right &&
                this.widths.left == this.widths.bottom;
        final boolean stylesSame =
                this.leftStyle.equals(this.topStyle) &&
                this.leftStyle.equals(this.rightStyle) &&
                this.leftStyle.equals(this.bottomStyle);

        strokeUniform = colorsSame && widthsSame && stylesSame;

        // TODO these calculations are not accurate if we are stroking a shape. In such cases, we
        // need to account for the mitre limit

        innerEdge = new Insets(
                this.insets.getTop() + computeInside(this.topStyle.getType(), this.widths.getTop()),
                this.insets.getRight() + computeInside(this.rightStyle.getType(), this.widths.getRight()),
                this.insets.getBottom() + computeInside(this.bottomStyle.getType(), this.widths.getBottom()),
                this.insets.getLeft() + computeInside(this.leftStyle.getType(), this.widths.getLeft())
        );
        outerEdge = new Insets(
                Math.max(0, computeOutside(this.topStyle.getType(), this.widths.getTop()) - this.insets.getTop()),
                Math.max(0, computeOutside(this.rightStyle.getType(), this.widths.getRight()) - this.insets.getRight()),
                Math.max(0, computeOutside(this.bottomStyle.getType(), this.widths.getBottom())- this.insets.getBottom()),
                Math.max(0, computeOutside(this.leftStyle.getType(), this.widths.getLeft()) - this.insets.getLeft())
        );
        this.hash = preComputeHash();
    }

    private int preComputeHash() {
        int result;
        result = topStroke.hashCode();
        result = 31 * result + rightStroke.hashCode();
        result = 31 * result + bottomStroke.hashCode();
        result = 31 * result + leftStroke.hashCode();
        result = 31 * result + topStyle.hashCode();
        result = 31 * result + rightStyle.hashCode();
        result = 31 * result + bottomStyle.hashCode();
        result = 31 * result + leftStyle.hashCode();
        result = 31 * result + widths.hashCode();
        result = 31 * result + radii.hashCode();
        result = 31 * result + insets.hashCode();
        return result;
    }

    private double computeInside(StrokeType type, double width) {
        if (type == StrokeType.OUTSIDE) {
            return 0;
        } else if (type == StrokeType.CENTERED) {
            return width / 2.0;
        } else if (type == StrokeType.INSIDE) {
            return width;
        } else {
            throw new AssertionError("Unexpected Stroke Type");
        }
    }

    private double computeOutside(StrokeType type, double width) {
        if (type == StrokeType.OUTSIDE) {
            return width;
        } else if (type == StrokeType.CENTERED) {
            return width / 2.0;
        } else if (type == StrokeType.INSIDE) {
            return 0;
        } else {
            throw new AssertionError("Unexpected Stroke Type");
        }
    }

    /**
     * @inheritDoc
     */
    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BorderStroke that = (BorderStroke) o;
        if (this.hash != that.hash) return false;
        if (!bottomStroke.equals(that.bottomStroke)) return false;
        if (!bottomStyle.equals(that.bottomStyle)) return false;
        if (!leftStroke.equals(that.leftStroke)) return false;
        if (!leftStyle.equals(that.leftStyle)) return false;
        if (!radii.equals(that.radii)) return false;
        if (!rightStroke.equals(that.rightStroke)) return false;
        if (!rightStyle.equals(that.rightStyle)) return false;
        if (!topStroke.equals(that.topStroke)) return false;
        if (!topStyle.equals(that.topStyle)) return false;
        if (!widths.equals(that.widths)) return false;
        if (!insets.equals(that.insets)) return false;

        return true;
    }

    /**
     * @inheritDoc
     */
    @Override public int hashCode() {
        return hash;
    }
}
