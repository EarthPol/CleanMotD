package dev._2lstudios.cleanmotd.bungee;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import java.io.IOException;

import java.util.concurrent.TimeUnit;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import dev._2lstudios.cleanmotd.bungee.commands.CleanMotDCommand;
import dev._2lstudios.cleanmotd.bungee.listeners.ProxyPingListener;
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
					JsonParser parser = new JsonParser();
					
					try {        
			            URL oracle = new URL("https://earthpol.com/count/count.php"); // URL to Parse
			            URLConnection yc = oracle.openConnection();
			            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
			           
			            String inputLine;
			            while ((inputLine = in.readLine()) != null) {              
			                JsonObject a = (JsonObject) parser.parse(inputLine);
			                String count = a.get("playerCount").toString();
			                count = count.replace("\"","");
			                playerCount = Integer.parseInt(count);
			                playerCounter.setCount(playerCount);
			            }
			            in.close();
			        } catch (FileNotFoundException e) {
			            e.printStackTrace();
			        } catch (IOException e) {
			            e.printStackTrace();
			        } catch (Exception e) {
						e.printStackTrace();
					}
	            }
	        }, 1, 5, TimeUnit.SECONDS);
	    }
}