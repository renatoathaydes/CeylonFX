import javafx.geometry {
    HPos,
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

shared alias Position => [HorizontalPosition, VerticalPosition];
