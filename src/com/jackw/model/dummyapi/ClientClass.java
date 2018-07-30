package com.jackw.model.dummyapi;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.parabot.api.output.Logger;

public class ClientClass implements Comparable<ClientClass> {
	public final String name;
	private final List<JavaField> fields;
	private final ClassNode node;

	public ClientClass(String s, List<JavaField> fields) {
		this.name = s;
		this.fields = fields.stream().sorted().collect(Collectors.toList());
		node = null;
	}

	public ClientClass(ClassNode value) {
		int lastDir = value.name.lastIndexOf("/");
		this.name = value.name.substring(lastDir == -1 ? 0 : lastDir + 1);
		this.fields = asmToDummy(value).stream().sorted().collect(Collectors.toList());
		this.node = value;
	}

	public int fieldCount() {
		return fields != null ? fields.size() : node != null ? node.fields.size() : 0;
	}

	public int methodCountASM() {
		return node == null ? 0 : node.methods.size();
	}

	public int interfaceCountASM() {
		return node == null ? 0 : node.interfaces.size();
	}

	public ObservableList<JavaField> getFields() {
		if (fields.size() > 100) {
			Logger.warning("ClientClass", "Putting "+fields.size()+" fields into a List -- this will be slow! Please wait...");
		}
		return FXCollections.observableArrayList(fields);
	}

	private List<JavaField> asmToDummy(ClassNode node) {
		ArrayList<JavaField> real = new ArrayList<>(node.fields.size());
		node.fields.forEach(f -> real.add(new JavaField((FieldNode)f)));
		return real;
	}

	@Override
	public int compareTo(ClientClass o) {
		if (this.name.equalsIgnoreCase(o.name))
			return 1;
		return this.name.compareTo(o.name);
	}
}
