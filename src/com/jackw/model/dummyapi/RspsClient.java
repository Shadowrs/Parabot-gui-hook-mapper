package com.jackw.model.dummyapi;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RspsClient {
	public File client;
	public List<ClientEntry> entries = new ArrayList<>(List.of(
			new ClientEntry("a.class"),
			new ClientEntry("b.class"),
			new ClientEntry("c.class"),
			new ClientEntry("d.class"),
			new ClientEntry("e.class")
	));

	public RspsClient(File f) {
		this.client = f;
	}
}
