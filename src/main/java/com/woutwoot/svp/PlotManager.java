package com.woutwoot.svp;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.managers.storage.StorageException;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlotManager {

	public List<Plot> plots = new ArrayList<Plot>();
	public WorldGuardPlugin wg = WGBukkit.getPlugin();
	public int plotCount = 1;

	public void removePlot(String plotname) {
		Plot p = this.getPlot(plotname);
		p.getArea().set(Material.AIR);
		wg.getRegionManager(p.getArea().getC1().getWorld()).removeRegion(p.getName());
		plots.remove(p);
		Config.deletePlotFile(plotname);
	}

	public void createAndAddPlot(String plotname, Location c1, Location c2) {
		Plot plot = new Plot(plotname);
		plot.setArea(new Area(c1, c2).set(Material.AIR).setOutline(Material.WOOL));

		ProtectedCuboidRegion pr = new ProtectedCuboidRegion(plot.getName(), convertToSk89qBV(plot.getArea().getC1()), convertToSk89qBV(plot.getArea().getC2()));
		pr.setFlag(DefaultFlag.CREEPER_EXPLOSION, State.DENY);
		pr.setFlag(DefaultFlag.GREET_MESSAGE, ChatColor.translateAlternateColorCodes('&', "&5" + plot.getName() + " &3can be bought for &5" + Main.econ.format(plot.getPrice())));
		pr.setPriority(50);
		wg.getRegionManager(plot.getArea().getC1().getWorld()).addRegion(pr);
		plots.add(plot);
		plotCount++;
	}

	public boolean claimPlot(String plotname, String owner) {
		Plot plot = this.getPlot(plotname);
		if (plot != null) {
			plot.setOwner(owner);
			ProtectedRegion rg = wg.getRegionManager(plot.getArea().getC1().getWorld()).getRegion(plotname);
			rg.setFlag(DefaultFlag.GREET_MESSAGE, ChatColor.translateAlternateColorCodes('&', "&3Entering &5" + plot.getName() + " &3Owner: &5" + plot.getOwner()));
			rg.setFlag(DefaultFlag.FAREWELL_MESSAGE, ChatColor.translateAlternateColorCodes('&', "&3Leaving &5" + plot.getName() + " &3Owner: &5" + plot.getOwner()));
			try {
				wg.getRegionManager(plot.getArea().getC1().getWorld()).save();
			} catch (StorageException e) {
			}
			DefaultDomain domain = new DefaultDomain();
			domain.addPlayer(plot.getOwner());
			for (String friend : plot.getFriends()) {
				domain.addPlayer(friend);
			}
			rg.setOwners(domain);
			plot.getArea().setOutline(Material.AIR);
			return true;
		}
		return false;
	}

	public boolean resetPlot(String plotname) {
		Plot plot = this.getPlot(plotname);
		return resetPlot(plot);
	}
	
	public boolean resetPlot(Plot plot) {
		if (plot != null) {
			plot.setOwner(null);
			ProtectedRegion rg = wg.getRegionManager(plot.getArea().getC1().getWorld()).getRegion(plot.getName());
			rg.setFlag(DefaultFlag.GREET_MESSAGE, ChatColor.translateAlternateColorCodes('&', "&5" + plot.getName() + " &3can be bought for &5" + Main.econ.format(plot.getPrice())));
			Map<Flag<?>, Object> flags = rg.getFlags();
			flags.remove(DefaultFlag.FAREWELL_MESSAGE);
			rg.setFlags(flags);
			DefaultDomain domain = new DefaultDomain();
			rg.setOwners(domain);
			try {
				wg.getRegionManager(plot.getArea().getC1().getWorld()).save();
			} catch (StorageException e) {
				return false;
			}
			plot.getArea().set(Material.AIR).setOutline(Material.WOOL);
			return true;
		}
		return false;
	}

	public void resetAllPlots(){
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plugin, new ResetTask(), 10L);
	}
	
	public Plot getPlot(String plotname) {
		for (Plot p : plots) {
			if (p.getName().equals(plotname)) {
				return p;
			}
		}
		return null;
	}

	public Plot getPlotByLocation(Location loc) {
		for (Plot p : plots) {
			if (p.getArea().isInArea(loc)) {
				return p;
			}
		}
		return null;
	}

	public com.sk89q.worldedit.BlockVector convertToSk89qBV(Location location) {
		return new com.sk89q.worldedit.BlockVector(location.getX(), location.getY(), location.getZ());
	}

	public void updateAllPlots() {
		List<World> worlds = new ArrayList<World>();
		for(Plot plot : plots){
			this.updatePlot(plot, false);
			if(!worlds.contains(plot.getArea().getC1().getWorld())){
				worlds.add(plot.getArea().getC1().getWorld());
			}
		}
		for(World w : worlds){
			try {
				wg.getRegionManager(w).save();
			} catch (StorageException e) {
			}
		}
	}

	public void updatePlot(Plot plot, boolean save) {
		if (plot != null && plot.getOwner() == null) {
			ProtectedRegion rg = wg.getRegionManager(plot.getArea().getC1().getWorld()).getRegion(plot.getName());
			rg.setFlag(DefaultFlag.GREET_MESSAGE, ChatColor.translateAlternateColorCodes('&', "&5" + plot.getName() + " &3can be bought for &5" + Main.econ.format(plot.getPrice())));
			Map<Flag<?>, Object> flags = rg.getFlags();
			flags.remove(DefaultFlag.FAREWELL_MESSAGE);
			rg.setFlags(flags);
			if(save){
				try {
					wg.getRegionManager(plot.getArea().getC1().getWorld()).save();
				} catch (StorageException e) {
				}
			}
		}else if (plot != null) {
			ProtectedRegion rg = wg.getRegionManager(plot.getArea().getC1().getWorld()).getRegion(plot.getName());
			rg.setFlag(DefaultFlag.GREET_MESSAGE, ChatColor.translateAlternateColorCodes('&', "&3Entering &5" + plot.getName() + " &3Owner: &5" + plot.getOwner()));
			rg.setFlag(DefaultFlag.FAREWELL_MESSAGE, ChatColor.translateAlternateColorCodes('&', "&3Leaving &5" + plot.getName() + " &3Owner: &5" + plot.getOwner()));
			if(save){
				try {
					wg.getRegionManager(plot.getArea().getC1().getWorld()).save();
				} catch (StorageException e) {
				}
			}
		}
	}

}
