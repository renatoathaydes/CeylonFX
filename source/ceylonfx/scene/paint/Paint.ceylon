import ceylonfx.application {
	CeylonFxAdapter
}

import javafx.scene.paint {
	CycleMethod {
		noCycle=NO_CYCLE
	},
	Stop,
	JLinearGrad=LinearGradient,
	JRadialGrad=RadialGradient,
	JImagePattern=ImagePattern,
	Color {
		white=WHITE,
		yellow=YELLOW
	},
	JPaint=Paint
}
import javafx.scene.image { Image }

shared abstract class Paint() 
		extends CeylonFxAdapter<JPaint>() {}

{Stop*} jStops({[Float, Color]*} stops)
		=> { for (elem in stops) Stop(elem[0], elem[1]) };

shared class LinearGradient(
	[Float,Float] start = [0.0, 0.0],
	[Float,Float] end = [1.0, 0.0], 
	Boolean proportional = true,
	CycleMethod cycleMethod = noCycle, 
	{[Float, Color]*} stops = 
			{[0.0, white], [1.0, yellow]})
		extends Paint() {
	
	shared actual JPaint createDelegate() => JLinearGrad(
		start[0], start[1],
		end[0], end[1], 
		proportional, cycleMethod, 
		*jStops(stops));
	
	
}

shared class RadialGradient(
	Float focusAngle = 0.0, 
	Float focusDistance = 0.0, 
	[Float,Float] center = [0.0, 0.0], 
	Float radius = 1.0, 
	Boolean proportional = true,
	CycleMethod cycleMethod = noCycle, 
	{[Float, Color]*} stops = 
			{[0.0, white], [1.0, yellow]}) 
		extends Paint() {
	
	createDelegate() => JRadialGrad(
		focusAngle, focusDistance,
		center[0], center[1], radius, 
		proportional, cycleMethod, 
		*jStops(stops));
	
}

shared class ImagePattern(
	Image image, 
	[Float,Float,Float,Float] anchorRectangle = 
			[0.0, 0.0, 1.0, 1.0],
	Boolean proportional = true)
		extends Paint() {
	
	createDelegate() => JImagePattern(
		image, 
		anchorRectangle[0], anchorRectangle[1], 
		anchorRectangle[2], anchorRectangle[3], 
		proportional);
	
}