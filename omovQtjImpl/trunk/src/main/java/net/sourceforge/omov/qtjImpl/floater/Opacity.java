package net.sourceforge.omov.qtjImpl.floater;

class Opacity {

	private int value = 0;
	
	private final QtjFloater listener;
	
	public Opacity(QtjFloater listener) {
		this.listener = listener;
	}
	
//	public synchronized void setValue(int value) {
//		this.value = value;
//	}
	public synchronized int getValue() {
		return this.value;
	}
	public synchronized void decrease() {
		this.value -= 10;
		listener.opacityChanged(this.value);
	}
	/** sets to MAX */
	public synchronized void reset() {
		this.value = 100;
		listener.opacityChanged(this.value);
	}
	public String toString() {
		return "" + value;
	}
	public synchronized boolean isMinValue() {
		return this.value == 0;
	}
}
