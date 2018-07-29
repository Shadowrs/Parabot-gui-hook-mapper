package com.jackw.model.dummyapi;

import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

/**
 * A class that holds an internal "field" "method" or "interface", and has methods to convert those types
 * into Strings shown in TableView Columns or ListViews.
 */
public class JavaField implements Comparable<JavaField> {
	public final String name;
	private final FieldType type;
	private final FieldNode fieldNode;
	private final MethodNode methodNode;

	public JavaField(String s) {
		this.name = s;
		this.type = FieldType.forString(s);
		fieldNode = null;
		methodNode = null;
	}

	public JavaField(String name, FieldType type) {
		this.name = name;
		this.type = type;
		fieldNode = null;
		methodNode = null;
	}

	public JavaField(FieldNode f) {
		this.name = f.name;
		this.type = null;
		fieldNode = f;
		methodNode = null;
	}

	public JavaField(MethodNode f) {
		this.name = f.name;
		this.type = null;
		fieldNode = null;
		methodNode = f;
	}

	public boolean typeMatch(JavaField other) {
		return other != null && (
				(other.type != null && type == other.type)
				|| (other.fieldNode != null && other.fieldNode.desc.equals(fieldNode.desc))
				|| (other.methodNode != null && other.methodNode.desc.equals(methodNode.desc))
				);
	}

	@Override
	public int compareTo(JavaField o) {
		if (this.name.equalsIgnoreCase(o.name))
			return 1;
		return this.name.compareTo(o.name);
	}
}
