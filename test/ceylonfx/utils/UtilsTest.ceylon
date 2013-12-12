import ceylon.test {
	...
}


test void canRunInFxThread() {

}

test void testNullSafeEquals() {
	assertTrue(nullSafeEquals(null, null));
	assertTrue(nullSafeEquals(1, 1));
	assertTrue(nullSafeEquals("", ""));
	assertTrue(nullSafeEquals(false, false));
	assertTrue(nullSafeEquals(0.0, 0.0));
	
	assertFalse(nullSafeEquals(0, null));
	assertFalse(nullSafeEquals(null, 0));
	assertFalse(nullSafeEquals(0, 1));
	assertFalse(nullSafeEquals("", "a"));
	assertFalse(nullSafeEquals(0.1, 1.0));
}
