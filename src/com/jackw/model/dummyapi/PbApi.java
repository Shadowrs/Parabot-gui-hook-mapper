package com.jackw.model.dummyapi;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PbApi {
	private ApiData apiData;
	public File apiJar;
	public List<ApiInterface> interfaces;

	public PbApi(ApiData apiData, File file) {
		this.apiData = apiData;
		this.apiJar = file;
		interfaces = new ArrayList<>(List.of(
				new ApiInterface("Player", new ArrayList<>(List.of(
						new JavaField("getName()", FieldType.STRING),
						new JavaField("getAppearance()")
				))),
				new ApiInterface("Character", new ArrayList<>(List.of(
						new JavaField("getX()", FieldType.INT),
						new JavaField("getY()", FieldType.INT),
						new JavaField("getZ()", FieldType.INT),
						new JavaField("getModel()")
				))),
				new ApiInterface("NPC", new ArrayList<>(List.of(
						new JavaField("getNpcDefinition()"),
						new JavaField("getExamine()", FieldType.STRING)
				)))
		));
	}
}
