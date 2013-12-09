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
	print("Creating color ``red`` ``green`` ``blue``");
	return rgb(red, green, blue, opacity);
}

"A fully transparent color with an RGB value of #00000000."
shared Color transparent = Color(\iTRANSPARENT);

"The color alice blue with an RGB value of #F0F8FF."
shared Color aliceBlue = Color(\iALICEBLUE);

"The color antique white with an RGB value of #FAEBD7."
shared Color antiqueWhite = Color(\iANTIQUEWHITE);

"The color aqua with an RGB value of #00FFFF."
shared Color aqua = Color(\iAQUA);

"The color aquamarine with an RGB value of #7FFFD4."
shared Color aquaMarine = Color(\iAQUAMARINE);

"The color azure with an RGB value of #F0FFFF."
shared Color azure = Color(\iAZURE);

"The color beige with an RGB value of #F5F5DC."
shared Color beige = Color(\iBEIGE);

"The color bisque with an RGB value of #FFE4C4."
shared Color bisque = Color(\iBISQUE);

"The color black with an RGB value of #000000."
shared Color black = Color(\iBLACK);

"The color blanched almond with an RGB value of #FFEBCD."
shared Color blanchedAlmond = Color(\iBLANCHEDALMOND);

"The color blue with an RGB value of #0000FF."
shared Color blue = Color(\iBLUE);

"The color blue violet with an RGB value of #8A2BE2."
shared Color blueViolet = Color(\iBLUEVIOLET);

"The color brown with an RGB value of #A52A2A."
shared Color brown = Color(\iBROWN);

"The color burly wood with an RGB value of #DEB887."
shared Color burlyWood = Color(\iBURLYWOOD);

"The color cadet blue with an RGB value of #5F9EA0."
shared Color cadetBlue = Color(\iCADETBLUE);

"The color chartreuse with an RGB value of #7FFF00."
shared Color chartReuse = Color(\iCHARTREUSE);

"The color chocolate with an RGB value of #D2691E."
shared Color chocolate = Color(\iCHOCOLATE);

"The color coral with an RGB value of #FF7F50."
shared Color coral = Color(\iCORAL);

"The color cornflower blue with an RGB value of #6495ED."
shared Color cornFlowerBlue = Color(\iCORNFLOWERBLUE);

"The color cornsilk with an RGB value of #FFF8DC."
shared Color cornSilk = Color(\iCORNSILK);

"The color crimson with an RGB value of #DC143C."
shared Color crimson = Color(\iCRIMSON);

"The color cyan with an RGB value of #00FFFF."
shared Color cyan = Color(\iCYAN);

"The color dark blue with an RGB value of #00008B."
shared Color darkBlue = Color(\iDARKBLUE);

"The color dark cyan with an RGB value of #008B8B."
shared Color darkCyan = Color(\iDARKCYAN);

"The color dark goldenrod with an RGB value of #B8860B."
shared Color darkGoldenrod = Color(\iDARKGOLDENROD);

"The color dark gray with an RGB value of #A9A9A9."
shared Color darkGray = Color(\iDARKGRAY);

"The color dark green with an RGB value of #006400."
shared Color darkGreen = Color(\iDARKGREEN);

"The color dark grey with an RGB value of #A9A9A9."
shared Color darkGrey = darkGray;

"The color dark khaki with an RGB value of #BDB76B."
shared Color darkKhaki = Color(\iDARKKHAKI);

"The color dark magenta with an RGB value of #8B008B."
shared Color darkMagenta = Color(\iDARKMAGENTA);

"The color dark olive green with an RGB value of #556B2F."
shared Color darkOliveGreen = Color(\iDARKOLIVEGREEN);

"The color dark orange with an RGB value of #FF8C00."
shared Color darkOrange = Color(\iDARKORANGE);

"The color dark orchid with an RGB value of #9932CC."
shared Color darkOrchid = Color(\iDARKORCHID);

"The color dark red with an RGB value of #8B0000."
shared Color darkRed = Color(\iDARKRED);

"The color dark salmon with an RGB value of #E9967A."
shared Color darkSalmon = Color(\iDARKSALMON);

"The color dark sea green with an RGB value of #8FBC8F."
shared Color darkSeaGreen = Color(\iDARKSEAGREEN);

"The color dark slate blue with an RGB value of #483D8B."
shared Color darkSlateBlue = Color(\iDARKSLATEBLUE);

"The color dark slate gray with an RGB value of #2F4F4F."
shared Color darkSlateGray = Color(\iDARKSLATEGRAY);

"The color dark slate grey with an RGB value of #2F4F4F."
shared Color darkSlateGrey = darkSlateGray;
"The color dark turquoise with an RGB value of #00CED1."
shared Color darkTurquoise = Color(\iDARKTURQUOISE);

"The color dark violet with an RGB value of #9400D3."
shared Color darkViolet = Color(\iDARKVIOLET);

"The color deep pink with an RGB value of #FF1493."
shared Color deepPink = Color(\iDEEPPINK);

"The color deep sky blue with an RGB value of #00BFFF."
shared Color deepSkyBlue = Color(\iDEEPSKYBLUE);

"The color dim gray with an RGB value of #696969."
shared Color dimGray = Color(\iDIMGRAY);

"The color dim grey with an RGB value of #696969."
shared Color dimGrey = dimGray;
"The color dodger blue with an RGB value of #1E90FF."
shared Color dodgerBlue = Color(\iDODGERBLUE);

"The color firebrick with an RGB value of #B22222."
shared Color fireBrick = Color(\iFIREBRICK);

"The color floral white with an RGB value of #FFFAF0."
shared Color floralWhite = Color(\iFLORALWHITE);

"The color forest green with an RGB value of #228B22."
shared Color forestGreen = Color(\iFORESTGREEN);

"The color fuchsia with an RGB value of #FF00FF."
shared Color fuchsia = Color(\iFUCHSIA);

"The color gainsboro with an RGB value of #DCDCDC."
shared Color gainsboro = Color(\iGAINSBORO);

"The color ghost white with an RGB value of #F8F8FF."
shared Color ghostWhite = Color(\iGHOSTWHITE);

"The color gold with an RGB value of #FFD700."
shared Color gold = Color(\iGOLD);

"The color goldenrod with an RGB value of #DAA520."
shared Color goldenRod = Color(\iGOLDENROD);

"The color gray with an RGB value of #808080."
shared Color gray = Color(\iGRAY);

"The color green with an RGB value of #008000."
shared Color green = Color(\iGREEN);

"The color green yellow with an RGB value of #ADFF2F."
shared Color greenYellow = Color(\iGREENYELLOW);

"The color grey with an RGB value of #808080."
shared Color grey = gray;

"The color honeydew with an RGB value of #F0FFF0."
shared Color honeyDew = Color(\iHONEYDEW);

"The color hot pink with an RGB value of #FF69B4."
shared Color hotPink = Color(\iHOTPINK);

"The color indian red with an RGB value of #CD5C5C."
shared Color indianRed = Color(\iINDIANRED);

"The color indigo with an RGB value of #4B0082."
shared Color indigo = Color(\iINDIGO);

"The color ivory with an RGB value of #FFFFF0."
shared Color ivory = Color(\iIVORY);

"The color khaki with an RGB value of #F0E68C."
shared Color khaki = Color(\iKHAKI);

"The color lavender with an RGB value of #E6E6FA."
shared Color lavender = Color(\iLAVENDER);

"The color lavender blush with an RGB value of #FFF0F5."
shared Color lavenderBlush = Color(\iLAVENDERBLUSH);

"The color lawn green with an RGB value of #7CFC00."
shared Color lawnGreen = Color(\iLAWNGREEN);

"The color lemon chiffon with an RGB value of #FFFACD."
shared Color lemonChiffon = Color(\iLEMONCHIFFON);

"The color light blue with an RGB value of #ADD8E6."
shared Color lightBlue = Color(\iLIGHTBLUE);

"The color light coral with an RGB value of #F08080."
shared Color lightCoral = Color(\iLIGHTCORAL);

"The color light cyan with an RGB value of #E0FFFF."
shared Color lightCyan = Color(\iLIGHTCYAN);

"The color light goldenrod yellow with an RGB value of #FAFAD2."
shared Color lightGoldenRod = Color(\iLIGHTGOLDENRODYELLOW);

"The color light gray with an RGB value of #D3D3D3."
shared Color lightGray = Color(\iLIGHTGRAY);

"The color light green with an RGB value of #90EE90."
shared Color lightGreen = Color(\iLIGHTGREEN);

"The color light grey with an RGB value of #D3D3D3."
shared Color lightGrey = lightGray;
"The color light pink with an RGB value of #FFB6C1."
shared Color lightPink = Color(\iLIGHTPINK);

"The color light salmon with an RGB value of #FFA07A."
shared Color lightSalmon = Color(\iLIGHTSALMON);

"The color light sea green with an RGB value of #20B2AA."
shared Color lightSeaGreen = Color(\iLIGHTSEAGREEN);

"The color light sky blue with an RGB value of #87CEFA."
shared Color lightSkyBlue = Color(\iLIGHTSKYBLUE);

"The color light slate gray with an RGB value of #778899."
shared Color lightSlateGray = Color(\iLIGHTSLATEGRAY);

"The color light slate grey with an RGB value of #778899."
shared Color lightSlateGrey = lightSlateGray;
"The color light steel blue with an RGB value of #B0C4DE."
shared Color lightSteelBlue = Color(\iLIGHTSTEELBLUE);

"The color light yellow with an RGB value of #FFFFE0."
shared Color lightYellow = Color(\iLIGHTYELLOW);

"The color lime with an RGB value of #00FF00."
shared Color lime = Color(\iLIME);

"The color lime green with an RGB value of #32CD32."
shared Color limeGreen = Color(\iLIMEGREEN);

"The color linen with an RGB value of #FAF0E6."
shared Color linen = Color(\iLINEN);

"The color magenta with an RGB value of #FF00FF."
shared Color magenta = Color(\iMAGENTA);

"The color maroon with an RGB value of #800000."
shared Color maroon = Color(\iMAROON);

"The color medium aquamarine with an RGB value of #66CDAA."
shared Color mediumAquamarine = Color(\iMEDIUMAQUAMARINE);

"The color medium blue with an RGB value of #0000CD."
shared Color mediumBlue = Color(\iMEDIUMBLUE); 

"The color medium orchid with an RGB value of #BA55D3."
shared Color mediumOrchid = Color(\iMEDIUMORCHID); 

"The color medium purple with an RGB value of #9370DB."
shared Color mediumPurple = Color(\iMEDIUMPURPLE); 

"The color medium sea green with an RGB value of #3CB371."
shared Color mediumSeaGreen = Color(\iMEDIUMSEAGREEN);

"The color medium slate blue with an RGB value of #7B68EE."
shared Color mediumSlateBlue = Color(\iMEDIUMSLATEBLUE);

"The color medium spring green with an RGB value of #00FA9A."
shared Color mediumSpringGreen = Color(\iMEDIUMSPRINGGREEN); 

"The color medium turquoise with an RGB value of #48D1CC."
shared Color mediumTurquoise = Color(\iMEDIUMTURQUOISE); 

"The color medium violet red with an RGB value of #C71585."
shared Color mediumVioletRed = Color(\iMEDIUMVIOLETRED); 

"The color midnight blue with an RGB value of #191970."
shared Color midnightBlue = Color(\iMIDNIGHTBLUE); 

"The color mint cream with an RGB value of #F5FFFA."
shared Color mintCream = Color(\iMINTCREAM);

"The color misty rose with an RGB value of #FFE4E1."
shared Color mistyRose = Color(\iMISTYROSE); 

"The color moccasin with an RGB value of #FFE4B5."
shared Color moccasin = Color(\iMOCCASIN); 

"The color navajo white with an RGB value of #FFDEAD."
shared Color navajoWhite = Color(\iNAVAJOWHITE);

"The color navy with an RGB value of #000080."
shared Color navy = Color(\iNAVY); 

"The color old lace with an RGB value of #FDF5E6."
shared Color oldLace = Color(\iOLDLACE); 

"The color olive with an RGB value of #808000."
shared Color olive = Color(\iOLIVE); 

"The color olive drab with an RGB value of #6B8E23."
shared Color oliveDrab = Color(\iOLIVEDRAB); 

"The color orange with an RGB value of #FFA500."
shared Color orange = Color(\iORANGE); 

"The color orange red with an RGB value of #FF4500."
shared Color orangeRed = Color(\iORANGERED); 

"The color orchid with an RGB value of #DA70D6."
shared Color orchid = Color(\iORCHID); 

"The color pale goldenrod with an RGB value of #EEE8AA."
shared Color paleGoldenRod = Color(\iPALEGOLDENROD); 

"The color pale green with an RGB value of #98FB98."
shared Color paleGreen = Color(\iPALEGREEN); 

"The color pale turquoise with an RGB value of #AFEEEE."
shared Color paleTurquoise = Color(\iPALETURQUOISE); 

"The color pale violet red with an RGB value of #DB7093."
shared Color paleVioletRed = Color(\iPALEVIOLETRED); 

"The color papaya whip with an RGB value of #FFEFD5."
shared Color papayaWhip = Color(\iPAPAYAWHIP); 

"The color peach puff with an RGB value of #FFDAB9."
shared Color peachPuff = Color(\iPEACHPUFF); 

"The color peru with an RGB value of #CD853F."
shared Color peru = Color(\iPERU); 

"The color pink with an RGB value of #FFC0CB."
shared Color pink = Color(\iPINK);

"The color plum with an RGB value of #DDA0DD."
shared Color plum = Color(\iPLUM);

"The color powder blue with an RGB value of #B0E0E6."
shared Color powderBlue = Color(\iPOWDERBLUE);

"The color purple with an RGB value of #800080."
shared Color purple = Color(\iPURPLE);

"The color red with an RGB value of #FF0000."
shared Color red = Color(\iRED);

"The color rosy brown with an RGB value of #BC8F8F."
shared Color rosyBrown = Color(\iROSYBROWN);

"The color royal blue with an RGB value of #4169E1."
shared Color royalBlue = Color(\iROYALBLUE);

"The color saddle brown with an RGB value of #8B4513."
shared Color saddleBrown = Color(\iSADDLEBROWN);

"The color salmon with an RGB value of #FA8072."
shared Color salmon = Color(\iSALMON);

"The color sandy brown with an RGB value of #F4A460."
shared Color sandyBrown = Color(\iSANDYBROWN);

"The color sea green with an RGB value of #2E8B57."
shared Color seaGreen = Color(\iSEAGREEN);

"The color sea shell with an RGB value of #FFF5EE."
shared Color seaShell = Color(\iSEASHELL);

"The color sienna with an RGB value of #A0522D."
shared Color sienna = Color(\iSIENNA);

"The color silver with an RGB value of #C0C0C0."
shared Color silver = Color(\iSILVER);

"The color sky blue with an RGB value of #87CEEB."
shared Color skyBlue = Color(\iSKYBLUE);

"The color slate blue with an RGB value of #6A5ACD."
shared Color slateBlue = Color(\iSLATEBLUE);

"The color slate gray with an RGB value of #708090."
shared Color slateGray = Color(\iSLATEGRAY);

"The color slate grey with an RGB value of #708090."
shared Color slateGrey = slateGray;

"The color snow with an RGB value of #FFFAFA."
shared Color snow = Color(\iSNOW);

"The color spring green with an RGB value of #00FF7F."
shared Color springGreen = Color(\iSPRINGGREEN);

"The color steel blue with an RGB value of #4682B4."
shared Color steelBlue = Color(\iSTEELBLUE);

"The color tan with an RGB value of #D2B48C."
shared Color tan = Color(\iTAN);

"The color teal with an RGB value of #008080."
shared Color teal = Color(\iTEAL);

"The color thistle with an RGB value of #D8BFD8."
shared Color thistle = Color(\iTHISTLE);

"The color tomato with an RGB value of #FF6347."
shared Color tomato = Color(\iTOMATO);

"The color turquoise with an RGB value of #40E0D0."
shared Color turquoise = Color(\iTURQUOISE);

"The color violet with an RGB value of #EE82EE."
shared Color violet = Color(\iVIOLET); 

"The color wheat with an RGB value of #F5DEB3."
shared Color wheat = Color(\iWHEAT);

"The color white with an RGB value of #FFFFFF."
shared Color white = Color(\iWHITE);

"The color white smoke with an RGB value of #F5F5F5."
shared Color whiteSmoke = Color(\iWHITESMOKE);

"The color yellow with an RGB value of #FFFF00."
shared Color yellow = Color(\iYELLOW);

"The color yellow green with an RGB value of #9ACD32."
shared Color yellowGreen = Color(\iYELLOWGREEN);
