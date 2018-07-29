package com.jackw.model.dummyapi;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.parabot.core.classpath.ClassPath;

public class RspsClient {
	public File clientFile;
	public final List<ClientClass> entries;
	private final ClassPath classPath;

	public RspsClient(File f) {
		this.clientFile = f;
		classPath = null;
		entries = new ArrayList<>(List.of(
				// a = Player
				new ClientClass("a.class", new ArrayList<>(List.of(
						new JavaField("anInt1"), // dummy
						new JavaField("anInt2"), // dummy
						new JavaField("aString1"), // name
						new JavaField("aString4"), // dummy
						new JavaField("RSPlayerAppearance"),
						new JavaField("aStringArray1") // dummy
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
		)).stream().sorted().collect(Collectors.toList());
	}

	public RspsClient(boolean dummy, File clientFile) {
		this.clientFile = clientFile;
		classPath = new ClassPath(true);
		classPath.addJar(clientFile);
		entries = classPath.classes.values().stream().map(v -> new ClientClass((v))).sorted().collect(Collectors.toList());
	}
}
