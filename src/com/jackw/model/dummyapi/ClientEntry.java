package com.jackw.model.dummyapi;

import java.util.ArrayList;
import java.util.List;

public class ClientEntry {
	public final String name;
	public final FieldType type;

	public List<JavaField> fields = new ArrayList<>(List.of(
			new JavaField("anInt1"),
			new JavaField("anInt2"),
			new JavaField("aString1"),
			new JavaField("aLong1"),
			new JavaField("aByteArray1"),
			new JavaField("aChar1")
	));

	public ClientEntry(String s) {
		this.name = s;
		this.type = FieldType.forString(s);
	}

	public ClientEntry(String s, FieldType type) {
		this.name = s;
		this.type = type;
	}
}
