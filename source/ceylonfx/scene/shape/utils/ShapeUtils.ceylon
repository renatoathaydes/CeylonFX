import ceylonfx.scene.shape { Shape }
import javafx.scene.shape { JShape=Shape }
import ceylonfx.scene.utils { initNode }

"Transfers Node and Shape properties from the Ceylon Shape to the Java Shape.
 Notice that this method calls [[initNode]] so you don't need to call that also."
shared void transferProperties(Shape<JShape> shape, JShape jshape) {
	initNode(shape, jshape);
	jshape.fill = shape.fill.delegate;
	jshape.smooth = shape.smooth;
	jshape.strokeDashOffset = shape.strokeDashOffset;
	jshape.strokeLineCap = shape.strokeLineCap.delegate;
	jshape.strokeLineJoin = shape.strokeLineJoin.delegate;
	jshape.strokeMiterLimit = shape.strokeMiterLimit;
	if (exists stroke = shape.stroke) { jshape.stroke = stroke.delegate; }
	jshape.strokeType = shape.strokeType.delegate;
	jshape.strokeWidth = shape.strokeWidth;
}
