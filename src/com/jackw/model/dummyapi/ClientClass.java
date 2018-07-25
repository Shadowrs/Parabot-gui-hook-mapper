package com.jackw.model.dummyapi;

import java.util.List;

public class ClientClass {
	public final String name;
	public final List<JavaField> fields;

	public ClientClass(String s, List<JavaField> fields) {
		this.name = s;
		this.fields = fields;
	}

}
