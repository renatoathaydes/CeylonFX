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
    Color {
        white=WHITE,
        yellow=YELLOW
    },
    JPaint=Paint
}

shared abstract class Paint() 
        satisfies CeylonFxAdapter<JPaint> {}

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
	
	delegate => JLinearGrad(
		start[0], start[1],
		end[0], end[1], 
		proportional, cycleMethod, 
		*jStops(stops));
	
}

shared class RadialGradient(
    Float focusAngle = 0.0, 
    Float focusDistance = 0.0, 
    [Float,Float] center = [0.0,0.0], 
    Float radius = 1.0, 
    Boolean proportional = true,
    CycleMethod cycleMethod = noCycle, 
	{[Float, Color]*} stops = 
			{[0.0, white], [1.0, yellow]}) 
        extends Paint() {
    
    delegate => JRadialGrad(
        focusAngle, focusDistance,
        center[0], center[1], radius, 
        proportional, cycleMethod, 
        *jStops(stops));
    
}