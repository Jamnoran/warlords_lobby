package game.util;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import game.logging.Log;
import game.vo.Config;

import java.io.*;
import java.net.URL;
import java.util.Properties;

/**
 * Created by Eric on 2017-03-09.
 */
public class ConfigUtil {

	public static Integer getPort() {
//		return getConfig().getPort();
		return Integer.parseInt(getProperty("app.port"));
	}

	private static String getProperty(String property) {
		String value;
		//to load application's properties, we use this class
		Properties mainProperties = new Properties();

		FileInputStream file;

		//the base folder is ./, the root of the main.properties file
//		String path = "./main.properties";
		String path = null;
		try {
			URL url = ClassLoader.getSystemClassLoader().getResource(".");
			if(url != null){
				path = url.getPath() + "/main.properties";
			}else{
				path = "main.properties";
			}
			Log.i(ConfigUtil.class.getSimpleName(), "Path: " + path);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO Generate file
		}

		//load the file handle for main.properties
		try {
			file = new FileInputStream(path);

			//load all the properties from this file
			mainProperties.load(file);

			//we have loaded the properties, so close the file handle
			file.close();

		} catch (FileNotFoundException e) {
			Log.i("ConfigUtil", "Using local instead");
			path = "./main.properties";
			//load the file handle for main.properties
			try {
				file = new FileInputStream(path);

				//load all the properties from this file
				mainProperties.load(file);

				//we have loaded the properties, so close the file handle
				file.close();

			} catch (FileNotFoundException ex) {
				e.printStackTrace();
			} catch (IOException exce) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		value = mainProperties.getProperty(property);
		return value;
	}

	public static Config getConfig() {
		Gson gson = new Gson();
		JsonReader reader = null;
		try {
			reader = new JsonReader(new FileReader("./src/main/java/game/config/config.json"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Config config = gson.fromJson(reader, Config.class); // contains the whole reviews list
		return config;
	}
}
