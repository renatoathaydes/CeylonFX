import javafx.scene.effect { JBlendMode=BlendMode, JEffect=Effect }
import ceylonfx.application { CeylonFxAdapter }

"The abstract base class for all effect implementations. An effect is a graphical algorithm that produces an image, typically as a modification of a source image."
shared abstract class Effect()
		extends CeylonFxAdapter<JEffect>() {}

"A blending mode defines the manner in which the inputs of a Blend effect are composited together or how a Node is blended into the background of a scene. "
shared abstract class BlendMode(shared JBlendMode delegate)
		of addBlendMode|blueBlendMode|colorBurnBlendMode|colorDodgeBlendMode|
		darkenBlendMode|differenceBlendMode|exclusionBlendMode|greenBlendMode|
		hardLightBlendMode|lightenBlendMode|multiplyBlendMode|overlayBlendMode|
		redBlendMode|screenBlendMode|softLightBlendMode|srcAtopBlendMode|
		srcOverBlendMode {}

"The color and alpha components from the top input are added to those from the bottom input."
shared object addBlendMode extends BlendMode(JBlendMode.\iADD) {}

"The blue component of the bottom input is replaced with the blue component of the top input; the other color components are unaffected."
shared object blueBlendMode extends BlendMode(JBlendMode.\iBLUE) {}

"The inverse of the bottom input color components are divided by the top input color components, all of which is then inverted to produce the resulting color."
shared object colorBurnBlendMode extends BlendMode(JBlendMode.\iCOLOR_BURN) {}

"The bottom input color components are divided by the inverse of the top input color components to produce the resulting color."
shared object colorDodgeBlendMode extends BlendMode(JBlendMode.\iCOLOR_DODGE) {}

"The darker of the color components from the two inputs are selected to produce the resulting color."
shared object darkenBlendMode extends BlendMode(JBlendMode.\iDARKEN) {}

"The darker of the color components from the two inputs are subtracted from the lighter ones to produce the resulting color."
shared object differenceBlendMode extends BlendMode(JBlendMode.\iDIFFERENCE) {}

"The color components from the two inputs are multiplied and doubled, and then subtracted from the sum of the bottom input color components, to produce the resulting color."
shared object exclusionBlendMode extends BlendMode(JBlendMode.\iEXCLUSION) {}

"The green component of the bottom input is replaced with the green component of the top input; the other color components are unaffected."
shared object greenBlendMode extends BlendMode(JBlendMode.\iGREEN) {}

"The input color components are either multiplied or screened, depending on the top input color."
shared object hardLightBlendMode extends BlendMode(JBlendMode.\iHARD_LIGHT) {}

"The lighter of the color components from the two inputs are selected to produce the resulting color."
shared object lightenBlendMode extends BlendMode(JBlendMode.\iLIGHTEN) {}

"The color components from the first input are multiplied with those from the second input."
shared object multiplyBlendMode extends BlendMode(JBlendMode.\iMULTIPLY) {}

"The input color components are either multiplied or screened, depending on the bottom input color."
shared object overlayBlendMode extends BlendMode(JBlendMode.\iOVERLAY) {}

"The red component of the bottom input is replaced with the red component of the top input; the other color components are unaffected."
shared object redBlendMode extends BlendMode(JBlendMode.\iRED) {}

"The color components from both of the inputs are inverted, multiplied with each other, and that result is again inverted to produce the resulting color."
shared object screenBlendMode extends BlendMode(JBlendMode.\iSCREEN) {}

"The input color components are either darkened or lightened, depending on the top input color."
shared object softLightBlendMode extends BlendMode(JBlendMode.\iSOFT_LIGHT) {}

"The part of the top input lying inside of the bottom input is blended with the bottom input."
shared object srcAtopBlendMode extends BlendMode(JBlendMode.\iSRC_ATOP) {}

"The top input is blended over the bottom input."
shared object srcOverBlendMode extends BlendMode(JBlendMode.\iSRC_OVER) {}

