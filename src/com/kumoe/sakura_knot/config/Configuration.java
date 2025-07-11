package com.kumoe.sakura_knot.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.kumoe.sakura_knot.SakuraKnot;

public abstract class Configuration {

	private String resourceName;
	protected String configName;
		public String getConfigName() { return this.configName; }

	protected File configFile;
		public File getConfigFile() { return this.configFile; }

	protected FileConfiguration config;

	public Configuration(String configName) {
		this.configName = configName;
		this.createDefaultConfiguration(true);
		this.loadConfiguration();
	}

	public Configuration(String resourceName, String configName) {
		this.resourceName = resourceName;
		this.configName = configName;
		this.createDefaultConfiguration(false);
		this.loadConfiguration();
	}

	protected void createDefaultConfiguration(Boolean empty) {
		try {
			this.configFile = new File(SakuraKnot._this().getDataFolder(), this.configName);
			if (!this.configFile.exists()) {
				this.configFile.getParentFile().mkdirs();
				if (empty) {
					this.configFile.createNewFile();
				} else {
					InputStream in = getClass().getClassLoader().getResourceAsStream(this.resourceName);
					OutputStream out = new FileOutputStream(this.configFile);
					byte[] copyBuffer = new byte[1024];
					int read;
					while((read = in.read(copyBuffer)) > 0) {
						out.write(copyBuffer, 0, read);
					}
					out.close();
					in.close();
				}
			} else {
				SakuraKnot._this().logNotice("Found existing file at " + this.configName + " - not creating a new one");
			}
		} catch (IOException e) {
			SakuraKnot._this().logSevere("Error initializing config file " + this.configName);
			e.printStackTrace();
		}
	}

	protected void loadConfiguration() {
		this.config = YamlConfiguration.loadConfiguration(this.configFile);
	}

	public void saveConfiguration() {
		try {
			this.config.save(this.configFile);
		  } catch (IOException e) {
			e.printStackTrace();
			SakuraKnot._this().logSevere("Unable to save configuration file.");
		  }
	}
}
