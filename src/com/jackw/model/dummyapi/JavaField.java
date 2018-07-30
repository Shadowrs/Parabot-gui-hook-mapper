package com.jackw.model.dummyapi;

import java.util.Arrays;
import jdk.internal.org.objectweb.asm.Type;
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
				(type != null && other.type != null && type == other.type)
				|| (fieldNode != null && other.fieldNode != null && other.fieldNode.desc.equals(fieldNode.desc))
				|| (methodNode != null && other.methodNode != null && other.methodNode.desc.equals(methodNode.desc))
				);
		// TODO field to method.. if needed? senario: Client->client -> click client on tab 2.
	}

	@Override
	public int compareTo(JavaField o) {
		if (this.name.equalsIgnoreCase(o.name))
			return 1;
		return this.name.compareTo(o.name);
	}

	public String getDisplayForApiMethodType() {
		return name+" | "+methodNode.desc;
	}

	public String getDisplayForFieldType() {
		return name+" | "+descToJava(fieldNode.desc);
	}

	// method = ()Ljava/lang/String;
	// field = Ljava/lang/String;
	// field = [I
	private String descToJava(String desc) {
		if (desc.startsWith("(")) { // METHOD
			System.out.println(Arrays.toString(Type.getArgumentTypes(desc)));
			String params = desc.substring(1, desc.lastIndexOf(")")+1);
			System.out.println("[JField] params = "+params);
			if (params.contains(";")) {
				String[] objs = params.split(";");
				System.out.println("\t"+Arrays.toString(objs));
			}
			// TODO argument parsing

			desc = desc.substring(desc.indexOf("L") + 1);
			if (desc.length() > 1) { // object ref / class
				return desc.substring(0, desc.length() - 1); // remove ; at end
			}
		} else { // FIELD
			int arrayDimensions = 0;
			if (desc.startsWith("[")) {
				int curChar = 0;
				while (desc.charAt(curChar++) == "[".charAt(0))
					arrayDimensions++;
			}
			StringBuilder arrayExtension = new StringBuilder();
			while (arrayDimensions-- > 0)
				arrayExtension.append("[]");
			desc = desc.replace("[", "");

			desc = desc.substring(desc.indexOf("L") + 1);
			if (desc.length() > 1) { // object ref / class
				desc = desc.substring(desc.lastIndexOf("/")+1);
				return desc.substring(0, desc.length() - 1) + arrayExtension.toString(); // remove ; at end
			}
			final char c = desc.charAt(0);
			String type = "";
			switch (c) {
				case 'I':
				case 'Z':
				case 'B':
				case 'S':
				case 'C':
					type = "Integer";
					break;
				case 'J':
					type = "Long";
					break;
				case 'F':
					type = "Float";
					break;
				case 'D':
					type = "Double";
					break;
				case 'V': // void, method desc
					type = "Void";
					break;
			}
			return type + arrayExtension.toString();
		}
		return "???="+desc;
	}

	public String getDisplayForASMType() {
		return name+" | "+(methodNode != null ? descToJava(methodNode.desc) : fieldNode != null ? descToJava(fieldNode.desc) : type != null ? type.toString() : "???");
	}

	public boolean typeNotVoid() {
		return methodNode == null || !methodNode.desc.endsWith(")V");
	}
}
