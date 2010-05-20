package org.talend.designer.components.thash.io.sortimpl;

class DataContainer {

	long cursorPosition;

	Object object;

	byte[] data;

	public void reset() {
		object = null;
		data = null;
	}
}
