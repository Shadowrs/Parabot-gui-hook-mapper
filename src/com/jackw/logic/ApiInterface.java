package com.jackw.logic;

import java.util.ArrayList;
import java.util.List;

public class ApiInterface {

	public String name;

	public List<JavaField> entries = new ArrayList<>(List.of(
			new JavaField("getX()"),
			new JavaField("getY()"),
			new JavaField("getZ()")
	));

	public ApiInterface(String name) {
		this.name = name;
	}
}
