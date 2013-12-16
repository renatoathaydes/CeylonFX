import ceylonfx.application {
	CeylonFxAdapter
}

import javafx.geometry {
	JInsets=Insets,
	JBounds=Bounds,
	JBBox=BoundingBox
}

"A set of inside offsets for the 4 side of a rectangular area"
shared class Insets(
	shared Float top = 0.0,
	shared Float right = 0.0,
	shared Float bottom = 0.0,
	shared Float left = 0.0) {
	
	shared JInsets delegate => JInsets(top, right, bottom, left);
	
}

"The base class for objects that are used to describe the bounds of a node or other scene graph object."
shared abstract class Bounds<out Delegate>(
	shared [Float, Float, Float] minimumCoordinates = [0.0, 0.0, 0.0],
	shared [Float, Float, Float] dimension3d = [0.0, 0.0, 0.0])
		extends CeylonFxAdapter<Delegate>()
		given Delegate satisfies Object {
	
	"Tests if the interior of this Bounds entirely contains the specified Bounds,
	 or if the coordinates [x, y] or [[Point3D]] is inside this Bounds."
	shared formal Boolean contains(Bounds<JBounds>|[Float, Float]|Point3D bounds);
	
	"Tests if the interior of this Bounds intersects the interior of the specified Bounds."
	shared formal Boolean intersects(Bounds<JBounds> bounds);
	
	"Indicates whether any of the dimensions(width, height or depth) of this bounds is less than zero."
	shared default Boolean empty => any({ for (coordinate in dimension3d) coordinate < 0.0 });
	
}

"A rectangular bounding box which is used to describe the bounds of a node or other scene graph object."
shared class BoundingBox(Location|Point3D location, Dimension|[Float, Float, Float] dimension)
		extends Bounds<JBBox>() {
	
	shared actual Boolean contains(Bounds<JBounds>|[Float, Float]|Point3D bounds) {
		switch(bounds)
		case (is Bounds<JBounds>) {
			return delegate.contains(bounds.delegate);
		}
		case (is [Float, Float]) {
			return delegate.contains(bounds[0], bounds[1]);
		}
		case (is Point3D) {
			return delegate.contains(bounds.x, bounds.y, bounds.z);
		}
	}
	
	shared actual Boolean intersects(Bounds<JBounds> bounds) {
		return delegate.intersects(bounds.delegate);
	}
	
	shared actual JBBox createDelegate() {
		switch(location)
		case (is Location) {
			if (exists depth = dimension[2]) {
				return JBBox(location[0], location[1], 0.0, dimension[0], dimension[1], depth);
			} else {
				return JBBox(location[0], location[1], dimension[0], dimension[1]);
			}
		}
		case (is Point3D) {
			if (exists depth = dimension[2]) {
				return JBBox(location.x, location.y, location.z, dimension[0], dimension[1], depth);
			} else {
				return JBBox(location.x, location.y, location.z, dimension[0], dimension[1], 0.0);
			}
		}
	}
	
}
