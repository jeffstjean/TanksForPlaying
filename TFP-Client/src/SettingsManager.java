
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.LinkedList;

public class SettingsManager {
	static File config = new File("src/resources/configs/config.txt");
	static File blankConfig = new File("src/resources/default_configs/default_config.txt");
	static String line = null;
	static LinkedList<String> settings = new LinkedList<String>();
	static boolean isError = false;

	public static void init() {
		try {
			if (!config.exists()) {
				config.createNewFile(); 
				Files.copy(blankConfig.toPath(), config.toPath(), StandardCopyOption.REPLACE_EXISTING);
			}
			FileReader fr = new FileReader(config);
			BufferedReader br = new BufferedReader(fr);

			while ((line = br.readLine()) != null) {
				String words[] = line.split(": ");
				if (words.length != 2) {
					// Settings aren't written properly
					isError = true;
					break;
				}
				settings.add(words[1]);
			}
		} catch (FileNotFoundException e) {
			System.err.println("FILE NOT FOUND EXCEPTION");
		} catch (IOException e) {
			System.err.println("IO EXCEPTION");
		}
		if (isError) {
			System.err.println("Settings aren't configured properly");
			return;
		}
		
	}

	public static int getIntSetting(Setting set) {
		switch (set) {
		case NUM_PLAYERS:
			try {
				return Integer.parseInt(settings.get(0).trim());
			} catch (Exception e) {
				return -1;
			}
		case PORT:
			try {
				return Integer.parseInt(settings.get(1).trim());
			} catch (Exception e) {
				return -1;
			}
		case FPS:
			try {
				return Integer.parseInt(settings.get(2).trim());
			} catch (Exception e) {
				return -1;
			}
		case TANK_SIZE:
			try {
				return Integer.parseInt(settings.get(4).trim());
			} catch (Exception e) {
				return -1;
			}
		case TANK_SPEED:
			try {
				return Integer.parseInt(settings.get(5).trim());
			} catch (Exception e) {
				return -1;
			}
		case BULLET_SIZE:
			try {
				return Integer.parseInt(settings.get(6).trim());
			} catch (Exception e) {
				return -1;
			}
		case BULLET_SPEED:
			try {
				return Integer.parseInt(settings.get(7).trim());
			} catch (Exception e) {
				return -1;
			}
		case MAP_LAYOUT:
			try {
				return Integer.parseInt(settings.get(8).trim());
			} catch (Exception e) {
				return -1;
			}
		default: 
			return -1;
		}
	}

	public static String getStringSetting(Setting set) {
		switch (set) {
		case IP:
			try {
				return settings.get(3).trim();
			} catch (Exception e) {
				
			}
		default:
			return null;
		}
	}
}
