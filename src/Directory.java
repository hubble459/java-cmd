import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Scanner;

public class Directory {
    private String slash = System.getProperty("os.name").toLowerCase().contains("windows") ? "\\" : "/";
    protected String directory = System.getProperty("user.dir");

    private String align(int length) {
        return " ".repeat(Math.max(0, 5 - length));
    }
    public void list(String[] args) {
        String newDir = directory;
        if (args.length != 1) {
            if (args[1].contains(slash)) newDir = args[1];
            else newDir = directory + slash + args[1];
        }
        System.out.println("Directory of " + newDir);
        System.out.println();
        try {
            Files.list(new File(newDir).toPath())
                    .forEach(path -> {
                        try {
                            System.out.println((Files.isDirectory(path) ? "<DIR> | ":"      | ") + Files.size(path) + align(String.valueOf(Files.size(path)).length()) + "| " + path.getFileName());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println();
    }

    public void cd(String[] args) {
        if (args.length == 1) System.out.println(directory);
        else if(args[1].equals("..")) {
            ArrayList<Integer> slashPlaces = new ArrayList<>();
            for (int i = 0; i < directory.length(); i++) {
                if (directory.charAt(i) == slash.charAt(0)) {
                    slashPlaces.add(i);
                }
            }
            String newDir = "";
            if (slashPlaces.size() != 1) {
                for (int i = 0; i < slashPlaces.get(slashPlaces.size()-1); i++) {
                    newDir += directory.charAt(i);
                }
            }
            directory = newDir;
        } else {
            String newDir;
            if (args[1].contains(slash)) newDir = args[1];
            else newDir = directory + slash + args[1];
            if (exists(newDir)) directory = newDir;
        }
    }

    private boolean exists(String dir) {
        if (!Files.exists(new File(dir).toPath())) {
            System.out.println("This directory ("+dir+") does not exists!");
            return false;
        }
        if (!Files.isDirectory(new File(dir).toPath())) {
            System.out.println("This is not a directory!");
            return false;
        }
        return true;
    }

    public void remove(String[] args) {
        if (args.length == 1) System.out.println("The syntax of the command is incorrect.");
        else {
            String newDir = args[1].contains(slash) ? args[1] : directory + slash + args[1];
            if (Files.exists(new File(newDir).toPath())) {
                if (Files.isDirectory(new File(newDir).toPath())) {
                    System.out.print(newDir + slash + "*, are you sure? (y/N)");
                    if (!confirm()) return;
                    try {
                        Files.delete(new File(newDir).toPath());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else System.out.println("Directory not found!");
        }
    }

    private boolean confirm() {
        Scanner sc = new Scanner(System.in);
        return sc.nextLine().equalsIgnoreCase("y");
    }

    public void type(String[] args) {
        if (args.length == 1) System.out.println("The syntax of the command is incorrect.");
        else {
            String newDir;
            if (args[1].contains(slash)) newDir = args[1];
            else newDir = directory + slash + args[1];
            if (Files.isDirectory(new File(newDir).toPath())) System.out.println("This is a directory!");
            else if (Files.exists(new File(newDir).toPath())) {
                ReadFile rf = new ReadFile(newDir);
                rf.read();
                String[] file = rf.getFile();
                for (String line: file) {
                    System.out.println(line);
                }
            } else System.out.println("File could not be found!");
        }
    }


}
