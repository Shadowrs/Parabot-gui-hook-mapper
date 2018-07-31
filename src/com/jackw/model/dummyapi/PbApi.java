package com.jackw.model.dummyapi;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.objectweb.asm.tree.ClassNode;
import org.parabot.core.classpath.ClassPath;

public class PbApi {
	public File apiJar;
	public final List<ApiInterface> interfaces;
	private final ClassPath classPath;

	public PbApi(File file) {
		this.apiJar = file;
		classPath = null;
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
				))),
				new ApiInterface("Item", new ArrayList<>(List.of(
						new JavaField("getId()", FieldType.INT)
				)))
		)).stream().sorted().collect(Collectors.toList());
	}

	public PbApi(File file, String accessorRoot) {
		this.apiJar = file;
		classPath = new ClassPath(true);
		classPath.addJar(file);
		System.out.println("API "+file.getAbsolutePath()+" has "+classPath.classes.size()+" classes");
		System.out.println(
				Arrays.toString(classPath.classes.entrySet().stream().filter(e -> e.getKey().startsWith("org/rev317/min/"))
				.map(e -> e.getKey()+" = "+e.getValue().name+"|"+e.getValue().fields.size()+"\n")
				.collect(Collectors.toList())
				.toArray(new String[0]))
		);
		List<Map.Entry<String, ClassNode>> accessors = classPath.classes.entrySet().stream().
				filter(e -> e.getKey().startsWith(accessorRoot))
				.collect(Collectors.toList());
		interfaces = accessors.stream().map(e -> new ApiInterface(e.getKey().replace(accessorRoot, ""),
				e.getValue())).sorted().collect(Collectors.toList());
	}
}
