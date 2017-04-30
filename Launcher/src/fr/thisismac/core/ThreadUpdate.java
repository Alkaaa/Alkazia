package fr.thisismac.core;

public class ThreadUpdate extends Thread {

	Panel p;

	public ThreadUpdate(Panel pa) {
		this.p = pa;
	}

	public void run() {
		while (true) {
			p.refresh();
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
		}
	}
}
