package com.woutwoot.svp;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.*;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.Random;

public class Area {

	private Location c1;
	private Location c2;

	/**
	 * @param c1
	 *            Location of corner1 of the area
	 * @param c2
	 *            Location of corner2 of the area
	 */
	public Area(Location c1, Location c2) {
		this.c1 = c1;
		this.c2 = c2;
	}

	/**
	 * @return the c1
	 */
	public Location getC1() {
		return c1;
	}

	/**
	 * @param c1
	 *            the c1 to set
	 */
	public void setC1(Location c1) {
		this.c1 = c1;
	}

	/**
	 * @return the c2
	 */
	public Location getC2() {
		return c2;
	}

	/**
	 * @param c2
	 *            the c2 to set
	 */
	public void setC2(Location c2) {
		this.c2 = c2;
	}

	public boolean isInArea(Location loc) {
		double xMin = 0;
		double xMax = 0;
		double yMin = 0;
		double yMax = 0;
		double zMin = 0;
		double zMax = 0;
		double x = loc.getX();
		double y = loc.getY();
		double z = loc.getZ();
		xMin = Math.min(getC1().getX(), getC2().getX());
		xMax = Math.max(getC1().getX(), getC2().getX());
		yMin = Math.min(getC1().getY(), getC2().getY());
		yMax = Math.max(getC1().getY(), getC2().getY());
		zMin = Math.min(getC1().getZ(), getC2().getZ());
		zMax = Math.max(getC1().getZ(), getC2().getZ());
		return (x >= xMin && x <= xMax && y >= yMin && y <= yMax && z >= zMin && z <= zMax);
	}

	public Area setOutline(Material m) {
		double xMin = 0;
		double xMax = 0;
		double yMin = 0;
		double yMax = 0;
		double zMin = 0;
		double zMax = 0;
		xMin = Math.min(c1.getX(), c2.getX());
		xMax = Math.max(c1.getX(), c2.getX());
		yMin = Math.min(c1.getY(), c2.getY());
		yMax = Math.max(c1.getY(), c2.getY());
		zMin = Math.min(c1.getZ(), c2.getZ());
		zMax = Math.max(c1.getZ(), c2.getZ());
		World w = c1.getWorld();
		for (double x = xMin; x <= xMax; x++) {
			for (double y = yMin; y <= yMax; y++) {
				for (double z = zMin; z <= zMax; z++) {
					if (x == xMin && y == yMin) {
						w.getBlockAt(new Location(w, x, y, z)).setType(m);
					} else if (x == xMin && y == yMax) {
						w.getBlockAt(new Location(w, x, y, z)).setType(m);
					} else if (x == xMax && y == yMin) {
						w.getBlockAt(new Location(w, x, y, z)).setType(m);
					} else if (x == xMax && y == yMax) {
						w.getBlockAt(new Location(w, x, y, z)).setType(m);
					} else if (x == xMin && z == zMin) {
						w.getBlockAt(new Location(w, x, y, z)).setType(m);
					} else if (x == xMin && z == zMax) {
						w.getBlockAt(new Location(w, x, y, z)).setType(m);
					} else if (x == xMax && z == zMin) {
						w.getBlockAt(new Location(w, x, y, z)).setType(m);
					} else if (x == xMax && z == zMax) {
						w.getBlockAt(new Location(w, x, y, z)).setType(m);
					} else if (z == zMin && y == yMin) {
						w.getBlockAt(new Location(w, x, y, z)).setType(m);
					} else if (z == zMin && y == yMax) {
						w.getBlockAt(new Location(w, x, y, z)).setType(m);
					} else if (z == zMax && y == yMin) {
						w.getBlockAt(new Location(w, x, y, z)).setType(m);
					} else if (z == zMax && y == yMax) {
						w.getBlockAt(new Location(w, x, y, z)).setType(m);
					}
				}
			}
		}
		return this;
	}

	public boolean isEmpty() {
		double xMin = 0;
		double xMax = 0;
		double yMin = 0;
		double yMax = 0;
		double zMin = 0;
		double zMax = 0;
		xMin = Math.min(c1.getX(), c2.getX());
		xMax = Math.max(c1.getX(), c2.getX());
		yMin = Math.min(c1.getY(), c2.getY());
		yMax = Math.max(c1.getY(), c2.getY());
		zMin = Math.min(c1.getZ(), c2.getZ());
		zMax = Math.max(c1.getZ(), c2.getZ());
		World w = c1.getWorld();
		boolean res = false;
		for (double x = xMin; x <= xMax; x++) {
			for (double y = yMin; y <= yMax; y++) {
				for (double z = zMin; z <= zMax; z++) {
					if (w.getBlockAt(new Location(w, x, y, z)).getType() == Material.AIR 
							|| w.getBlockAt(new Location(w, x, y, z)).getType() == Material.DIRT 
							|| w.getBlockAt(new Location(w, x, y, z)).getType() == Material.COBBLESTONE
							|| w.getBlockAt(new Location(w, x, y, z)).getType() == Material.WOOL) {
						res = true;
					} else {
						return false;
					}
				}
			}
		}
		return res;
	}

	public Area set(Material m) {
		double xMin = 0;
		double xMax = 0;
		double yMin = 0;
		double yMax = 0;
		double zMin = 0;
		double zMax = 0;
		xMin = Math.min(c1.getX(), c2.getX());
		xMax = Math.max(c1.getX(), c2.getX());
		yMin = Math.min(c1.getY(), c2.getY());
		yMax = Math.max(c1.getY(), c2.getY());
		zMin = Math.min(c1.getZ(), c2.getZ());
		zMax = Math.max(c1.getZ(), c2.getZ());
		World w = c1.getWorld();
		for (double x = xMin; x <= xMax; x++) {
			for (double y = yMin; y <= yMax; y++) {
				for (double z = zMin; z <= zMax; z++) {
					w.getBlockAt(new Location(w, x, y, z)).setType(m);
				}
			}
		}
		return this;
	}

	public double getSize(){
		double xMin = 0;
		double xMax = 0;
		double yMin = 0;
		double yMax = 0;
		double zMin = 0;
		double zMax = 0;
		double x = 0;
		double y = 0;
		double z = 0;
		xMin = Math.min(c1.getX(), c2.getX());
		xMax = Math.max(c1.getX(), c2.getX());
		yMin = Math.min(c1.getY(), c2.getY());
		yMax = Math.max(c1.getY(), c2.getY());
		zMin = Math.min(c1.getZ(), c2.getZ());
		zMax = Math.max(c1.getZ(), c2.getZ());
		x = xMax - xMin;
		y = yMax - yMin;
		z = zMax - zMin;
		return (Math.abs(x) * Math.abs(y) * Math.abs(z));
	}

	public void doeNeZiekeFlashShow(){
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plugin, new SetTask(Material.GOLD_BLOCK, this), 1L);
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plugin, new SetTask(Material.DIAMOND_BLOCK, this), 10L);
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plugin, new SetTask(Material.IRON_BLOCK, this), 20L);
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plugin, new SetTask(Material.AIR, this), 30L);
		
		Firework fw = (Firework) c1.getWorld().spawnEntity(this.getCenter(), EntityType.FIREWORK);
		FireworkMeta fwm = fw.getFireworkMeta();
        Random r = new Random(); 
        int rt = r.nextInt(5) + 1;
        Type type = Type.BALL;     
        if (rt == 1) type = Type.BALL;
        if (rt == 2) type = Type.BALL_LARGE;
        if (rt == 3) type = Type.BURST;
        if (rt == 4) type = Type.CREEPER;
        if (rt == 5) type = Type.STAR;
        int r1i = r.nextInt(17) + 1;
        int r2i = r.nextInt(17) + 1;
        Color c1 = getColor(r1i);
        Color c2 = getColor(r2i);
        FireworkEffect effect = FireworkEffect.builder().flicker(r.nextBoolean()).withColor(c1).withFade(c2).with(type).trail(r.nextBoolean()).build();
        fwm.addEffect(effect);
        int rp = r.nextInt(2) + 1;
        fwm.setPower(rp);
        fw.setFireworkMeta(fwm);        
	}
	
	private Location getCenter() {
		double avgX = (c1.getBlockX() + c2.getBlockX()) / 2;
		double avgY = (c1.getBlockY() + c2.getBlockY()) / 2;
		double avgZ = (c1.getBlockZ() + c2.getBlockZ()) / 2;
		return new Location(c1.getWorld(), avgX, avgY, avgZ);
	}

	private Color getColor(int i) {
		Color c = null;
		if(i==1){
		c=Color.AQUA;
		}
		if(i==2){
		c=Color.BLACK;
		}
		if(i==3){
		c=Color.BLUE;
		}
		if(i==4){
		c=Color.FUCHSIA;
		}
		if(i==5){
		c=Color.GRAY;
		}
		if(i==6){
		c=Color.GREEN;
		}
		if(i==7){
		c=Color.LIME;
		}
		if(i==8){
		c=Color.MAROON;
		}
		if(i==9){
		c=Color.NAVY;
		}
		if(i==10){
		c=Color.OLIVE;
		}
		if(i==11){
		c=Color.ORANGE;
		}
		if(i==12){
		c=Color.PURPLE;
		}
		if(i==13){
		c=Color.RED;
		}
		if(i==14){
		c=Color.SILVER;
		}
		if(i==15){
		c=Color.TEAL;
		}
		if(i==16){
		c=Color.WHITE;
		}
		if(i==17){
		c=Color.YELLOW;
		}
		 
		return c;
		}

	public void regenerate() {
		Selection selection = new CuboidSelection(this.getC1().getWorld(), this.getC1(), this.getC2());
		try {
			Region region = selection.getRegionSelector().getRegion();
			region.getWorld().regenerate(region, WorldEdit.getInstance().getEditSessionFactory().getEditSession(region.getWorld(), -1));
		} catch (IncompleteRegionException e) {
		}
	}
}
