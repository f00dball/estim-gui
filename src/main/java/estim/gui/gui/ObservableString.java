package estim.gui.gui;

import java.util.Observable;

public class ObservableString extends Observable {
	protected String data;

	public String getData() {
		return data;
	}

	public void setData(String data) {
		setChanged();
		this.data = data;
	}

	@Override
	public String toString() {
		return "ObservableString [data=" + data + "]";
	}
	
}
