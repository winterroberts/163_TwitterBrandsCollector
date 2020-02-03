package net.aionstudios.twitbrand;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.TimeZone;

import org.json.JSONException;
import org.json.JSONObject;

import net.aionstudios.twitbrand.database.DatabaseConnector;
import net.aionstudios.twitbrand.util.ConfigUtils;
import net.aionstudios.twitbrand.util.DatabaseUtils;

public class TwitterBrandsInfo {
	
	public static final String CONFIG_DB = "./database.json";
	private static JSONObject dbConfig;
	
	/**
	 * Reads configurable information when the server starts and handles setup if necessary.
	 * Should a config file not exist it will be created and the application terminated.
	 * @return True if the config was available and processed, false otherwise.
	 * @throws JSONException 
	 * @throws IOException 
	 */
	public static void readConfigsAtStart() throws JSONException, IOException {
		dbConfig = ConfigUtils.getLinkedJsonObject();
		File dcf = new File(CONFIG_DB);
		if(!dcf.exists()) {
			dcf.getParentFile().mkdirs();
			dcf.createNewFile();
			dbConfig.put("hostname", "127.0.0.1");
			dbConfig.put("database", "db");
			dbConfig.put("username", "root");
			dbConfig.put("password", "password");
			dbConfig.put("port", 0);
			dbConfig.put("autoReconnect", true);
			dbConfig.put("timezone", "UTC");
			dbConfig.put("enabled", false);
			ConfigUtils.writeConfig(dbConfig, dcf);
			System.out.println("Database config was not available! Exiting.");
			System.exit(0);
		} else {
			dbConfig = ConfigUtils.readConfig(dcf);
		}
		if(dbConfig.getBoolean("enabled")) {
			String hostname = dbConfig.getString("hostname");
			String database = dbConfig.getString("database");
			String username = dbConfig.getString("username");
			String password = dbConfig.getString("password");
			int port = dbConfig.getInt("port");
			boolean autoReconnect = dbConfig.has("autoReconnect")?dbConfig.getBoolean("autoReconnect"):true;
			String timezone = dbConfig.has("timezone")?dbConfig.getString("timezone"):"UTC";
			if(!Arrays.asList(TimeZone.getAvailableIDs()).contains(timezone)) {
				System.err.println("Failed connecting to database! No such timezone as '"+timezone+"' in config file!");
			} else {
				if(port > 0 && port < 65536) {
					DatabaseConnector.setupDatabase(hostname, database, Integer.toString(port), username, password, autoReconnect, timezone);
				} else {
					DatabaseConnector.setupDatabase(hostname, database, username, password, autoReconnect, timezone);
				}
			}
		}
	}
	
	private static final String createSchema = "CREATE DATABASE `twitbrands` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */;";
	private static final String createStocksTable = "CREATE TABLE `twitbrands`.`stock_volume` (" + 
			"  `requestID` int(11) NOT NULL AUTO_INCREMENT," + 
			"  `symbol` varchar(16) NOT NULL," + 
			"  `matchingID` varchar(32) NOT NULL," + 
			"  `stock_price_current` double NOT NULL," + 
			"  `tweet_volume` int(11) NOT NULL," + 
			"  `unix_timestamp` varchar(32) NOT NULL," + 
			"  `currency` varchar(8) NOT NULL DEFAULT 'USD'," + 
			"  PRIMARY KEY (`requestID`)" + 
			") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;";	
	private static final String createTweetsTable = "CREATE TABLE `twitbrands`.`tweets` (" + 
			"  `tweetID` varchar(32) NOT NULL," + 
			"  `matchingID` varchar(32) NOT NULL," + 
			"  `tweet` text NOT NULL," + 
			"  `unix_timestamp` varchar(32) NOT NULL," + 
			"  PRIMARY KEY (`tweetID`)" + 
			") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;";
	public static void setupDB() {
		//This may fail if using the wrong SQL version. utf8mb4_0900_ai_ci is not available for MySQL 5.
		DatabaseUtils.prepareAndExecute(createSchema, false);
		DatabaseUtils.prepareAndExecute(createStocksTable, false);
		DatabaseUtils.prepareAndExecute(createTweetsTable, false);
	}

}
