package com.jackw.model.dummyapi;

import java.util.ArrayList;
import java.util.List;

public class ApiInterface {

	public final String name;

	public final List<JavaField> fields;

	public ApiInterface(String name, ArrayList<JavaField> fields)
	{
		this.name = name;
		this.fields = fields;
	}
}
