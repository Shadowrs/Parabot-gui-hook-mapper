package com.jackw.model.dummyapi;

public enum FieldType {
	INT, LONG, STRING, BYTE, CHAR,
	INT_ARRAY, LONG_ARRAY, STRING_ARRAY, BYTE_ARRAY, CHAR_ARRAY,
	CLASS;

	public static FieldType forString(String s) {
		FieldType type;
		if (s.startsWith("anInt"))
			type  = s.startsWith("anIntArray") ? FieldType.INT_ARRAY : FieldType.INT;
		else if (s.startsWith("aLong"))
			type  = s.startsWith("aLongArray") ? FieldType.LONG_ARRAY : FieldType.LONG;
		else if (s.startsWith("aString"))
			type  = s.startsWith("aStringArray") ? FieldType.STRING_ARRAY : FieldType.STRING;
		else if (s.startsWith("aByte"))
			type  = s.startsWith("aByteArray") ? FieldType.BYTE_ARRAY : FieldType.BYTE;
		else if (s.startsWith("aChar"))
			type  = s.startsWith("aCharArray") ? FieldType.CHAR_ARRAY : FieldType.CHAR;
		else
			type = FieldType.CLASS;
		return  type;
	}
}
