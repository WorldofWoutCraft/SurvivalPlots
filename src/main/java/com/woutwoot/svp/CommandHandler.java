package com.woutwoot.svp;

import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.google.common.base.Joiner;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;

public class CommandHandler implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equals("svp")) {
			Player p;
			if (!(sender instanceof Player)) {
				sender.sendMessage(Vars.tag + "You have to be a logged in player to use this plugin!");
				return true;
			} else {
				p = (Player) sender;
			}

			switch (args.length) {
			case 1:
				if (args[0].equals("remove") || args[0].equals("delete")) {
					if(p.isOp()){
						Plot plot = Main.pm.getPlotByLocation(p.getLocation());
						Main.pm.removePlot(plot.getName());
						p.sendMessage(Vars.tag + "Plot was successfully removed.");
					}else{
						p.sendMessage(Vars.tag + "No permission.");
					}
				} else if (args[0].equals("reset")) {
					if(p.isOp()){
						Plot plot = Main.pm.getPlotByLocation(p.getLocation());
						Main.pm.resetPlot(plot);
						p.sendMessage(Vars.tag + "Plot was successfully reset.");
					}else{
						p.sendMessage(Vars.tag + "No permission.");
					}
				} else if (args[0].equals("updateplots")) {
					if(p.isOp()){
						Main.pm.updateAllPlots();
						p.sendMessage(Vars.tag + "Plots have updated.");
					}else{
						p.sendMessage(Vars.tag + "No permission.");
					}
				} else if (args[0].equals("help")) {
					p.sendMessage(Vars.tag + "No help for you... sorry!");
				} else if (args[0].equals("create")) {
					if (p.isOp()) {
						WorldEditPlugin we = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
						Selection sel = we.getSelection(p);
						Main.pm.createAndAddPlot("plot" + Main.pm.plotCount, sel.getMinimumPoint(), sel.getMaximumPoint());
						p.sendMessage(Vars.tag + "Plot was successfully created.");
					} else {
						p.sendMessage(Vars.tag + "No permission.");
					}
				} else if (args[0].equals("list")) {
					if (p.isOp()) {
						p.sendMessage(Vars.tag + "-- List of plots: --");
						for(Plot plot : Main.pm.plots){
							p.sendMessage(Vars.tag + plot);
						}
						p.sendMessage(Vars.tag + "-- End of list. --");
					} else {
						p.sendMessage(Vars.tag + "No permission.");
					}
				} else if (args[0].equals("info")) {
					if (p.isOp()) {
						Plot plot = Main.pm.getPlotByLocation(p.getLocation());
						if (plot != null) {
							p.sendMessage(Vars.tag + "A plot was found at this location. Information:");
							p.sendMessage(Vars.tag + "Name: " + plot.getName());
							if (plot.getOwner() != null) {
								p.sendMessage(Vars.tag + "Owner: " + plot.getOwner());
							} else {
								p.sendMessage(Vars.tag + "No owner - buyable!");
							}
							if (!plot.getFriends().isEmpty()) {
								p.sendMessage(Vars.tag + "Friends: " + Joiner.on(",").join(plot.getFriends()));
							}
							p.sendMessage(Vars.tag + "Price: " + plot.getPrice());
						} else {
							p.sendMessage(Vars.tag + "There is no plot at this location.");
						}
					} else {
						p.sendMessage(Vars.tag + "No permission.");
					}
				} else if (args[0].equals("buy")) {
					Plot plot = Main.pm.getPlotByLocation(p.getLocation());
					if (plot != null && plot.getOwner() == null) {
						if (Main.econ.getBalance(p.getName()) > plot.getPrice()) {
							EconomyResponse r = Main.econ.withdrawPlayer(p.getName(), plot.getPrice());
							if (r.transactionSuccess()) {
								Main.pm.claimPlot(plot.getName(), p.getName());
								p.sendMessage(Vars.tag + "Tadam! Enjoy your new plot!");
								plot.getArea().doeNeZiekeFlashShow();
							} else {
								p.sendMessage(Vars.tag + "Something went wrong while buying the plot...");
							}
						} else {
							p.sendMessage(Vars.tag + "You can't afford this plot right now.");
						}
					} else {
						p.sendMessage(Vars.tag + "There is no plot here or it is allready owned by someone.");
					}
				}
				return true;
			case 2:
				if (args[0].equals("reset")) {
					if (p.isOp()) {
						if (args[1].equals("all")) {
							p.sendMessage(Vars.tag + "Resetting all plots! This might take a few seconds depending on the amount of plots!");
							Main.pm.resetAllPlots();
						} else {
							Main.pm.resetPlot(args[1]);
							p.sendMessage(Vars.tag + "Plot was successfully reset.");
						}
					} else {
						p.sendMessage(Vars.tag + "No permission.");
					}
				} else if (args[0].equals("delete") || args[0].equals("remove")) {
					if (p.isOp()) {
						Main.pm.removePlot(args[1]);
						p.sendMessage(Vars.tag + "Plot was successfully removed.");
					} else {
						p.sendMessage(Vars.tag + "No permission.");
					}
				}
				return true;
			case 3:
				if (args[0].equals("setowner")) {
					if (p.isOp()) {
						Main.pm.claimPlot(args[1], args[2]);
						p.sendMessage(Vars.tag + "Owner was successfully set.");
					} else {
						p.sendMessage(Vars.tag + "No permission.");
					}
				}
				return true;
			}
		}
		return false;
	}

}
