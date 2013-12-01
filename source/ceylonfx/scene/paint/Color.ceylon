import javafx.scene.paint {
	JColor=Color { ... }
}

shared abstract class Color() 
        extends Paint() {
	shared actual formal JColor createDelegate();
}

shared Color grayShade(Float gray, Float opacity = 1.0) 
        => colorFor(JColor.gray(gray, opacity));

shared Color rgb(Integer red, Integer green, Integer blue, Float opacity = 1.0) 
        => colorFor(JColor.rgb(red, green, blue, opacity));

shared Color web(String colorString, Float opacity = 1.0) 
        => colorFor(JColor.web(colorString, opacity));

Color colorFor(JColor jColor) {
	object result extends Color() {
		createDelegate() => jColor;
	}
	return result;
}

"A fully transparent color with an ARGB value of #00000000."
shared object transparent extends Color() {
	createDelegate() => \iTRANSPARENT;
}

"The color alice blue with an RGB value of #F0F8FF."
shared object aliceBlue extends Color() {
	createDelegate() => \iALICEBLUE;
}

"The color antique white with an RGB value of #FAEBD7."
shared object antiqueWhite extends Color() {
	createDelegate() => \iANTIQUEWHITE;
}

"The color aqua with an RGB value of #00FFFF."
shared object aqua extends Color() {
	createDelegate() => \iAQUA;
}

"The color aquamarine with an RGB value of #7FFFD4."
shared object aquaMarine extends Color() {
	createDelegate() => \iAQUAMARINE;
}

"The color azure with an RGB value of #F0FFFF."
shared object azure extends Color() {
	createDelegate() => \iAZURE;
}

"The color beige with an RGB value of #F5F5DC."
shared object beige extends Color() {
	createDelegate() => \iBEIGE;
}

"The color bisque with an RGB value of #FFE4C4."
shared object bisque extends Color() {
	createDelegate() => \iBISQUE;
}

"The color black with an RGB value of #000000."
shared object black extends Color() {
	createDelegate() => \iBLACK;
}

"The color blanched almond with an RGB value of #FFEBCD."
shared object blanchedAlmond extends Color() {
	createDelegate() => \iBLANCHEDALMOND;
}

"The color blue with an RGB value of #0000FF."
shared object blue extends Color() {
	createDelegate() => \iBLUE;
}

"The color blue violet with an RGB value of #8A2BE2."
shared object blueViolet extends Color() {
	createDelegate() => \iBLUEVIOLET;
}

"The color brown with an RGB value of #A52A2A."
shared object brown extends Color() {
	createDelegate() => \iBROWN;
}

"The color burly wood with an RGB value of #DEB887."
shared object burlyWood extends Color() {
	createDelegate() => \iBURLYWOOD;
}

"The color cadet blue with an RGB value of #5F9EA0."
shared object cadetBlue extends Color() {
	createDelegate() => \iCADETBLUE;
}

"The color chartreuse with an RGB value of #7FFF00."
shared object chartReuse extends Color() {
	createDelegate() => \iCHARTREUSE;
}

"The color chocolate with an RGB value of #D2691E."
shared object chocolate extends Color() {
	createDelegate() => \iCHOCOLATE;
}

"The color coral with an RGB value of #FF7F50."
shared object coral extends Color() {
	createDelegate() => \iCORAL;
}

"The color cornflower blue with an RGB value of #6495ED."
shared object cornFlowerBlue extends Color() {
	createDelegate() => \iCORNFLOWERBLUE;
}

"The color cornsilk with an RGB value of #FFF8DC."
shared object cornSilk extends Color() {
	createDelegate() => \iCORNSILK;
}

"The color crimson with an RGB value of #DC143C."
shared object crimson extends Color() {
	createDelegate() => \iCRIMSON;
}

"The color cyan with an RGB value of #00FFFF."
shared object cyan extends Color() {
	createDelegate() => \iCYAN;
}

"The color dark blue with an RGB value of #00008B."
shared object darkBlue extends Color() {
	createDelegate() => \iDARKBLUE;
}

"The color dark cyan with an RGB value of #008B8B."
shared object darkCyan extends Color() {
	createDelegate() => \iDARKCYAN;
}

"The color dark goldenrod with an RGB value of #B8860B."
shared object darkGoldenrod extends Color() {
	createDelegate() => \iDARKGOLDENROD;
}

"The color dark gray with an RGB value of #A9A9A9."
shared object darkGray extends Color() {
	createDelegate() => \iDARKGRAY;
}

"The color dark green with an RGB value of #006400."
shared object darkGreen extends Color() {
	createDelegate() => \iDARKGREEN;
}

"The color dark grey with an RGB value of #A9A9A9."
shared Color darkGrey = darkGray;

"The color dark khaki with an RGB value of #BDB76B."
shared object darkKhaki extends Color() {
	createDelegate() => \iDARKKHAKI;
}

"The color dark magenta with an RGB value of #8B008B."
shared object darkMagenta extends Color() {
	createDelegate() => \iDARKMAGENTA;
}

"The color dark olive green with an RGB value of #556B2F."
shared object darkOliveGreen extends Color() {
	createDelegate() => \iDARKOLIVEGREEN;
}

"The color dark orange with an RGB value of #FF8C00."
shared object darkOrange extends Color() {
	createDelegate() => \iDARKORANGE;
}

"The color dark orchid with an RGB value of #9932CC."
shared object darkOrchid extends Color() {
	createDelegate() => \iDARKORCHID;
}

"The color dark red with an RGB value of #8B0000."
shared object darkRed extends Color() {
	createDelegate() => \iDARKRED;
}

"The color dark salmon with an RGB value of #E9967A."
shared object darkSalmon extends Color() {
	createDelegate() => \iDARKSALMON;
}

"The color dark sea green with an RGB value of #8FBC8F."
shared object darkSeaGreen extends Color() {
	createDelegate() => \iDARKSEAGREEN;
}

"The color dark slate blue with an RGB value of #483D8B."
shared object darkSlateBlue extends Color() {
	createDelegate() => \iDARKSLATEBLUE;
}

"The color dark slate gray with an RGB value of #2F4F4F."
shared object darkSlateGray extends Color() {
	createDelegate() => \iDARKSLATEGRAY;
}

"The color dark slate grey with an RGB value of #2F4F4F."
shared Color darkSlateGrey = darkSlateGray;

"The color dark turquoise with an RGB value of #00CED1."
shared object darkTurquoise extends Color() {
	createDelegate() => \iDARKTURQUOISE;
}

"The color dark violet with an RGB value of #9400D3."
shared object darkViolet extends Color() {
	createDelegate() => \iDARKVIOLET;
}

"The color deep pink with an RGB value of #FF1493."
shared object deepPink extends Color() {
	createDelegate() => \iDEEPPINK;
}

"The color deep sky blue with an RGB value of #00BFFF."
shared object deepSkyBlue extends Color() {
	createDelegate() => \iDEEPSKYBLUE;
}

"The color dim gray with an RGB value of #696969."
shared object dimGray extends Color() {
	createDelegate() => \iDIMGRAY;
}

"The color dim grey with an RGB value of #696969."
shared Color dimGrey = dimGray;

"The color dodger blue with an RGB value of #1E90FF."
shared object dodgerBlue extends Color() {
	createDelegate() => \iDODGERBLUE;
}

"The color firebrick with an RGB value of #B22222."
shared object fireBrick extends Color() {
	createDelegate() => \iFIREBRICK;
}

"The color floral white with an RGB value of #FFFAF0."
shared object floralWhite extends Color() {
	createDelegate() => \iFLORALWHITE;
}

"The color forest green with an RGB value of #228B22."
shared object forestGreen extends Color() {
	createDelegate() => \iFORESTGREEN;
}

"The color fuchsia with an RGB value of #FF00FF."
shared object fuchsia extends Color() {
	createDelegate() => \iFUCHSIA;
}

"The color gainsboro with an RGB value of #DCDCDC."
shared object gainsboro extends Color() {
	createDelegate() => \iGAINSBORO;
}

"The color ghost white with an RGB value of #F8F8FF."
shared object ghostWhite extends Color() {
	createDelegate() => \iGHOSTWHITE;
}

"The color gold with an RGB value of #FFD700."
shared object gold extends Color() {
	createDelegate() => \iGOLD;
}

"The color goldenrod with an RGB value of #DAA520."
shared object goldenRod extends Color() {
	createDelegate() => \iGOLDENROD;
}

"The color gray with an RGB value of #808080."
shared object gray extends Color() {
	createDelegate() => \iGRAY;
}

"The color green with an RGB value of #008000."
shared object green extends Color() {
	createDelegate() => \iGREEN;
}

"The color green yellow with an RGB value of #ADFF2F."
shared object greenYellow extends Color() {
	createDelegate() => \iGREENYELLOW;
}

"The color grey with an RGB value of #808080."
shared Color grey = gray;

"The color honeydew with an RGB value of #F0FFF0."
shared object honeyDew extends Color() {
	createDelegate() => \iHONEYDEW;
}

"The color hot pink with an RGB value of #FF69B4."
shared object hotPink extends Color() {
	createDelegate() => \iHOTPINK;
}

"The color indian red with an RGB value of #CD5C5C."
shared object indianRed extends Color() {
	createDelegate() => \iINDIANRED;
}

"The color indigo with an RGB value of #4B0082."
shared object indigo extends Color() {
	createDelegate() => \iINDIGO;
}

"The color ivory with an RGB value of #FFFFF0."
shared object ivory extends Color() {
	createDelegate() => \iIVORY;
}

"The color khaki with an RGB value of #F0E68C."
shared object khaki extends Color() {
	createDelegate() => \iKHAKI;
}

"The color lavender with an RGB value of #E6E6FA."
shared object lavender extends Color() {
	createDelegate() => \iLAVENDER;
}

"The color lavender blush with an RGB value of #FFF0F5."
shared object lavenderBlush extends Color() {
	createDelegate() => \iLAVENDERBLUSH;
}

"The color lawn green with an RGB value of #7CFC00."
shared object lawnGreen extends Color() {
	createDelegate() => \iLAWNGREEN;
}

"The color lemon chiffon with an RGB value of #FFFACD."
shared object lemonChiffon extends Color() {
	createDelegate() => \iLEMONCHIFFON;
}

"The color light blue with an RGB value of #ADD8E6."
shared object lightBlue extends Color() {
	createDelegate() => \iLIGHTBLUE;
}

"The color light coral with an RGB value of #F08080."
shared object lightCoral extends Color() {
	createDelegate() => \iLIGHTCORAL;
}

"The color light cyan with an RGB value of #E0FFFF."
shared object lightCyan extends Color() {
	createDelegate() => \iLIGHTCYAN;
}

"The color light goldenrod yellow with an RGB value of #FAFAD2."
shared object lightGoldenRod extends Color() {
	createDelegate() => \iLIGHTGOLDENRODYELLOW;
}

"The color light gray with an RGB value of #D3D3D3."
shared object lightGray extends Color() {
	createDelegate() => \iLIGHTGRAY;
}

"The color light green with an RGB value of #90EE90."
shared object lightGreen extends Color() {
	createDelegate() => \iLIGHTGREEN;
}

"The color light grey with an RGB value of #D3D3D3."
shared Color lightGrey = lightGray;

"The color light pink with an RGB value of #FFB6C1."
shared object lightPink extends Color() {
	createDelegate() => \iLIGHTPINK;
}

"The color light salmon with an RGB value of #FFA07A."
shared object lightSalmon extends Color() {
	createDelegate() => \iLIGHTSALMON;
}

"The color light sea green with an RGB value of #20B2AA."
shared object lightSeaGreen extends Color() {
	createDelegate() => \iLIGHTSEAGREEN;
}

"The color light sky blue with an RGB value of #87CEFA."
shared object lightSkyBlue extends Color() {
	createDelegate() => \iLIGHTSKYBLUE;
}

"The color light slate gray with an RGB value of #778899."
shared object lightSlateGray extends Color() {
	createDelegate() => \iLIGHTSLATEGRAY;
}

"The color light slate grey with an RGB value of #778899."
shared Color lightSlateGrey = lightSlateGray;

"The color light steel blue with an RGB value of #B0C4DE."
shared object lightSteelBlue extends Color() {
	createDelegate() => \iLIGHTSTEELBLUE;
}

"The color light yellow with an RGB value of #FFFFE0."
shared object lightYellow extends Color() {
	createDelegate() => \iLIGHTYELLOW;
}

"The color lime with an RGB value of #00FF00."
shared object lime extends Color() {
	createDelegate() => \iLIME;
}

"The color lime green with an RGB value of #32CD32."
shared object limeGreen extends Color() {
	createDelegate() => \iLIMEGREEN;
}

"The color linen with an RGB value of #FAF0E6."
shared object linen extends Color() {
	createDelegate() => \iLINEN;
}

"The color magenta with an RGB value of #FF00FF."
shared object magenta extends Color() {
	createDelegate() => \iMAGENTA;
}

"The color maroon with an RGB value of #800000."
shared object maroon extends Color() {
	createDelegate() => \iMAROON;
}

"The color medium aquamarine with an RGB value of #66CDAA."
shared object mediumAquamarine extends Color() {
	createDelegate() => \iMEDIUMAQUAMARINE;
}

"The color medium blue with an RGB value of #0000CD."
shared object mediumBlue extends Color() {
	createDelegate() => \iMEDIUMBLUE; 
}

"The color medium orchid with an RGB value of #BA55D3."
shared object mediumOrchid extends Color() { 
	createDelegate() => \iMEDIUMORCHID; 
}

"The color medium purple with an RGB value of #9370DB."
shared object mediumPurple extends Color() {
	createDelegate() => \iMEDIUMPURPLE; 
}

"The color medium sea green with an RGB value of #3CB371."
shared object mediumSeaGreen extends Color() { 
	createDelegate() => \iMEDIUMSEAGREEN; 
}

"The color medium slate blue with an RGB value of #7B68EE."
shared object mediumSlateBlue extends Color() {
	createDelegate() => \iMEDIUMSLATEBLUE; 
}

"The color medium spring green with an RGB value of #00FA9A."
shared object mediumSpringGreen extends Color() { 
	createDelegate() => \iMEDIUMSPRINGGREEN; 
}

"The color medium turquoise with an RGB value of #48D1CC."
shared object mediumTurquoise extends Color() {
	createDelegate() => \iMEDIUMTURQUOISE; 
}

"The color medium violet red with an RGB value of #C71585."
shared object mediumVioletRed extends Color() {
	createDelegate() => \iMEDIUMVIOLETRED; 
}

"The color midnight blue with an RGB value of #191970."
shared object midnightBlue extends Color() { 
	createDelegate() => \iMIDNIGHTBLUE; 
}

"The color mint cream with an RGB value of #F5FFFA."
shared object mintCream extends Color() { 
	createDelegate() => \iMINTCREAM; 
}

"The color misty rose with an RGB value of #FFE4E1."
shared object mistyRose extends Color() { 
	createDelegate() => \iMISTYROSE; 
}

"The color moccasin with an RGB value of #FFE4B5."
shared object moccasin extends Color() { 
	createDelegate() => \iMOCCASIN; 
}

"The color navajo white with an RGB value of #FFDEAD."
shared object navajoWhite extends Color() { 
	createDelegate() => \iNAVAJOWHITE; 
}

"The color navy with an RGB value of #000080."
shared object navy extends Color() { 
	createDelegate() => \iNAVY; 
}

"The color old lace with an RGB value of #FDF5E6."
shared object oldLace extends Color() { 
	createDelegate() => \iOLDLACE; 
}

"The color olive with an RGB value of #808000."
shared object olive extends Color() { 
	createDelegate() => \iOLIVE; 
}

"The color olive drab with an RGB value of #6B8E23."
shared object oliveDrab extends Color() { 
	createDelegate() => \iOLIVEDRAB; 
}

"The color orange with an RGB value of #FFA500."
shared object orange extends Color() { 
	createDelegate() => \iORANGE; 
}

"The color orange red with an RGB value of #FF4500."
shared object orangeRed extends Color() { 
	createDelegate() => \iORANGERED; 
}

"The color orchid with an RGB value of #DA70D6."
shared object orchid extends Color() { 
	createDelegate() => \iORCHID; 
}

"The color pale goldenrod with an RGB value of #EEE8AA."
shared object paleGoldenRod extends Color() { 
	createDelegate() => \iPALEGOLDENROD; 
}

"The color pale green with an RGB value of #98FB98."
shared object paleGreen extends Color() { 
	createDelegate() => \iPALEGREEN; 
}

"The color pale turquoise with an RGB value of #AFEEEE."
shared object paleTurquoise extends Color() { 
	createDelegate() => \iPALETURQUOISE; 
}

"The color pale violet red with an RGB value of #DB7093."
shared object paleVioletRed extends Color() { 
	createDelegate() => \iPALEVIOLETRED; 
}

"The color papaya whip with an RGB value of #FFEFD5."
shared object papayaWhip extends Color() { 
	createDelegate() => \iPAPAYAWHIP; 
}

"The color peach puff with an RGB value of #FFDAB9."
shared object peachPuff extends Color() { 
	createDelegate() => \iPEACHPUFF; 
}

"The color peru with an RGB value of #CD853F."
shared object peru extends Color() { 
	createDelegate() => \iPERU; 
}

"The color pink with an RGB value of #FFC0CB."
shared object pink extends Color() { 
	createDelegate() => \iPINK; 
}

"The color plum with an RGB value of #DDA0DD."
shared object plum extends Color() { 
	createDelegate() => \iPLUM; 
}

"The color powder blue with an RGB value of #B0E0E6."
shared object powderBlue extends Color() { 
	createDelegate() => \iPOWDERBLUE; 
}

"The color purple with an RGB value of #800080."
shared object purple extends Color() { 
	createDelegate() => \iPURPLE; 
}

"The color red with an RGB value of #FF0000."
shared object red extends Color() { 
	createDelegate() => \iRED; 
}

"The color rosy brown with an RGB value of #BC8F8F."
shared object rosyBrown extends Color() { 
	createDelegate() => \iROSYBROWN; 
}

"The color royal blue with an RGB value of #4169E1."
shared object royalBlue extends Color() { 
	createDelegate() => \iROYALBLUE; 
}

"The color saddle brown with an RGB value of #8B4513."
shared object saddleBrown extends Color() { 
	createDelegate() => \iSADDLEBROWN; 
}

"The color salmon with an RGB value of #FA8072."
shared object salmon extends Color() { 
	createDelegate() => \iSALMON; 
}

"The color sandy brown with an RGB value of #F4A460."
shared object sandyBrown extends Color() { 
	createDelegate() => \iSANDYBROWN; 
}

"The color sea green with an RGB value of #2E8B57."
shared object seaGreen extends Color() { 
	createDelegate() => \iSEAGREEN; 
}

"The color sea shell with an RGB value of #FFF5EE."
shared object seaShell extends Color() { 
	createDelegate() => \iSEASHELL; 
}

"The color sienna with an RGB value of #A0522D."
shared object sienna extends Color() { 
	createDelegate() => \iSIENNA; 
}

"The color silver with an RGB value of #C0C0C0."
shared object silver extends Color() { 
	createDelegate() => \iSILVER; 
}

"The color sky blue with an RGB value of #87CEEB."
shared object skyBlue extends Color() { 
	createDelegate() => \iSKYBLUE; 
}

"The color slate blue with an RGB value of #6A5ACD."
shared object slateBlue extends Color() { 
	createDelegate() => \iSLATEBLUE; 
}

"The color slate gray with an RGB value of #708090."
shared object slateGray extends Color() { 
	createDelegate() => \iSLATEGRAY; 
}

"The color slate grey with an RGB value of #708090."
shared Color slateGrey = slateGray;

"The color snow with an RGB value of #FFFAFA."
shared object snow extends Color() { 
	createDelegate() => \iSNOW;
}

"The color spring green with an RGB value of #00FF7F."
shared object springGreen extends Color() { 
	createDelegate() => \iSPRINGGREEN; 
}

"The color steel blue with an RGB value of #4682B4."
shared object steelBlue extends Color() { 
	createDelegate() => \iSTEELBLUE; 
}

"The color tan with an RGB value of #D2B48C."
shared object tan extends Color() { 
	createDelegate() => \iTAN; 
}

"The color teal with an RGB value of #008080."
shared object teal extends Color() { 
	createDelegate() => \iTEAL; 
}

"The color thistle with an RGB value of #D8BFD8."
shared object thistle extends Color() { 
	createDelegate() => \iTHISTLE; 
}

"The color tomato with an RGB value of #FF6347."
shared object tomato extends Color() { 
	createDelegate() => \iTOMATO; 
}

"The color turquoise with an RGB value of #40E0D0."
shared object turquoise extends Color() { 
	createDelegate() => \iTURQUOISE; 
}

"The color violet with an RGB value of #EE82EE."
shared object violet extends Color() { 
	createDelegate() => \iVIOLET; 
}

"The color wheat with an RGB value of #F5DEB3."
shared object wheat extends Color() { 
	createDelegate() => \iWHEAT; 
}

"The color white with an RGB value of #FFFFFF."
shared object white extends Color() { 
	createDelegate() => \iWHITE; 
}

"The color white smoke with an RGB value of #F5F5F5."
shared object whiteSmoke extends Color() { 
	createDelegate() => \iWHITESMOKE; 
}

"The color yellow with an RGB value of #FFFF00."
shared object yellow extends Color() { 
	createDelegate() => \iYELLOW; 
}

"The color yellow green with an RGB value of #9ACD32."
shared object yellowGreen extends Color() { 
	createDelegate() => \iYELLOWGREEN; 
}
