package com.jackw.model.dummyapi;

public class JavaField {
	public final String name;
	public final FieldType type;

	public JavaField(String s) {
		this.name = s;
		this.type = FieldType.forString(s);
	}

	public JavaField(String name, FieldType type) {
		this.name = name;
		this.type = type;
	}

	public boolean typeMatch(JavaField other) {
		return other != null && other.type != null && type == other.type;
	}
}
