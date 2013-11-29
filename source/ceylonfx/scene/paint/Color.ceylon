import javafx.scene.paint {
	JColor=Color { ... }
}

shared abstract class Color() extends Paint() {
	
	shared actual formal JColor delegate;
	
}

shared Color grayColor(Float gray, Float opacity = 1.0) =>
		colorFor(JColor.gray(gray, opacity));

shared Color rgb(Integer red, Integer green, Integer blue) =>
		colorFor(JColor.rgb(red, green, blue));

shared Color web(String colorString, Float opacity = 1.0) =>
		colorFor(JColor.web(colorString, opacity));

Color colorFor(JColor jColor) {
	object result extends Color() { delegate => jColor; }
	return result;
}

/**
 * A fully transparent color with an ARGB value of #00000000.
 */
shared object transparent extends Color() {
	delegate => \iTRANSPARENT;
}

/**
 * The color alice blue with an RGB value of #F0F8FF.
 */
shared object aliceBlue extends Color() {
	delegate => \iALICEBLUE;
}

/**
 * The color antique white with an RGB value of #FAEBD7.
 */
shared object antiqueWhite extends Color() {
	delegate => \iANTIQUEWHITE;
}

/**
 * The color aqua with an RGB value of #00FFFF.
 */
shared object aqua extends Color() {
	delegate => \iAQUA;
}

/**
 * The color aquamarine with an RGB value of #7FFFD4.
 */
shared object aquaMarine extends Color() {
	delegate => \iAQUAMARINE;
}

/**
 * The color azure with an RGB value of #F0FFFF.
 */
shared object azure extends Color() {
	delegate => \iAZURE;
}

/**
 * The color beige with an RGB value of #F5F5DC.
 */
shared object beige extends Color() {
	delegate => \iBEIGE;
}

/**
 * The color bisque with an RGB value of #FFE4C4.
 */
shared object bisque extends Color() {
	delegate => \iBISQUE;
}

/**
 * The color black with an RGB value of #000000.
 */
shared object black extends Color() {
	delegate => \iBLACK;
}

/**
 * The color blanched almond with an RGB value of #FFEBCD.
 */
shared object blanchedAlmond extends Color() {
	delegate => \iBLANCHEDALMOND;
}

/**
 * The color blue with an RGB value of #0000FF.
 */
shared object blue extends Color() {
	delegate => \iBLUE;
}

/**
 * The color blue violet with an RGB value of #8A2BE2.
 */
shared object blueViolet extends Color() {
	delegate => \iBLUEVIOLET;
}

/**
 * The color brown with an RGB value of #A52A2A.
 */
shared object brown extends Color() {
	delegate => \iBROWN;
}

/**
 * The color burly wood with an RGB value of #DEB887.
 */
shared object burlyWood extends Color() {
	delegate => \iBURLYWOOD;
}

/**
 * The color cadet blue with an RGB value of #5F9EA0.
 */
shared object cadetBlue extends Color() {
	delegate => \iCADETBLUE;
}

/**
 * The color chartreuse with an RGB value of #7FFF00.
 */
shared object chartReuse extends Color() {
	delegate => \iCHARTREUSE;
}

/**
 * The color chocolate with an RGB value of #D2691E.
 */
shared object chocolate extends Color() {
	delegate => \iCHOCOLATE;
}

/**
 * The color coral with an RGB value of #FF7F50.
 */
shared object coral extends Color() {
	delegate => \iCORAL;
}

/**
 * The color cornflower blue with an RGB value of #6495ED.
 */
shared object cornFlowerBlue extends Color() {
	delegate => \iCORNFLOWERBLUE;
}

/**
 * The color cornsilk with an RGB value of #FFF8DC.
 */
shared object cornSilk extends Color() {
	delegate => \iCORNSILK;
}

/**
 * The color crimson with an RGB value of #DC143C.
 */
shared object crimson extends Color() {
	delegate => \iCRIMSON;
}

/**
 * The color cyan with an RGB value of #00FFFF.
 */
shared object cyan extends Color() {
	delegate => \iCYAN;
}

/**
 * The color dark blue with an RGB value of #00008B.
 */
shared object darkBlue extends Color() {
	delegate => \iDARKBLUE;
}

/**
 * The color dark cyan with an RGB value of #008B8B.
 */
shared object darkCyan extends Color() {
	delegate => \iDARKCYAN;
}

/**
 * The color dark goldenrod with an RGB value of #B8860B.
 */
shared object darkGoldenrod extends Color() {
	delegate => \iDARKGOLDENROD;
}

/**
 * The color dark gray with an RGB value of #A9A9A9.
 */
shared object darkGray extends Color() {
	delegate => \iDARKGRAY;
}

/**
 * The color dark green with an RGB value of #006400.
 */
shared object darkGreen extends Color() {
	delegate => \iDARKGREEN;
}

/**
 * The color dark grey with an RGB value of #A9A9A9.
 */
shared Color darkGrey = darkGray;

/**
 * The color dark khaki with an RGB value of #BDB76B.
 */
shared object darkKhaki extends Color() {
	delegate => \iDARKKHAKI;
}

/**
 * The color dark magenta with an RGB value of #8B008B.
 */
shared object darkMagenta extends Color() {
	delegate => \iDARKMAGENTA;
}

/**
 * The color dark olive green with an RGB value of #556B2F.
 */
shared object darkOliveGreen extends Color() {
	delegate => \iDARKOLIVEGREEN;
}

/**
 * The color dark orange with an RGB value of #FF8C00.
 */
shared object darkOrange extends Color() {
	delegate => \iDARKORANGE;
}

/**
 * The color dark orchid with an RGB value of #9932CC.
 */
shared object darkOrchid extends Color() {
	delegate => \iDARKORCHID;
}

/**
 * The color dark red with an RGB value of #8B0000.
 */
shared object darkRed extends Color() {
	delegate => \iDARKRED;
}

/**
 * The color dark salmon with an RGB value of #E9967A.
 */
shared object darkSalmon extends Color() {
	delegate => \iDARKSALMON;
}

/**
 * The color dark sea green with an RGB value of #8FBC8F.
 */
shared object darkSeaGreen extends Color() {
	delegate => \iDARKSEAGREEN;
}

/**
 * The color dark slate blue with an RGB value of #483D8B.
 */
shared object darkSlateBlue extends Color() {
	delegate => \iDARKSLATEBLUE;
}

/**
 * The color dark slate gray with an RGB value of #2F4F4F.
 */
shared object darkSlateGray extends Color() {
	delegate => \iDARKSLATEGRAY;
}

/**
 * The color dark slate grey with an RGB value of #2F4F4F.
 */
shared Color darkSlateGrey = darkSlateGray;

/**
 * The color dark turquoise with an RGB value of #00CED1.
 */
shared object darkTurquoise extends Color() {
	delegate => \iDARKTURQUOISE;
}

/**
 * The color dark violet with an RGB value of #9400D3.
 */
shared object darkViolet extends Color() {
	delegate => \iDARKVIOLET;
}

/**
 * The color deep pink with an RGB value of #FF1493.
 */
shared object deepPink extends Color() {
	delegate => \iDEEPPINK;
}

/**
 * The color deep sky blue with an RGB value of #00BFFF.
 */
shared object deepSkyBlue extends Color() {
	delegate => \iDEEPSKYBLUE;
}

/**
 * The color dim gray with an RGB value of #696969.
 */
shared object dimGray extends Color() {
	delegate => \iDIMGRAY;
}

/**
 * The color dim grey with an RGB value of #696969.
 */
shared Color dimGrey = dimGray;

/**
 * The color dodger blue with an RGB value of #1E90FF.
 */
shared object dodgerBlue extends Color() {
	delegate => \iDODGERBLUE;
}

/**
 * The color firebrick with an RGB value of #B22222.
 */
shared object fireBrick extends Color() {
	delegate => \iFIREBRICK;
}

/**
 * The color floral white with an RGB value of #FFFAF0.
 */
shared object floralWhite extends Color() {
	delegate => \iFLORALWHITE;
}

/**
 * The color forest green with an RGB value of #228B22.
 */
shared object forestGreen extends Color() {
	delegate => \iFORESTGREEN;
}

/**
 * The color fuchsia with an RGB value of #FF00FF.
 */
shared object fuchsia extends Color() {
	delegate => \iFUCHSIA;
}

/**
 * The color gainsboro with an RGB value of #DCDCDC.
 */
shared object gainsboro extends Color() {
	delegate => \iGAINSBORO;
}

/**
 * The color ghost white with an RGB value of #F8F8FF.
 */
shared object ghostWhite extends Color() {
	delegate => \iGHOSTWHITE;
}

/**
 * The color gold with an RGB value of #FFD700.
 */
shared object gold extends Color() {
	delegate => \iGOLD;
}

/**
 * The color goldenrod with an RGB value of #DAA520.
 */
shared object goldenRod extends Color() {
	delegate => \iGOLDENROD;
}

/**
 * The color gray with an RGB value of #808080.
 */
shared object gray extends Color() {
	delegate => \iGRAY;
}

/**
 * The color green with an RGB value of #008000.
 */
shared object green extends Color() {
	delegate => \iGREEN;
}

/**
 * The color green yellow with an RGB value of #ADFF2F.
 */
shared object greenYellow extends Color() {
	delegate => \iGREENYELLOW;
}

/**
 * The color grey with an RGB value of #808080.
 */
shared Color grey = gray;

/**
 * The color honeydew with an RGB value of #F0FFF0.
 */
shared object honeyDew extends Color() {
	delegate => \iHONEYDEW;
}

/**
 * The color hot pink with an RGB value of #FF69B4.
 */
shared object hotPink extends Color() {
	delegate => \iHOTPINK;
}

/**
 * The color indian red with an RGB value of #CD5C5C.
 */
shared object indianRed extends Color() {
	delegate => \iINDIANRED;
}

/**
 * The color indigo with an RGB value of #4B0082.
 */
shared object indigo extends Color() {
	delegate => \iINDIGO;
}

/**
 * The color ivory with an RGB value of #FFFFF0.
 */
shared object ivory extends Color() {
	delegate => \iIVORY;
}

/**
 * The color khaki with an RGB value of #F0E68C.
 */
shared object khaki extends Color() {
	delegate => \iKHAKI;
}

/**
 * The color lavender with an RGB value of #E6E6FA.
 */
shared object lavender extends Color() {
	delegate => \iLAVENDER;
}

/**
 * The color lavender blush with an RGB value of #FFF0F5.
 */
shared object lavenderBlush extends Color() {
	delegate => \iLAVENDERBLUSH;
}

/**
 * The color lawn green with an RGB value of #7CFC00.
 */
shared object lawnGreen extends Color() {
	delegate => \iLAWNGREEN;
}

/**
 * The color lemon chiffon with an RGB value of #FFFACD.
 */
shared object lemonChiffon extends Color() {
	delegate => \iLEMONCHIFFON;
}

/**
 * The color light blue with an RGB value of #ADD8E6.
 */
shared object lightBlue extends Color() {
	delegate => \iLIGHTBLUE;
}

/**
 * The color light coral with an RGB value of #F08080.
 */
shared object lightCoral extends Color() {
	delegate => \iLIGHTCORAL;
}

/**
 * The color light cyan with an RGB value of #E0FFFF.
 */
shared object lightCyan extends Color() {
	delegate => \iLIGHTCYAN;
}

/**
 * The color light goldenrod yellow with an RGB value of #FAFAD2.
 */
shared object lightGoldenRod extends Color() {
	delegate => \iLIGHTGOLDENRODYELLOW;
}

/**
 * The color light gray with an RGB value of #D3D3D3.
 */
shared object lightGray extends Color() {
	delegate => \iLIGHTGRAY;
}

/**
 * The color light green with an RGB value of #90EE90.
 */
shared object lightGreen extends Color() {
	delegate => \iLIGHTGREEN;
}

/**
 * The color light grey with an RGB value of #D3D3D3.
 */
shared Color lightGrey = lightGray;

/**
 * The color light pink with an RGB value of #FFB6C1.
 */
shared object lightPink extends Color() {
	delegate => \iLIGHTPINK;
}

/**
 * The color light salmon with an RGB value of #FFA07A.
 */
shared object lightSalmon extends Color() {
	delegate => \iLIGHTSALMON;
}

/**
 * The color light sea green with an RGB value of #20B2AA.
 */
shared object lightSeaGreen extends Color() {
	delegate => \iLIGHTSEAGREEN;
}

/**
 * The color light sky blue with an RGB value of #87CEFA.
 */
shared object lightSkyBlue extends Color() {
	delegate => \iLIGHTSKYBLUE;
}

/**
 * The color light slate gray with an RGB value of #778899.
 */
shared object lightSlateGray extends Color() {
	delegate => \iLIGHTSLATEGRAY;
}

/**
 * The color light slate grey with an RGB value of #778899.
 */
shared Color lightSlateGrey = lightSlateGray;

/**
 * The color light steel blue with an RGB value of #B0C4DE.
 */
shared object lightSteelBlue extends Color() {
	delegate => \iLIGHTSTEELBLUE;
}

/**
 * The color light yellow with an RGB value of #FFFFE0.
 */
shared object lightYellow extends Color() {
	delegate => \iLIGHTYELLOW;
}

/**
 * The color lime with an RGB value of #00FF00.
 */
shared object lime extends Color() {
	delegate => \iLIME;
}

/**
 * The color lime green with an RGB value of #32CD32.
 */
shared object limeGreen extends Color() {
	delegate => \iLIMEGREEN;
}

/**
 * The color linen with an RGB value of #FAF0E6.
 */
shared object linen extends Color() {
	delegate => \iLINEN;
}

/**
 * The color magenta with an RGB value of #FF00FF.
 */
shared object magenta extends Color() {
	delegate => \iMAGENTA;
}

/**
 * The color maroon with an RGB value of #800000.
 */
shared object maroon extends Color() {
	delegate => \iMAROON;
}

/**
 * The color medium aquamarine with an RGB value of #66CDAA.
 */
shared object mediumAquamarine extends Color() {
	delegate => \iMEDIUMAQUAMARINE;
}

/**
 * The color medium blue with an RGB value of #0000CD.
 */
shared object mediumBlue extends Color() {
	delegate => \iMEDIUMBLUE; 
}

/**
 * The color medium orchid with an RGB value of #BA55D3.
 */
shared object mediumOrchid extends Color() { 
	delegate => \iMEDIUMORCHID; 
}

/**
 * The color medium purple with an RGB value of #9370DB.
 */
shared object mediumPurple extends Color() {
	delegate => \iMEDIUMPURPLE; 
}

/**
 * The color medium sea green with an RGB value of #3CB371.
 */
shared object mediumSeaGreen extends Color() { 
	delegate => \iMEDIUMSEAGREEN; 
}

/**
 * The color medium slate blue with an RGB value of #7B68EE.
 */
shared object mediumSlateBlue extends Color() {
	delegate => \iMEDIUMSLATEBLUE; 
}

/**
 * The color medium spring green with an RGB value of #00FA9A.
 */
shared object mediumSpringGreen extends Color() { 
	delegate => \iMEDIUMSPRINGGREEN; 
}

/**
 * The color medium turquoise with an RGB value of #48D1CC.
 */
shared object mediumTurquoise extends Color() {
	delegate => \iMEDIUMTURQUOISE; 
}

/**
 * The color medium violet red with an RGB value of #C71585.
 */
shared object mediumVioletRed extends Color() {
	delegate => \iMEDIUMVIOLETRED; 
}

/**
 * The color midnight blue with an RGB value of #191970.
 */
shared object midnightBlue extends Color() { 
	delegate => \iMIDNIGHTBLUE; 
}

/**
 * The color mint cream with an RGB value of #F5FFFA.
 */
shared object mintCream extends Color() { 
	delegate => \iMINTCREAM; 
}

/**
 * The color misty rose with an RGB value of #FFE4E1.
 */
shared object mistyRose extends Color() { 
	delegate => \iMISTYROSE; 
}

/**
 * The color moccasin with an RGB value of #FFE4B5.
 */
shared object moccasin extends Color() { 
	delegate => \iMOCCASIN; 
}

/**
 * The color navajo white with an RGB value of #FFDEAD.
 */
shared object navajoWhite extends Color() { 
	delegate => \iNAVAJOWHITE; 
}

/**
 * The color navy with an RGB value of #000080.
 */
shared object navy extends Color() { 
	delegate => \iNAVY; 
}

/**
 * The color old lace with an RGB value of #FDF5E6.
 */
shared object oldLace extends Color() { 
	delegate => \iOLDLACE; 
}

/**
 * The color olive with an RGB value of #808000.
 */
shared object olive extends Color() { 
	delegate => \iOLIVE; 
}

/**
 * The color olive drab with an RGB value of #6B8E23.
 */
shared object oliveDrab extends Color() { 
	delegate => \iOLIVEDRAB; 
}

/**
 * The color orange with an RGB value of #FFA500.
 */
shared object orange extends Color() { 
	delegate => \iORANGE; 
}

/**
 * The color orange red with an RGB value of #FF4500.
 */
shared object orangeRed extends Color() { 
	delegate => \iORANGERED; 
}

/**
 * The color orchid with an RGB value of #DA70D6.
 */
shared object orchid extends Color() { 
	delegate => \iORCHID; 
}

/**
 * The color pale goldenrod with an RGB value of #EEE8AA.
 */
shared object paleGoldenRod extends Color() { 
	delegate => \iPALEGOLDENROD; 
}

/**
 * The color pale green with an RGB value of #98FB98.
 */
shared object paleGreen extends Color() { 
	delegate => \iPALEGREEN; 
}

/**
 * The color pale turquoise with an RGB value of #AFEEEE.
 */
shared object paleTurquoise extends Color() { 
	delegate => \iPALETURQUOISE; 
}

/**
 * The color pale violet red with an RGB value of #DB7093.
 */
shared object paleVioletRed extends Color() { 
	delegate => \iPALEVIOLETRED; 
}

/**
 * The color papaya whip with an RGB value of #FFEFD5.
 */
shared object papayaWhip extends Color() { 
	delegate => \iPAPAYAWHIP; 
}

/**
 * The color peach puff with an RGB value of #FFDAB9.
 */
shared object peachPuff extends Color() { 
	delegate => \iPEACHPUFF; 
}

/**
 * The color peru with an RGB value of #CD853F.
 */
shared object peru extends Color() { 
	delegate => \iPERU; 
}

/**
 * The color pink with an RGB value of #FFC0CB.
 */
shared object pink extends Color() { 
	delegate => \iPINK; 
}

/**
 * The color plum with an RGB value of #DDA0DD.
 */
shared object plum extends Color() { 
	delegate => \iPLUM; 
}

/**
 * The color powder blue with an RGB value of #B0E0E6.
 */
shared object powderBlue extends Color() { 
	delegate => \iPOWDERBLUE; 
}

/**
 * The color purple with an RGB value of #800080.
 */
shared object purple extends Color() { 
	delegate => \iPURPLE; 
}

/**
 * The color red with an RGB value of #FF0000.
 */
shared object red extends Color() { 
	delegate => \iRED; 
}

/**
 * The color rosy brown with an RGB value of #BC8F8F.
 */
shared object rosyBrown extends Color() { 
	delegate => \iROSYBROWN; 
}

/**
 * The color royal blue with an RGB value of #4169E1.
 */
shared object royalBlue extends Color() { 
	delegate => \iROYALBLUE; 
}

/**
 * The color saddle brown with an RGB value of #8B4513.
 */
shared object saddleBrown extends Color() { 
	delegate => \iSADDLEBROWN; 
}

/**
 * The color salmon with an RGB value of #FA8072.
 */
shared object salmon extends Color() { 
	delegate => \iSALMON; 
}

/**
 * The color sandy brown with an RGB value of #F4A460.
 */
shared object sandyBrown extends Color() { 
	delegate => \iSANDYBROWN; 
}

/**
 * The color sea green with an RGB value of #2E8B57.
 */
shared object seaGreen extends Color() { 
	delegate => \iSEAGREEN; 
}

/**
 * The color sea shell with an RGB value of #FFF5EE.
 */
shared object seaShell extends Color() { 
	delegate => \iSEASHELL; 
}

/**
 * The color sienna with an RGB value of #A0522D.
 */
shared object sienna extends Color() { 
	delegate => \iSIENNA; 
}

/**
 * The color silver with an RGB value of #C0C0C0.
 */
shared object silver extends Color() { 
	delegate => \iSILVER; 
}

/**
 * The color sky blue with an RGB value of #87CEEB.
 */
shared object skyBlue extends Color() { 
	delegate => \iSKYBLUE; 
}

/**
 * The color slate blue with an RGB value of #6A5ACD.
 */
shared object slateBlue extends Color() { 
	delegate => \iSLATEBLUE; 
}

/**
 * The color slate gray with an RGB value of #708090.
 */
shared object slateGray extends Color() { 
	delegate => \iSLATEGRAY; 
}

/**
 * The color slate grey with an RGB value of #708090.
 */
shared Color slateGrey = slateGray;

/**
 * The color snow with an RGB value of #FFFAFA.
 */
shared object snow extends Color() { 
	delegate => \iSNOW;
}

/**
 * The color spring green with an RGB value of #00FF7F.
 */
shared object springGreen extends Color() { 
	delegate => \iSPRINGGREEN; 
}

/**
 * The color steel blue with an RGB value of #4682B4.
 */
shared object steelBlue extends Color() { 
	delegate => \iSTEELBLUE; 
}

/**
 * The color tan with an RGB value of #D2B48C.
 */
shared object tan extends Color() { 
	delegate => \iTAN; 
}

/**
 * The color teal with an RGB value of #008080.
 */
shared object teal extends Color() { 
	delegate => \iTEAL; 
}

/**
 * The color thistle with an RGB value of #D8BFD8.
 */
shared object thistle extends Color() { 
	delegate => \iTHISTLE; 
}

/**
 * The color tomato with an RGB value of #FF6347.
 */
shared object tomato extends Color() { 
	delegate => \iTOMATO; 
}

/**
 * The color turquoise with an RGB value of #40E0D0.
 */
shared object turquoise extends Color() { 
	delegate => \iTURQUOISE; 
}

/**
 * The color violet with an RGB value of #EE82EE.
 */
shared object violet extends Color() { 
	delegate => \iVIOLET; 
}

/**
 * The color wheat with an RGB value of #F5DEB3.
 */
shared object wheat extends Color() { 
	delegate => \iWHEAT; 
}

/**
 * The color white with an RGB value of #FFFFFF.
 */
shared object white extends Color() { 
	delegate => \iWHITE; 
}

/**
 * The color white smoke with an RGB value of #F5F5F5.
 */
shared object whiteSmoke extends Color() { 
	delegate => \iWHITESMOKE; 
}

/**
 * The color yellow with an RGB value of #FFFF00.
 */
shared object yellow extends Color() { 
	delegate => \iYELLOW; 
}

/**
 * The color yellow green with an RGB value of #9ACD32.
 */
shared object yellowGreen extends Color() { 
	delegate => \iYELLOWGREEN; 
}
