package com.jackw.model.dummyapi;

import org.objectweb.asm.Type;
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
		return name+argsToString()+" | "+ descToTypeOnly(methodNode.desc);
	}

	private String argsToString() {
		return argsToString(Type.getArgumentTypes(methodNode.desc));
	}

	public String getDisplayForFieldType() {
		return name+" | "+ descToTypeOnly(fieldNode.desc);
	}

	private String descToTypeOnly(String desc) {
		if (desc.startsWith("(")) { // METHOD
			desc = desc.substring(desc.lastIndexOf(")")+1);
		}
		int arrayDimensions = 0;
		if (desc.startsWith("[")) {
			int curChar = 0;
			while (desc.charAt(curChar++) == "[".charAt(0)) {
				arrayDimensions++;
			}
		}
		StringBuilder arrayExtension = new StringBuilder();
		while (arrayDimensions-- > 0) {
			arrayExtension.append("[]");
		}
		desc = desc.replace("[", "");

		desc = desc.substring(desc.indexOf("L") + 1);
		if (desc.length() > 1) { // object ref / class
			desc = desc.substring(desc.lastIndexOf("/")+1);
			return desc.substring(0, desc.length() - 1) + arrayExtension.toString(); // remove ; at end
		}
		final char c = desc.charAt(0);
		String type = "";
		switch (c) {
			case 'I': // INT
				type = "Int";
				break;
			case 'Z': // BOOLEAN
				type = "Boolean";
				break;
			case 'B': // byte
				type = "Byte";
				break;
			case 'S': // short
				type = "Short";
				break;
			case 'C': // char (see Type.BOOLEAN_TYPE ASM sources)
				type = "Char";
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

	private String argsToString(Type[] argumentTypes) {
		if (argumentTypes == null || argumentTypes.length == 0)
			return "";
		StringBuilder sb = new StringBuilder();
		sb.append(" (");
		for (Type a : argumentTypes) {
			String desc = a.getDescriptor();
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
			String arrayEx = arrayExtension.toString();

			String type = "";
			switch (desc.charAt(0)) {
				case 'I': // INT
					type = "int";
					break;
				case 'Z': // BOOLEAN
					type = "boolean";
					break;
				case 'B': // byte
					type = "byte";
					break;
				case 'S': // short
					type = "short";
					break;
				case 'C': // char (see Type.BOOLEAN_TYPE ASM sources)
					type = "char";
					break;
				case 'J':
					type = "long";
					break;
				case 'F':
					type = "float";
					break;
				case 'D':
					type = "double";
					break;
				case 'V':
					type = "void";
					break;
				case 'L':
					type = desc.substring(1, desc.lastIndexOf(";"));
					if (type.contains("/")) // in a dir
						type = type.substring(type.lastIndexOf("/")+1);
					break;
			}
			sb.append(type+arrayEx+", ");
		}
		/*System.out.println("Types: "+Arrays.toString(Arrays.stream(argumentTypes)
				.map(a -> "\n"+a.toString()+" , "+a.getDescriptor()+" , "+a.getClassName()+" , "+a.getSort())
				.collect(Collectors.toList())
				.toArray(new String[0])));*/

		sb.append(")");
		return sb.toString().replace(", )", ")");
	}

	public String getDisplayForASMType() {
		return name+" | "+(methodNode != null ? descToTypeOnly(methodNode.desc) : fieldNode != null ? descToTypeOnly(fieldNode.desc) : type != null ? type.toString() : "???");
	}

	public boolean typeNotVoid() {
		return methodNode == null || !methodNode.desc.endsWith(")V");
	}
}
