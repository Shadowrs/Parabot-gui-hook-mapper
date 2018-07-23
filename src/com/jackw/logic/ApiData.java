package com.jackw.logic;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ApiData {

	public class PbApi {
		public File apiJar;
		public List<ApiInterface> interfaces = new ArrayList<>(List.of(
				new ApiInterface(),
				new ApiInterface(),
				new ApiInterface()
		));

		public PbApi(File file) {
			this.apiJar = file;
		}
	}

	public class JavaField {
		public String name;

		public JavaField(String s) {
			this.name = s;
		}
		// TODO types access etc etc
	}

	public class ApiInterface {
		public List<JavaField> entries = new ArrayList<>(List.of(
				new JavaField("getX()"),
				new JavaField("getY()"),
				new JavaField("getZ()")
		));
	}

	public PbApi pbApi = new PbApi(new File("api.jar"));
	public RspsClient client = new RspsClient("dreamscape.jar");

	public class ClientEntry {
		public String name;

		public ClientEntry(String s) {
			this.name = s;
		}
	}

	public class RspsClient {
		public String name;
		public List<ClientEntry> entries = new ArrayList<>(List.of(
				new ClientEntry("a.class"),
				new ClientEntry("b.class"),
				new ClientEntry("c.class"),
				new ClientEntry("d.class"),
				new ClientEntry("e.class")
		));

		public RspsClient(String s) {
			this.name = s;
		}
	}

}
