import javafx.geometry { JBounds=Bounds }
import ceylonfx.geometry { BoundingBox, Bounds, Point3D }
import ceylonfx.application.java { TypeConverter }

shared object boundingBoxJ2C satisfies TypeConverter<JBounds, Bounds<JBounds>> {

	shared actual BoundingBox convert(JBounds? from) {
		if (exists from) {
			return BoundingBox(
				Point3D(from.minX, from.minY, from.minZ),
				[from.width, from.height, from.depth]);
		} else {
			return BoundingBox([0.0, 0.0], [0.0, 0.0]);
		}
	}
	
}
