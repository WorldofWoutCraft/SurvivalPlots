package com.woutwoot.svp;

public class ResetTask implements Runnable {
	
	@Override
	public void run() {
		for(Plot p : Main.pm.plots){
			Main.pm.resetPlot(p);
		}
	}

}
