import javafx.scene { JDepthTest=DepthTest }

"This enum defines the possible states for the depthTest flag in node."
shared abstract class DepthTest(shared JDepthTest delegate)
		of disableDepthTest|enableDepthTest|inheritDepthTest {}

"Specifies that depth testing is disabled."
object disableDepthTest extends DepthTest(JDepthTest.\iDISABLE) {}

"Specifies that depth testing is enabled."
object enableDepthTest extends DepthTest(JDepthTest.\iENABLE) {}

"Specifies that the depth testing state is inherited from the parent."
object inheritDepthTest extends DepthTest(JDepthTest.\iINHERIT) {}
