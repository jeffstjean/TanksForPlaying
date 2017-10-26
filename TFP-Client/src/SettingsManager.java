
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class SettingsManager {

    static File config = new File("/resources/config.txt");
    static String line = null;
    static LinkedList<String> settings = new LinkedList<String>();

    public static void init() {
        try {
            FileReader fr = new FileReader(config);
            BufferedReader br = new BufferedReader(fr);

            while ((line = br.readLine()) != null) {
                System.out.println(line);
                settings.add(line);
            }
        } catch (FileNotFoundException e) {
            System.err.println("FILE NOT FOUND EXCEPTION");
        } catch (IOException e) {
            System.err.println("IO EXCEPTION");
        }
    }

    public static String getSetting(Setting set) {
        switch (set) {
            case NUMPLAYERS:
                break;
            case BULLETSPEED:
                break;
            case PORT:
                return "Port";
        }
        return "Nothing";
    }
}
