import javafx.geometry {
	HPos,
	Pos,
	VPos
}

shared abstract class HorizontalPosition(shared HPos hpos)
		of horizontalCenter|horizontalLeft|horizontalRight {
	string=>hpos.string;
}
shared object horizontalCenter 
		extends HorizontalPosition(HPos.\iCENTER) {}
shared object horizontalLeft
		extends HorizontalPosition(HPos.\iLEFT) {}
shared object horizontalRight
		extends HorizontalPosition(HPos.\iRIGHT) {}

shared abstract class VerticalPosition(shared VPos vpos)
		of verticalBaseline|verticalCenter|verticalBottom|verticalTop {
	string=>vpos.string;
}

shared object verticalBaseline
		extends VerticalPosition(VPos.\iBASELINE) {}
shared object verticalCenter 
		extends VerticalPosition(VPos.\iCENTER) {}
shared object verticalBottom
		extends VerticalPosition(VPos.\iBOTTOM) {}
shared object verticalTop
		extends VerticalPosition(VPos.\iTOP) {}

shared abstract class Position(
	shared HorizontalPosition hPosition,
	shared VerticalPosition vPosition)
		of topLeft|topCenter|topRight|
		centerLeft|center|centerRight|
		bottomLeft|bottomCenter|bottomRight {
	shared formal Pos pos;
}

shared object topLeft extends Position(horizontalLeft, verticalTop) {
	pos => Pos.\iTOP_LEFT;
}
shared object topCenter extends Position(horizontalCenter, verticalTop) {
	pos => Pos.\iTOP_CENTER;
}
shared object topRight extends Position(horizontalRight, verticalTop) {
	pos => Pos.\iTOP_RIGHT;
}
shared object centerLeft extends Position(horizontalLeft, verticalCenter) {
	pos => Pos.\iCENTER_LEFT;
}
shared object center extends Position(horizontalCenter, verticalCenter) {
	pos => Pos.\iCENTER;
}
shared object centerRight extends Position(horizontalRight, verticalCenter) {
	pos => Pos.\iCENTER_RIGHT;
}
shared object bottomLeft extends Position(horizontalLeft, verticalBottom) {
	pos => Pos.\iBOTTOM_LEFT;
}
shared object bottomCenter extends Position(horizontalCenter, verticalBottom) {
	pos => Pos.\iBOTTOM_CENTER;
}
shared object bottomRight extends Position(horizontalRight, verticalBottom) {
	pos => Pos.\iBOTTOM_RIGHT;
}

shared alias Location => [Float, Float];
shared alias Dimension => [Float, Float];
