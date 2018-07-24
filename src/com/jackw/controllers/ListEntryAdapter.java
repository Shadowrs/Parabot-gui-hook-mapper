package com.jackw.controllers;

import javafx.beans.property.SimpleStringProperty;

public class ListEntryAdapter<T> {
	private final SimpleStringProperty e; // TODO could actually just be a string i think
	// no need for this wrapper
	public T o;

	ListEntryAdapter(String display, T o) {
		e = new SimpleStringProperty(display);
		this.o = o;
	}

	public String getE() {
		return e.get();
	}

	public SimpleStringProperty eProperty() {
		return e;
	}

	public void setE(String e) {
		this.e.set(e);
	}

	public T getO() {
		return o;
	}

	public void setO(T o) {
		this.o = o;
	}
}
