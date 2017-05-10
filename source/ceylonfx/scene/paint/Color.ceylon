import javafx.scene.paint {
	JColor=Color {
		...
	}
} 

"The Color class is used to encapsulate colors in the default sRGB color space."
shared class Color(JColor jColor) 
		extends Paint() {
	shared actual JColor createDelegate() => jColor;
}

"Creates a grey color."
shared Color grayShade(Float gray, Float opacity = 1.0) 
		=> Color(JColor.gray(gray, opacity));

"Creates an sRGB color with the specified RGB values in the range 0-255, and a given opacity."
shared Color rgb(Integer red, Integer green, Integer blue, Float opacity = 1.0) 
		=> Color(JColor.rgb(red, green, blue, opacity));

"Creates an RGB color specified with an HTML or CSS attribute string."
shared Color web(String colorString, Float opacity = 1.0) 
		=> Color(JColor.web(colorString, opacity));

"Creates an sRGB color with the specified RGB values in the range #00_00_00 and #FF_FF_FF, and a given opacity."
shared Color rgbHexa(Integer hexadecimal, Float opacity = 1.0) {
	Integer red = hexadecimal / 65536 % 256;
	Integer green = hexadecimal / 256 % 256;
	Integer blue = hexadecimal % 256;
	return rgb(red, green, blue, opacity);
}

"A fully transparent color with an RGB value of #00000000."
shared Color transparent = Color(JColor.\iTRANSPARENT);

"The color alice blue with an RGB value of #F0F8FF."
shared Color aliceBlue = Color(JColor.\iALICEBLUE);

"The color antique white with an RGB value of #FAEBD7."
shared Color antiqueWhite = Color(JColor.\iANTIQUEWHITE);

"The color aqua with an RGB value of #00FFFF."
shared Color aqua = Color(JColor.\iAQUA);

"The color aquamarine with an RGB value of #7FFFD4."
shared Color aquaMarine = Color(JColor.\iAQUAMARINE);

"The color azure with an RGB value of #F0FFFF."
shared Color azure = Color(JColor.\iAZURE);

"The color beige with an RGB value of #F5F5DC."
shared Color beige = Color(JColor.\iBEIGE);

"The color bisque with an RGB value of #FFE4C4."
shared Color bisque = Color(JColor.\iBISQUE);

"The color black with an RGB value of #000000."
shared Color black = Color(JColor.\iBLACK);

"The color blanched almond with an RGB value of #FFEBCD."
shared Color blanchedAlmond = Color(JColor.\iBLANCHEDALMOND);

"The color blue with an RGB value of #0000FF."
shared Color blue = Color(JColor.\iBLUE);

"The color blue violet with an RGB value of #8A2BE2."
shared Color blueViolet = Color(JColor.\iBLUEVIOLET);

"The color brown with an RGB value of #A52A2A."
shared Color brown = Color(JColor.\iBROWN);

"The color burly wood with an RGB value of #DEB887."
shared Color burlyWood = Color(JColor.\iBURLYWOOD);

"The color cadet blue with an RGB value of #5F9EA0."
shared Color cadetBlue = Color(JColor.\iCADETBLUE);

"The color chartreuse with an RGB value of #7FFF00."
shared Color chartReuse = Color(JColor.\iCHARTREUSE);

"The color chocolate with an RGB value of #D2691E."
shared Color chocolate = Color(JColor.\iCHOCOLATE);

"The color coral with an RGB value of #FF7F50."
shared Color coral = Color(JColor.\iCORAL);

"The color cornflower blue with an RGB value of #6495ED."
shared Color cornFlowerBlue = Color(JColor.\iCORNFLOWERBLUE);

"The color cornsilk with an RGB value of #FFF8DC."
shared Color cornSilk = Color(JColor.\iCORNSILK);

"The color crimson with an RGB value of #DC143C."
shared Color crimson = Color(JColor.\iCRIMSON);

"The color cyan with an RGB value of #00FFFF."
shared Color cyan = Color(JColor.\iCYAN);

"The color dark blue with an RGB value of #00008B."
shared Color darkBlue = Color(JColor.\iDARKBLUE);

"The color dark cyan with an RGB value of #008B8B."
shared Color darkCyan = Color(JColor.\iDARKCYAN);

"The color dark goldenrod with an RGB value of #B8860B."
shared Color darkGoldenrod = Color(JColor.\iDARKGOLDENROD);

"The color dark gray with an RGB value of #A9A9A9."
shared Color darkGray = Color(JColor.\iDARKGRAY);

"The color dark green with an RGB value of #006400."
shared Color darkGreen = Color(JColor.\iDARKGREEN);

"The color dark grey with an RGB value of #A9A9A9."
shared Color darkGrey = darkGray;

"The color dark khaki with an RGB value of #BDB76B."
shared Color darkKhaki = Color(JColor.\iDARKKHAKI);

"The color dark magenta with an RGB value of #8B008B."
shared Color darkMagenta = Color(JColor.\iDARKMAGENTA);

"The color dark olive green with an RGB value of #556B2F."
shared Color darkOliveGreen = Color(JColor.\iDARKOLIVEGREEN);

"The color dark orange with an RGB value of #FF8C00."
shared Color darkOrange = Color(JColor.\iDARKORANGE);

"The color dark orchid with an RGB value of #9932CC."
shared Color darkOrchid = Color(JColor.\iDARKORCHID);

"The color dark red with an RGB value of #8B0000."
shared Color darkRed = Color(JColor.\iDARKRED);

"The color dark salmon with an RGB value of #E9967A."
shared Color darkSalmon = Color(JColor.\iDARKSALMON);

"The color dark sea green with an RGB value of #8FBC8F."
shared Color darkSeaGreen = Color(JColor.\iDARKSEAGREEN);

"The color dark slate blue with an RGB value of #483D8B."
shared Color darkSlateBlue = Color(JColor.\iDARKSLATEBLUE);

"The color dark slate gray with an RGB value of #2F4F4F."
shared Color darkSlateGray = Color(JColor.\iDARKSLATEGRAY);

"The color dark slate grey with an RGB value of #2F4F4F."
shared Color darkSlateGrey = darkSlateGray;
"The color dark turquoise with an RGB value of #00CED1."
shared Color darkTurquoise = Color(JColor.\iDARKTURQUOISE);

"The color dark violet with an RGB value of #9400D3."
shared Color darkViolet = Color(JColor.\iDARKVIOLET);

"The color deep pink with an RGB value of #FF1493."
shared Color deepPink = Color(JColor.\iDEEPPINK);

"The color deep sky blue with an RGB value of #00BFFF."
shared Color deepSkyBlue = Color(JColor.\iDEEPSKYBLUE);

"The color dim gray with an RGB value of #696969."
shared Color dimGray = Color(JColor.\iDIMGRAY);

"The color dim grey with an RGB value of #696969."
shared Color dimGrey = dimGray;
"The color dodger blue with an RGB value of #1E90FF."
shared Color dodgerBlue = Color(JColor.\iDODGERBLUE);

"The color firebrick with an RGB value of #B22222."
shared Color fireBrick = Color(JColor.\iFIREBRICK);

"The color floral white with an RGB value of #FFFAF0."
shared Color floralWhite = Color(JColor.\iFLORALWHITE);

"The color forest green with an RGB value of #228B22."
shared Color forestGreen = Color(JColor.\iFORESTGREEN);

"The color fuchsia with an RGB value of #FF00FF."
shared Color fuchsia = Color(JColor.\iFUCHSIA);

"The color gainsboro with an RGB value of #DCDCDC."
shared Color gainsboro = Color(JColor.\iGAINSBORO);

"The color ghost white with an RGB value of #F8F8FF."
shared Color ghostWhite = Color(JColor.\iGHOSTWHITE);

"The color gold with an RGB value of #FFD700."
shared Color gold = Color(JColor.\iGOLD);

"The color goldenrod with an RGB value of #DAA520."
shared Color goldenRod = Color(JColor.\iGOLDENROD);

"The color gray with an RGB value of #808080."
shared Color gray = Color(JColor.\iGRAY);

"The color green with an RGB value of #008000."
shared Color green = Color(JColor.\iGREEN);

"The color green yellow with an RGB value of #ADFF2F."
shared Color greenYellow = Color(JColor.\iGREENYELLOW);

"The color grey with an RGB value of #808080."
shared Color grey = gray;

"The color honeydew with an RGB value of #F0FFF0."
shared Color honeyDew = Color(JColor.\iHONEYDEW);

"The color hot pink with an RGB value of #FF69B4."
shared Color hotPink = Color(JColor.\iHOTPINK);

"The color indian red with an RGB value of #CD5C5C."
shared Color indianRed = Color(JColor.\iINDIANRED);

"The color indigo with an RGB value of #4B0082."
shared Color indigo = Color(JColor.\iINDIGO);

"The color ivory with an RGB value of #FFFFF0."
shared Color ivory = Color(JColor.\iIVORY);

"The color khaki with an RGB value of #F0E68C."
shared Color khaki = Color(JColor.\iKHAKI);

"The color lavender with an RGB value of #E6E6FA."
shared Color lavender = Color(JColor.\iLAVENDER);

"The color lavender blush with an RGB value of #FFF0F5."
shared Color lavenderBlush = Color(JColor.\iLAVENDERBLUSH);

"The color lawn green with an RGB value of #7CFC00."
shared Color lawnGreen = Color(JColor.\iLAWNGREEN);

"The color lemon chiffon with an RGB value of #FFFACD."
shared Color lemonChiffon = Color(JColor.\iLEMONCHIFFON);

"The color light blue with an RGB value of #ADD8E6."
shared Color lightBlue = Color(JColor.\iLIGHTBLUE);

"The color light coral with an RGB value of #F08080."
shared Color lightCoral = Color(JColor.\iLIGHTCORAL);

"The color light cyan with an RGB value of #E0FFFF."
shared Color lightCyan = Color(JColor.\iLIGHTCYAN);

"The color light goldenrod yellow with an RGB value of #FAFAD2."
shared Color lightGoldenRod = Color(JColor.\iLIGHTGOLDENRODYELLOW);

"The color light gray with an RGB value of #D3D3D3."
shared Color lightGray = Color(JColor.\iLIGHTGRAY);

"The color light green with an RGB value of #90EE90."
shared Color lightGreen = Color(JColor.\iLIGHTGREEN);

"The color light grey with an RGB value of #D3D3D3."
shared Color lightGrey = lightGray;
"The color light pink with an RGB value of #FFB6C1."
shared Color lightPink = Color(JColor.\iLIGHTPINK);

"The color light salmon with an RGB value of #FFA07A."
shared Color lightSalmon = Color(JColor.\iLIGHTSALMON);

"The color light sea green with an RGB value of #20B2AA."
shared Color lightSeaGreen = Color(JColor.\iLIGHTSEAGREEN);

"The color light sky blue with an RGB value of #87CEFA."
shared Color lightSkyBlue = Color(JColor.\iLIGHTSKYBLUE);

"The color light slate gray with an RGB value of #778899."
shared Color lightSlateGray = Color(JColor.\iLIGHTSLATEGRAY);

"The color light slate grey with an RGB value of #778899."
shared Color lightSlateGrey = lightSlateGray;
"The color light steel blue with an RGB value of #B0C4DE."
shared Color lightSteelBlue = Color(JColor.\iLIGHTSTEELBLUE);

"The color light yellow with an RGB value of #FFFFE0."
shared Color lightYellow = Color(JColor.\iLIGHTYELLOW);

"The color lime with an RGB value of #00FF00."
shared Color lime = Color(JColor.\iLIME);

"The color lime green with an RGB value of #32CD32."
shared Color limeGreen = Color(JColor.\iLIMEGREEN);

"The color linen with an RGB value of #FAF0E6."
shared Color linen = Color(JColor.\iLINEN);

"The color magenta with an RGB value of #FF00FF."
shared Color magenta = Color(JColor.\iMAGENTA);

"The color maroon with an RGB value of #800000."
shared Color maroon = Color(JColor.\iMAROON);

"The color medium aquamarine with an RGB value of #66CDAA."
shared Color mediumAquamarine = Color(JColor.\iMEDIUMAQUAMARINE);

"The color medium blue with an RGB value of #0000CD."
shared Color mediumBlue = Color(JColor.\iMEDIUMBLUE);

"The color medium orchid with an RGB value of #BA55D3."
shared Color mediumOrchid = Color(JColor.\iMEDIUMORCHID);

"The color medium purple with an RGB value of #9370DB."
shared Color mediumPurple = Color(JColor.\iMEDIUMPURPLE);

"The color medium sea green with an RGB value of #3CB371."
shared Color mediumSeaGreen = Color(JColor.\iMEDIUMSEAGREEN);

"The color medium slate blue with an RGB value of #7B68EE."
shared Color mediumSlateBlue = Color(JColor.\iMEDIUMSLATEBLUE);

"The color medium spring green with an RGB value of #00FA9A."
shared Color mediumSpringGreen = Color(JColor.\iMEDIUMSPRINGGREEN);

"The color medium turquoise with an RGB value of #48D1CC."
shared Color mediumTurquoise = Color(JColor.\iMEDIUMTURQUOISE);

"The color medium violet red with an RGB value of #C71585."
shared Color mediumVioletRed = Color(JColor.\iMEDIUMVIOLETRED);

"The color midnight blue with an RGB value of #191970."
shared Color midnightBlue = Color(JColor.\iMIDNIGHTBLUE);

"The color mint cream with an RGB value of #F5FFFA."
shared Color mintCream = Color(JColor.\iMINTCREAM);

"The color misty rose with an RGB value of #FFE4E1."
shared Color mistyRose = Color(JColor.\iMISTYROSE);

"The color moccasin with an RGB value of #FFE4B5."
shared Color moccasin = Color(JColor.\iMOCCASIN);

"The color navajo white with an RGB value of #FFDEAD."
shared Color navajoWhite = Color(JColor.\iNAVAJOWHITE);

"The color navy with an RGB value of #000080."
shared Color navy = Color(JColor.\iNAVY);

"The color old lace with an RGB value of #FDF5E6."
shared Color oldLace = Color(JColor.\iOLDLACE);

"The color olive with an RGB value of #808000."
shared Color olive = Color(JColor.\iOLIVE);

"The color olive drab with an RGB value of #6B8E23."
shared Color oliveDrab = Color(JColor.\iOLIVEDRAB);

"The color orange with an RGB value of #FFA500."
shared Color orange = Color(JColor.\iORANGE);

"The color orange red with an RGB value of #FF4500."
shared Color orangeRed = Color(JColor.\iORANGERED);

"The color orchid with an RGB value of #DA70D6."
shared Color orchid = Color(JColor.\iORCHID);

"The color pale goldenrod with an RGB value of #EEE8AA."
shared Color paleGoldenRod = Color(JColor.\iPALEGOLDENROD);

"The color pale green with an RGB value of #98FB98."
shared Color paleGreen = Color(JColor.\iPALEGREEN);

"The color pale turquoise with an RGB value of #AFEEEE."
shared Color paleTurquoise = Color(JColor.\iPALETURQUOISE);

"The color pale violet red with an RGB value of #DB7093."
shared Color paleVioletRed = Color(JColor.\iPALEVIOLETRED);

"The color papaya whip with an RGB value of #FFEFD5."
shared Color papayaWhip = Color(JColor.\iPAPAYAWHIP);

"The color peach puff with an RGB value of #FFDAB9."
shared Color peachPuff = Color(JColor.\iPEACHPUFF);

"The color peru with an RGB value of #CD853F."
shared Color peru = Color(JColor.\iPERU);

"The color pink with an RGB value of #FFC0CB."
shared Color pink = Color(JColor.\iPINK);

"The color plum with an RGB value of #DDA0DD."
shared Color plum = Color(JColor.\iPLUM);

"The color powder blue with an RGB value of #B0E0E6."
shared Color powderBlue = Color(JColor.\iPOWDERBLUE);

"The color purple with an RGB value of #800080."
shared Color purple = Color(JColor.\iPURPLE);

"The color red with an RGB value of #FF0000."
shared Color red = Color(JColor.\iRED);

"The color rosy brown with an RGB value of #BC8F8F."
shared Color rosyBrown = Color(JColor.\iROSYBROWN);

"The color royal blue with an RGB value of #4169E1."
shared Color royalBlue = Color(JColor.\iROYALBLUE);

"The color saddle brown with an RGB value of #8B4513."
shared Color saddleBrown = Color(JColor.\iSADDLEBROWN);

"The color salmon with an RGB value of #FA8072."
shared Color salmon = Color(JColor.\iSALMON);

"The color sandy brown with an RGB value of #F4A460."
shared Color sandyBrown = Color(JColor.\iSANDYBROWN);

"The color sea green with an RGB value of #2E8B57."
shared Color seaGreen = Color(JColor.\iSEAGREEN);

"The color sea shell with an RGB value of #FFF5EE."
shared Color seaShell = Color(JColor.\iSEASHELL);

"The color sienna with an RGB value of #A0522D."
shared Color sienna = Color(JColor.\iSIENNA);

"The color silver with an RGB value of #C0C0C0."
shared Color silver = Color(JColor.\iSILVER);

"The color sky blue with an RGB value of #87CEEB."
shared Color skyBlue = Color(JColor.\iSKYBLUE);

"The color slate blue with an RGB value of #6A5ACD."
shared Color slateBlue = Color(JColor.\iSLATEBLUE);

"The color slate gray with an RGB value of #708090."
shared Color slateGray = Color(JColor.\iSLATEGRAY);

"The color slate grey with an RGB value of #708090."
shared Color slateGrey = slateGray;

"The color snow with an RGB value of #FFFAFA."
shared Color snow = Color(JColor.\iSNOW);

"The color spring green with an RGB value of #00FF7F."
shared Color springGreen = Color(JColor.\iSPRINGGREEN);

"The color steel blue with an RGB value of #4682B4."
shared Color steelBlue = Color(JColor.\iSTEELBLUE);

"The color tan with an RGB value of #D2B48C."
shared Color tan = Color(JColor.\iTAN);

"The color teal with an RGB value of #008080."
shared Color teal = Color(JColor.\iTEAL);

"The color thistle with an RGB value of #D8BFD8."
shared Color thistle = Color(JColor.\iTHISTLE);

"The color tomato with an RGB value of #FF6347."
shared Color tomato = Color(JColor.\iTOMATO);

"The color turquoise with an RGB value of #40E0D0."
shared Color turquoise = Color(JColor.\iTURQUOISE);

"The color violet with an RGB value of #EE82EE."
shared Color violet = Color(JColor.\iVIOLET);

"The color wheat with an RGB value of #F5DEB3."
shared Color wheat = Color(JColor.\iWHEAT);

"The color white with an RGB value of #FFFFFF."
shared Color white = Color(JColor.\iWHITE);

"The color white smoke with an RGB value of #F5F5F5."
shared Color whiteSmoke = Color(JColor.\iWHITESMOKE);

"The color yellow with an RGB value of #FFFF00."
shared Color yellow = Color(JColor.\iYELLOW);

"The color yellow green with an RGB value of #9ACD32."
shared Color yellowGreen = Color(JColor.\iYELLOWGREEN);
