import javafx.scene.text {
    JFont=Font,
    FontPosture,
    FontWeight
}

shared class Font(shared JFont font) {
    shared String family => font.family;
    shared Float size => font.size;
    shared actual Boolean equals(Object that) {
        if (is Font that) {
            return font.equals(that.font);
        }
        else {
            return false;
        }
    }
    hash => font.hash;
    string => font.name;
}

shared Font font(
    String family = "System",
    Float size = 12.0,
    FontWeight weight = FontWeight.\iNORMAL,
    FontPosture posture = FontPosture.\iREGULAR
)
        => Font(JFont.font(family, weight, posture, size));

shared Font defaultFont => Font(JFont.default);