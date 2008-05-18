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
public class FadeOutThread extends Thread {

	private final Lifetime lifetime;
	private final Opacity opacity;
	private boolean isShouldStop = false;
	
	
	public FadeOutThread(Lifetime lifetime, Opacity opacity) {
//		System.out.println("new FadeOutThread()");
		this.lifetime = lifetime;
		this.opacity = opacity;
	}
	
	
	public void shouldStop() {
		this.isShouldStop = true;
	}
	
	public void run() {
		while(isShouldStop == false && (lifetime.isMinValue() == false || opacity.isMinValue() == false)) {
//			System.out.println("thread: lifetime="+lifetime+"; opacity="+opacity);
			if(lifetime.isMinValue() == false) {
				lifetime.decrease();
			} else {
				assert(opacity.isMinValue() == false);
				opacity.decrease();
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
//		System.out.println("thread DIES!");
	}
}
