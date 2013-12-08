import ceylon.test { test }

test void testPropertiesOfDifferentTypes() {
	ObjectProperty<String> strProperty = ObjectProperty("hi");
	ObjectProperty<Boolean> boolProperty = ObjectProperty(true);
	
	void updateString(Boolean b) {
		if (b) {
			strProperty.set("Natallia");
		} else {
			strProperty.set("Renato");
		}
	}
	
	boolProperty.set(true);
	boolProperty.onChange(updateString);
	
	assert(boolProperty.get == true);
	assert(strProperty.get == "Natallia");
	
	boolProperty.set(false);
	assert(strProperty.get == "Renato");
}

test void testPropertiesOfTheSameType() {
	ObjectProperty<String> strProp1 = ObjectProperty("hi");
	ObjectProperty<String> strProp2 = ObjectProperty("ho");
	
	strProp1.onChange(strProp2.set);
	
	assert(strProp1.get == strProp2.get);
	
	strProp1.set("Bye");
	
	assert(strProp2.get == "Bye");
}

test void testDoubleSynch() {
	ObjectProperty<String> strProp1 = ObjectProperty("hi");
	ObjectProperty<String> strProp2 = ObjectProperty("ho");
	
	strProp1.onChange(strProp2.set);
	strProp2.onChange(strProp1.set);
	
	assert(strProp1.get == strProp2.get);
	
	strProp1.set("Bye");
	
	assert(strProp2.get == "Bye");
	
	strProp2.set("Ok");
	
	assert(strProp1.get == "Ok");
	assert(strProp1.get == strProp2.get);
}

test void testBind() {
	StringProperty strProp1 = ObjectProperty("hi");
	StringProperty strProp2 = ObjectProperty("ho");
	
	bind(strProp1, strProp2);
	
	assert(strProp1.get == strProp2.get);
	
	strProp1.set("Bye");
	
	assert(strProp2.get == "Bye");
}

test void testBindConverting() {
	StringProperty strProperty = ObjectProperty("hi");
	BooleanProperty boolProperty = ObjectProperty(true);
	
	bindConverting(boolProperty, strProperty, (Boolean b) => b then "Natallia" else "Renato");
	
	assert(boolProperty.get == true);
	assert(strProperty.get == "Natallia");
	
	boolProperty.set(false);
	assert(strProperty.get == "Renato");
}

test void testBindBidirectional() {
	StringProperty strProp1 = ObjectProperty("hi");
	StringProperty strProp2 = ObjectProperty("ho");
	
	bindBidirectional(strProp1, strProp2);
	
	assert(strProp1.get == strProp2.get);
	
	strProp1.set("Bye");
	
	assert(strProp2.get == "Bye");
	
	strProp2.set("Ok");
	
	assert(strProp1.get == "Ok");
	assert(strProp1.get == strProp2.get);
}

test void testBindBidirectionalConverting() {
	StringProperty strProp = ObjectProperty("Hi");
	BooleanProperty boolProp = ObjectProperty(true);
	
	bindConvertingBidirectional(strProp, boolProp,
		(Boolean b) => b then "Hi" else "Ho",
		(String s) => "Hi" == s);
	
	assert(boolProp.get == true);
	assert(strProp.get == "Hi");
	
	strProp.set("Ho");
	
	assert(boolProp.get == false);
	assert(strProp.get == "Ho");
	
	boolProp.set(true);
	
	assert(boolProp.get == true);
	assert(strProp.get == "Hi");
}
