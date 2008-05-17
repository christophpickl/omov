package net.sourceforge.omov.qtjImpl.floater;

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
