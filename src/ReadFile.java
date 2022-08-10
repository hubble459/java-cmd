import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class ReadFile {
    private String filename;
    private String[] file;

    public ReadFile(String filename) {
        this.filename = filename;
    }

    public boolean read() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            Object[] lines = reader.lines().toArray();

            file = new String[lines.length];
            for (int i = 0; i < lines.length; i++) {
                file[i] = lines[i].toString();
            }
        } catch (FileNotFoundException e) {
            System.out.println(filename + " does not exist, or is a directory.");
            return false;
        }
        return true;
    }

    public String[] getFile() {
        return file;
    }
}
