package com.jackw.logic;

import java.util.ArrayList;
import java.util.List;

public class ApiInterface {
	private ApiData apiData;
	public List<JavaField> entries = new ArrayList<>(List.of(
			new JavaField("getX()"),
			new JavaField("getY()"),
			new JavaField("getZ()")
	));

	public ApiInterface(ApiData apiData) {
		this.apiData = apiData;
	}
}
