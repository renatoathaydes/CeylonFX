import ceylonfx.application {
    CeylonFxAdapter
}
import ceylonfx.geometry {
    Location,
    Dimension
}

import javafx.scene.image {
    Image
}
import javafx.scene.paint {
    JCycleMethod=CycleMethod,
    JStop=Stop,
    JLinearGrad=LinearGradient,
    JRadialGrad=RadialGradient,
    JImagePattern=ImagePattern,
    JPaint=Paint
}

shared alias Stop => [Float, Color];

shared abstract class Paint() 
		extends CeylonFxAdapter<JPaint>() {}

shared abstract class CycleMethod(shared JCycleMethod type)
        of noCycle|reflectCycle|repeatCycle {
    string=>type.string;
}
shared object noCycle extends CycleMethod(JCycleMethod.\iNO_CYCLE) {}
shared object repeatCycle extends CycleMethod(JCycleMethod.\iREPEAT) {}
shared object reflectCycle extends CycleMethod(JCycleMethod.\iREFLECT) {}

{JStop*} jStops({Stop*} stops)
		=> { for (elem in stops) JStop(elem[0], elem[1].createDelegate()) };

shared class LinearGradient(
	Location start = [0.0, 0.0],
	Location end = [1.0, 0.0], 
	Boolean proportional = true,
	CycleMethod cycleMethod = noCycle, 
	{Stop*} stops = 
			{[0.0, white], [1.0, yellow]})
		extends Paint() {
	
	shared actual JPaint createDelegate() => JLinearGrad(
		start[0], start[1],
		end[0], end[1], 
		proportional, 
		cycleMethod.type, 
		*jStops(stops));
	
	
}

shared class RadialGradient(
	Float focusAngle = 0.0, 
	Float focusDistance = 0.0, 
	Location center = [0.0, 0.0], 
	Float radius = 1.0, 
	Boolean proportional = true,
	CycleMethod cycleMethod = noCycle, 
	{Stop*} stops = 
			{[0.0, white], [1.0, yellow]}) 
		extends Paint() {
	
	createDelegate() => JRadialGrad(
		focusAngle, focusDistance,
		center[0], center[1], radius,
		proportional,
		cycleMethod.type, 
		*jStops(stops));
	
}

shared class ImagePattern(
	Image image, 
	Location anchorLocation = [0.0, 0.0],
	Dimension anchorDimension = [1.0, 1.0],
	Boolean proportional = true)
		extends Paint() {
	
	createDelegate() => JImagePattern(
		image,
		anchorLocation[0], anchorLocation[1],
		anchorDimension[0], anchorDimension[1],
		proportional);
	
}