import ceylonfx.application {
	CeylonFxAdapter
}
import ceylonfx.binding {
	Binding,
	ObjectProperty, Property
}
import ceylonfx.geometry {
	Location, Point3D, Bounds, BoundingBox
}
import ceylonfx.scene.effect {
	BlendMode,
	Effect
}
import javafx.scene {
	JNode=Node
}
import javafx.geometry { JBounds=Bounds }
import ceylonfx.binding.internal { bindToJavaFx }
import ceylonfx.geometry.util { boundingBoxJ2C }

"Base class for scene graph nodes."
shared abstract class Node<out Delegate>(
	shared String id = "",
	shared String style = "",
	shared BlendMode? blendMode = null,
	shared CacheHint cacheHint = defaultCacheHint,
	shared Node<JNode>? clip = null,
	shared Cursor?|Binding<Object, Cursor> cursor = null,
	shared DepthTest depthTest = inheritDepthTest,
	shared Effect?|Binding<Object, Effect> effect = null,
	shared Boolean focusTraversable = false,
	shared Location location = [0.0, 0.0],
	shared Boolean managed = true,
	shared Boolean mouseTransparent = false,
	shared Boolean pickOnBounds = false,
	shared Float rotate = 0.0,
	shared Point3D rotationAxis = Point3D(0.0, 0.0, 0.0),
	shared [Float, Float, Float] scale = [1.0, 1.0, 1.0],
	shared [Float, Float, Float] translate = [0.0, 0.0, 0.0],
	shared Boolean visible = true)
		extends CeylonFxAdapter<Delegate>()
		given Delegate satisfies JNode {
	
	ObjectProperty<Cursor?> createCursorProperty() {
		if (exists cursor, is Cursor cursor) {
			return ObjectProperty<Cursor?>(cursor);
		}
		return ObjectProperty<Cursor?>(null);
	}
	
	ObjectProperty<Effect?> createEffectProperty() {
		if (exists effect, is Effect effect) {
			return ObjectProperty<Effect?>(effect);
		}
		return ObjectProperty<Effect?>(null);
	}
	
	shared ObjectProperty<Cursor?> cursorProperty = createCursorProperty();
	
	shared ObjectProperty<Effect?> effectProperty = createEffectProperty();
	
	if (is Binding<Object, Cursor> cursor) {
		cursor.bind(cursorProperty);
	}
	
	if (is Binding<Object, Effect> effect) {
		effect.bind(effectProperty);
	}
	
	object boundsInLocalProperty extends CeylonFxAdapter<Property<Bounds<JBounds>>>() {
		shared actual Property<Bounds<JBounds>> createDelegate() {
			value property = ObjectProperty<Bounds<JBounds>>(BoundingBox([0.0, 0.0], [0.0, 0.0]));
			bindToJavaFx(outer.delegate.boundsInLocalProperty(), property, boundingBoxJ2C);
			return property;
		}
	}
	
	shared Property<Bounds<JBounds>> boundsInLocal => boundsInLocalProperty.delegate;
	
	//TODO implement methods, including read-only properties not declared in the constructor
	
	
}
