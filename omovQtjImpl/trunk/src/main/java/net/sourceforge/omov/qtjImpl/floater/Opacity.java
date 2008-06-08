/*
 * OurMovies - Yet another movie manager
 * Copyright (C) 2008 Christoph Pickl (christoph_pickl@users.sourceforge.net)
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.sourceforge.omov.qtjImpl.floater;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
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
		listener.opacityChanged(this.value, false);
	}
	/** sets to MAX */
	public synchronized void reset() {
		this.value = 100;
		listener.opacityChanged(this.value, false);
	}
	public String toString() {
		return "" + value;
	}
	public synchronized boolean isMinValue() {
		return this.value == 0;
	}
}
