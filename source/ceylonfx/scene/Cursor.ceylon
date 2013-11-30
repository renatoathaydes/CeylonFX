import javafx.scene {
    JCursor=Cursor
}

shared class Cursor(shared JCursor cursor) {}
shared object closedHandCursor extends Cursor(JCursor.\iCLOSED_HAND) {}
shared object crosshairCursor extends Cursor(JCursor.\iCROSSHAIR) {}
shared object defaultCursor extends Cursor(JCursor.\iDEFAULT) {}
shared object disappearCursor extends Cursor(JCursor.\iDISAPPEAR) {}
shared object verticalResizeCursor extends Cursor(JCursor.\iV_RESIZE) {}
shared object horizontalResizeCursor extends Cursor(JCursor.\iH_RESIZE) {}
shared object handCursor extends Cursor(JCursor.\iHAND) {}
shared object openHandCursor extends Cursor(JCursor.\iOPEN_HAND) {}
shared object moveCursor extends Cursor(JCursor.\iMOVE) {}
shared object northResizeCursor extends Cursor(JCursor.\iN_RESIZE) {}
shared object northeastResizeCursor extends Cursor(JCursor.\iNE_RESIZE) {}
shared object northwestResizeCursor extends Cursor(JCursor.\iNW_RESIZE) {}
shared object southResizeCursor extends Cursor(JCursor.\iS_RESIZE) {}
shared object southeastResizeCursor extends Cursor(JCursor.\iSE_RESIZE) {}
shared object southwastResizeCursor extends Cursor(JCursor.\iSW_RESIZE) {}
shared object eastResizeCursor extends Cursor(JCursor.\iE_RESIZE) {}
shared object westResizeCursor extends Cursor(JCursor.\iW_RESIZE) {}
shared object textCursor extends Cursor(JCursor.\iTEXT) {}
shared object waitCursor extends Cursor(JCursor.\iWAIT) {}
