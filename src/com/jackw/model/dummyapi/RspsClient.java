package com.jackw.model.dummyapi;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RspsClient {
	public File client;
	public List<ClientClass> entries = new ArrayList<>(List.of(
			// a = Player
			new ClientClass("a.class", new ArrayList<>(List.of(
					new JavaField("anInt1"), // dummy
					new JavaField("anInt2"), // dummy
					new JavaField("aString1"), // name
					new JavaField("RSPlayerAppearance")
			))),
			// Character = b
			new ClientClass("b.class", new ArrayList<>(List.of(
					new JavaField("anInt3"), // x
					new JavaField("anInt5"), // y
					new JavaField("anInt6"), // z
					new JavaField("aString2"), // dummy
					new JavaField("aLong1"), // dummy
					new JavaField("Model")
			))),
			// NPC = c
			new ClientClass("c.class", new ArrayList<>(List.of(
					new JavaField("aLong2"), // dummy
					new JavaField("NpcDefinition"),
					new JavaField("aString4") // examine
			))),
			// dummy
			new ClientClass("d.class", new ArrayList<>(List.of(
					new JavaField("aLong3") // dummy
			))),
			// dummy
			new ClientClass("e.class", new ArrayList<>(List.of(
					new JavaField("anInt4"),
					new JavaField("aString3"),
					new JavaField("aLong4")
			)))
	));

	public RspsClient(File f) {
		this.client = f;
	}
}
