package auth.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.stereotype.Component;

import auth.LoginApplication;

@Component
public class PropertyReader {

	private static Properties properties = new Properties();
	private static PropertyReader instance;

	private PropertyReader() {
		loadProperties();
	}

	public static PropertyReader getInstance() {
		if (instance == null)
			instance = new PropertyReader();
		return instance;
	}

	public String getProperty(String key) {
		return properties.getProperty(key);
	}

	private void loadProperties() {
		try (InputStream stream = LoginApplication.class.getClassLoader()
				.getResourceAsStream("application.properties")) {

			properties.load(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
