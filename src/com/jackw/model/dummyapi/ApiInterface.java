package com.jackw.model.dummyapi;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.parabot.api.output.Logger;

public class ApiInterface implements Comparable<ApiInterface> {

	public final String name;
	private final List<JavaField> methods;
	private final ClassNode node;

	public ApiInterface(String name, ArrayList<JavaField> methods)
	{
		this.name = name;
		this.methods = methods.stream().sorted().collect(Collectors.toList());
		node = null;
	}

	public ApiInterface(String replace, ClassNode value) {
		this.name = replace;
		methods = asmToDummy(value).stream().sorted().collect(Collectors.toList());
		this.node = value;
	}

	public int fieldCount() {
		return methods != null ? methods.size() : node != null ? node.fields.size() : 0;
	}

	public List<JavaField> getMethods(Predicate<JavaField> predicate) {
		return predicate == null ? methods : methods.stream().filter(predicate).collect(Collectors.toList());
	}

	public List<JavaField> getMethods() {
		return getMethods(null);
	}

	private List<JavaField> asmToDummy(ClassNode node) {
		Logger.debug("ApiInterface", node.name+" counts: "+node.fields.size()+" | "+node.interfaces.size()+" | "+node.methods.size());
		return ((List<MethodNode>)node.methods).stream().map(m -> new JavaField((MethodNode)m)).sorted().collect(Collectors.toList());
	}

	@Override
	public int compareTo(ApiInterface o) {
		if (this.name.equalsIgnoreCase(o.name))
			return 1;
		return this.name.compareTo(o.name);
	}
}
