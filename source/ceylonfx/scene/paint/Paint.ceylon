import javafx.scene.paint { CycleMethod { noCycle = \iNO_CYCLE }, Stop, JLinearGrad = LinearGradient, Color { white = \iWHITE, yellow = \iYELLOW }, JPaint = Paint }
import ceylonfx.application { CeylonFxAdapter }

shared abstract class Paint() satisfies CeylonFxAdapter<JPaint> {}

shared class LinearGradient(Float startX = 0.0, Float startY = 0.0,
	Float endX = 1.0, Float endY = 0.0, Boolean proportional = true,
	CycleMethod cycleMethod = noCycle, {[Float, Color]*} stops = {[0.0, white], [1.0, yellow]})
		extends Paint() {
	
	value actualStops = { for(elem in stops) Stop(elem[0], elem[1]) };
	
	shared actual JLinearGrad delegate = JLinearGrad(startX, startY,
		endX, endY, proportional, cycleMethod, *actualStops);
	
}