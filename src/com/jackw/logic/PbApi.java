package com.jackw.logic;

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
				new ApiInterface("Player"),
				new ApiInterface("Character"),
				new ApiInterface("NPC")
		));
	}
}
