package com.woutwoot.svp;

import java.io.File;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	static Plugin plugin;
	static File folder;
	static PlotManager pm = new PlotManager();
	static Permission permission = null;
	static Economy econ = null;

	@Override
	public void onEnable() {
		plugin = this;
		folder = this.getDataFolder();
		folder.mkdirs();
		this.getCommand("svp").setExecutor(new CommandHandler());
		setupPermissions();
		setupEconomy();
		Config.load();
	}

	@Override
	public void onDisable() {
		Config.save();
	}

	private boolean setupPermissions() {
		RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
		if (permissionProvider != null) {
			permission = permissionProvider.getProvider();
		}
		return (permission != null);
	}

	private boolean setupEconomy() {
		RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
		if (economyProvider != null) {
			econ = economyProvider.getProvider();
		}
		return (econ != null);
	}
}
