import javafx.scene.effect { BlendMode }
import ceylonfx.binding { Binding }
import ceylonfx.scene.effect { Effect }
import ceylonfx.geometry { Location, Point3D }

"Base class for scene graph nodes."
class Node(
	String id = "",
	String style = "",
	BlendMode? blendMode = null,
	CacheHint cacheHint = defaultCacheHint,
	Node? clip = null,
	Cursor?|Binding<Object, Cursor> cursor = null,
	DepthTest depthTest = inheritDepthTest,
	Boolean disabled = false,
	Effect?|Binding<Object, Effect> effect = null,
	Boolean focusTraversable = false,
	Location location = [0.0, 0.0],
	Boolean managed = true,
	Boolean mouseTransparent = false,
	Boolean pickOnBounds = false,
	Float rotate = 0.0,
	Point3D rotationAxis = Point3D(0.0, 0.0, 0.0),
	[Float, Float, Float] scale = [1.0, 1.0, 1.0],
	[Float, Float, Float] translate = [0.0, 0.0, 0.0],
	Boolean visible = true) {

		//TODO implement methods, including read-only properties not declared in the constructor

}