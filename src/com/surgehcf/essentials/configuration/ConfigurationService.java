package com.surgehcf.essentials.configuration;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.surgehcf.SurgeCore;
import com.surgehcf.essentials.SurgeExtra;

public class ConfigurationService {

	public static void createFile(String name){
		File f = new File(SurgeCore.getInstance().getDataFolder(), "/" + name);

				try {
					f.createNewFile();
					SurgeExtra.log("&a[Success] &rCreated file " + name);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

	}
	public static FileConfiguration getConfig(){
		return SurgeExtra.getInstance().getConfig();
	}
	
	public static FileConfiguration getConfigFile(String name){
		File f = new File(SurgeCore.getInstance().getDataFolder(), name);
		if(f.exists()){
			YamlConfiguration config = YamlConfiguration.loadConfiguration(f);
			try{
				return config;
			}catch(NullPointerException e){
				SurgeExtra.log("&c[Error] &rError whilst grabbing config file. (File not found)");
				SurgeExtra.log("&c[Error] &rUsing default config.yml instead!");
				return getConfig();
			}
		}else{
			SurgeExtra.log("&c[Error] &rError whilst grabbing config file. (File not found)");
			SurgeExtra.log("&c[Error] &rUsing default config.yml instead!");
			return getConfig();
		}
	}
}
