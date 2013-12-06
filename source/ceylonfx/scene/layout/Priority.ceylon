import javafx.scene.layout{ JPriority=Priority }
import ceylonfx.application { CeylonNode }

"Enumeration used to determine the grow (or shrink) priority of a given node's
 layout area when its region has more (or less) space available and multiple nodes are competing for that space."
shared abstract class Priority(shared JPriority delegate)
	of always|never|sometimes {}

shared object always extends Priority(JPriority.\iALWAYS) {}
shared object never extends Priority(JPriority.\iNEVER) {}
shared object sometimes extends Priority(JPriority.\iSOMETIMES) {}

"Utility class to allow the use of a [[Priority]] when adding a [[CeylonNode]] to a Parent."
shared class NodeWithConstraint(shared CeylonNode node, shared Priority priority) {}

