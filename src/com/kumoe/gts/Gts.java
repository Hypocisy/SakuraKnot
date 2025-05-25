package com.kumoe.gts;

import com.Zrips.CMI.CMI;
import com.kumoe.gts.commands.ExampleCommand;
import com.kumoe.gts.config.MainConfiguration;
import com.kumoe.gts.listeners.PlayerJoinListener;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;
import java.util.logging.Level;

public class Gts extends JavaPlugin {
	public static final String messagingPrefix = ChatColor.GREEN + "[" + ChatColor.BLUE + "PCN" + ChatColor.GREEN + "]" + ChatColor.RESET;
	private static Economy econ;
	private static Gts _this;
	private static MainConfiguration configuration;

	public static Gts _this() {
		return _this;
	}

	public static Gts getInstance() {
		return _this();
	}

	public static MainConfiguration getConfiguration() {
		return configuration;
	}

	public static Economy getEcon() {
		return econ;
	}

	public static void depositPlayer(UUID player, double price) {
		Gts.getEcon().depositPlayer(Bukkit.getPlayer(player), price);
	}

	public static void withdrawPlayer(UUID player, double price) {
		Gts.getEcon().withdrawPlayer(Bukkit.getPlayer(player), price);
	}

	public static double getBalance(UUID player) {
		return Gts.getEcon().getBalance(Bukkit.getPlayer(player));
	}

	/**
	 * Called when Bukkit server enables the plguin
	 * For improved reload behavior, use this as if it was the class constructor
	 */
	public void onEnable() {
		this._this = this;
		// Save default config if one does not exist. Then load the configuration into memory
		configuration = new MainConfiguration();

//		this.setupCommands();
		this.setupEventListeners();
		// setupEconomy
		if (!setupEconomy()) {
			logDebug("Disabled due to no Vault dependency found!");
			getServer().getPluginManager().disablePlugin(this);
		}
	}

	public void logDebug(String message) {
		if (configuration.isDebugEnabled()) {
			this.getServer().getLogger().log(Level.INFO, message);
		}
	}

	public void logNotice(String message) {
		this.getServer().getLogger().log(Level.INFO, message);
	}

	public void logWarning(String message) {
		this.getServer().getLogger().log(Level.WARNING, message);
	}

	public void logSevere(String message) {
		this.getServer().getLogger().log(Level.SEVERE, message);
	}

	/**
	 * Called whenever Bukkit server disableds the plugin
	 * For improved reload behavior, try to reset the plugin to it's initaial state here.
	 */
	public void onDisable() {
		this.getServer().getScheduler().cancelTasks(this);
	}

	private void setupCommands() {
		this.getCommand("example").setExecutor(new ExampleCommand());
	}

	private void setupEventListeners() {
		this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
	}

	private boolean setupEconomy() {
		// cmi vault support
		if (getServer().getPluginManager().getPlugin("CMI") != null) {
			if (CMI.getInstance().getEconomyManager().isEnabled()) {
				econ = CMI.getInstance().getEconomyManager().getVaultManager().getVaultEconomy();
				logNotice("Enabled CMI Vault!");
				return econ != null;
			}
		} else if (getServer().getPluginManager().getPlugin("Vault") != null) {
			// normal vault support
			RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
			if (rsp != null) {
				econ = rsp.getProvider();
				logNotice("Enabled" + econ.getName() + " Vault!");
				return econ != null;
			}
		}

		return false;
	}
}