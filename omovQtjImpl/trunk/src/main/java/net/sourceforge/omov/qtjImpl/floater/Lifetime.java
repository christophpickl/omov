package net.sourceforge.omov.qtjImpl.floater;

class Lifetime {

	private int value = 10;
	
//	public synchronized void setValue(int value) {
//		this.value = value;
//	}
	public synchronized int getValue() {
		return this.value;
	}
	public synchronized void decrease() {
		this.value--;
	}
	/** sets to MAX */
	public synchronized void reset() {
		this.value = 10;
	}
	public String toString() {
		return "" + value;
	}
	public synchronized boolean isMinValue() {
		return this.value == 0;
	}
}
