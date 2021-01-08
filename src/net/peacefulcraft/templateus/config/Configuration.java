package net.peacefulcraft.templateus.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import net.peacefulcraft.templateus.Templateus;

public abstract class Configuration {

	protected String configName;
		public String getConfigName() { return this.configName; }

	protected File configFile;
		public File getConfigFile() { return this.configFile; }

	protected FileConfiguration config;

	public Configuration(String configName) {
		this.configName = configName;
		this.createDefaultConfiguration(false);
		this.loadConfiguration();
	}

	public Configuration(String configName, Boolean empty) {
		this.configName = configName;
		this.createDefaultConfiguration(empty);
		this.loadConfiguration();
	}

	protected void createDefaultConfiguration(Boolean empty) {
		try {
			this.configFile = new File(Templateus._this().getDataFolder(), this.configName + ".yml");
			if (!configFile.exists()) {
				configFile.getParentFile().mkdir();
				if (empty) {
					this.configFile.createNewFile();
				} else {
					InputStream in = getClass().getClassLoader().getResourceAsStream("config.yml");
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
				Templateus._this().logNotice("Found existing file at " + this.configName + ".yml - not creating a new one");
			}
		} catch (IOException e) {
			Templateus._this().logSevere("Error initializing config file " + this.configName);
			e.printStackTrace();
		}
	}

	protected void loadConfiguration() {
		config = YamlConfiguration.loadConfiguration(new File(Templateus._this().getDataFolder(), this.configName + ".yml"));
	}

	public void saveConfiguration() {
		try {
			this.config.save(this.configFile);
		  } catch (IOException e) {
			Templateus._this().logSevere("Unable to save configuration file.");
			e.printStackTrace();
		  }
	}
}