package com.woutwoot.svp;

import org.bukkit.Material;

public class SetTask implements Runnable {

	Material mat;
	Area area;
	
	public SetTask(Material matToSet, Area areaToSet){
		this.mat = matToSet;
		this.area = areaToSet;
	}
	
	@Override
	public void run() {
		area.setOutline(mat);
	}

}
