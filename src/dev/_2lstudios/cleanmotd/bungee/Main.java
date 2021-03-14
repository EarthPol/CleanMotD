package dev._2lstudios.cleanmotd.bungee;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import java.util.concurrent.TimeUnit;

import dev._2lstudios.cleanmotd.bungee.commands.CleanMotDCommand;
import dev._2lstudios.cleanmotd.bungee.listeners.MCQuery;
import dev._2lstudios.cleanmotd.bungee.listeners.ProxyPingListener;
import dev._2lstudios.cleanmotd.bungee.listeners.QueryResponse;
import dev._2lstudios.cleanmotd.bungee.utils.ConfigurationUtil;
import dev._2lstudios.cleanmotd.bungee.variables.Messages;
import dev._2lstudios.cleanmotd.bungee.variables.Variables;

public class Main extends Plugin {
	static setterGetter playerCounter = new setterGetter();
	public void onEnable() {
		final ConfigurationUtil configurationUtil = new ConfigurationUtil(this);

		configurationUtil.createConfiguration("%datafolder%/config.yml");
		configurationUtil.createConfiguration("%datafolder%/messages.yml");

		final ProxyServer proxy = getProxy();
		final Variables variables = new Variables(configurationUtil);
		final Messages messages = new Messages(configurationUtil);
		final PluginManager pluginManager = proxy.getPluginManager();

		pluginManager.registerListener(this, new ProxyPingListener(variables));
		pluginManager.registerCommand(this, new CleanMotDCommand("cleanmotd", variables, messages));

		proxy.getScheduler().schedule(this, variables::clearPinged, variables.getCacheTime(), variables.getCacheTime(), TimeUnit.MILLISECONDS);
		countPlayers();
	}
	
	public static int playerCount;
	
	
	 public void countPlayers() {
	        getProxy().getScheduler().schedule(this, new Runnable() {
	            @Override
	            public void run() {
	            	MCQuery mcQuery = new MCQuery("eu.earthpol.com", 25575);
		        	QueryResponse response = mcQuery.basicStat();
		        	playerCount = response.getOnlinePlayers();
		        	playerCounter.setCount(playerCount);
	            }
	        }, 1, 5, TimeUnit.SECONDS);
	    }
	
	
	/*public static Runnable getPlayerCount =
		    new Runnable(){
		        public void run(){
		        	MCQuery mcQuery = new MCQuery("us.earthpol.com", 25575);
		        	QueryResponse response = mcQuery.basicStat();
		        	playerCounter.setCount(response.getOnlinePlayers());
		        }
		    };*/
}