import ceylonfx.application {
	CeylonFxAdapter
}

import javafx.geometry {
	JPoint3D=Point3D
}

"A 3D geometric point that represents the x, y, z coordinates."
shared class Point3D(
	shared Float x = 0.0,
	shared Float y = 0.0,
	shared Float z = 0.0) extends CeylonFxAdapter<JPoint3D>() {

	shared actual JPoint3D createDelegate() => JPoint3D(x, y, z);

	"Computes the distance between this point and the given point."
	shared Float distance(Point3D point) {
		return delegate.distance(point.x, point.y, point.z);
	}
	
	shared actual Boolean equals(Object other) {
		if (is Point3D other) {
			return this.delegate == other.delegate;	
		} else {
			return false;
		}
	}
	
	shared actual Integer hash => delegate.hash;

}
