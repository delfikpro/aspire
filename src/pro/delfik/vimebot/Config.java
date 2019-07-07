package pro.delfik.vimebot;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Config {

	private final File file;
	private final Map<String, String> data = new HashMap<>();

	public Config(File file) {
		this.file = file;
	}

	public void read() {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			int i;
			while ((i = reader.read()) != -1) {
				String line = (char) i + reader.readLine();
				String[] part = line.split(": ");
				if (part.length < 2) continue;
				data.put(part[0], part[1]);
			}
			reader.close();
		} catch (FileNotFoundException e) {
			Console.log("Конфиг не найден. Создаём новый. Иди настраивай.");
			try {
				file.createNewFile();
			} catch (IOException ex) {
				Console.log("А нет, создать не получилось, хз чо делать, пиши делфику");
				ex.printStackTrace();
			}
			System.exit(0);
		} catch (IOException e) {
			throw new ConfigurationException("Ошибка при чтении конфига '" + file + "'", e);
		}
	}

	public String get(String key) {
		return data.get(key);
	}

	public static class ConfigurationException extends RuntimeException {

		public ConfigurationException(String s) {
			super(s);
		}

		public ConfigurationException(String s, Throwable throwable) {
			super(s, throwable);
		}

	}

}
