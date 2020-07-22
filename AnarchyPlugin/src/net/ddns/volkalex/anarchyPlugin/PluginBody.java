package net.ddns.volkalex.anarchyPlugin;

import org.bukkit.plugin.java.JavaPlugin;

public class PluginBody extends JavaPlugin {

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new EventListner(), this);
		DataStorHandler.loadFromFile();
	}

	@Override
	public void onDisable() {
	}
}
