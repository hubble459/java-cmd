import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Scanner;

public class Console {
    private Directory dir = new Directory();
    private String[] aliases = aliases();
    private boolean exit;

    public Console() {
        do {
            System.out.print(dir.directory + ">");
            String[] input = input();
            input = aliasConvert(input);
            run(input);
        } while (!exit);
    }

    private void run(String[] input) {
        switch (input[0]) {
            case "alias": alias(input); break;
            case "cd": dir.cd(input); break;
            case "date": date(); break;
            case "dir": dir.list(input); break;
            case "del": dir.remove(input); break;
            case "exit": exit = true; break;
            case "help": help(input); break;
            case "type": dir.type(input); break;
            case "unalias": unalias(input); break;
        }
    }

    private void date() {
        String date = LocalDate.now().toString();
        System.out.println("The current date is: " + date);
        System.out.println();
    }

    private String align(int length) {
        return " ".repeat(Math.max(0, 10 - length));
    }
    private void help(String[] args) {
        String help = "";
        if (args.length > 1) help = args[1].toLowerCase();
        System.out.println("ALIAS" + align(5) + "Make aliases for commands.");
        if (help.equals("alias"))
        System.out.println("CD" + align(2) + "Displays the name of or changes the current directory.");
        if (help.equals("cd"))
        System.out.println("DATE" + align(4) + "Displays the date.");
        if (help.equals("cd"))
        System.out.println("DEL" + align(3) + "Deletes one or more files.");
        if (help.equals("cd"))
        System.out.println("DIR" + align(3) + "Displays a list of files and subdirectories in a directory.");
        if (help.equals("cd"))
        System.out.println("EXIT" + align(4) + "Quits the command interpreter.");
        if (help.equals("cd"))
        System.out.println("HELP" + align(4) + "Provides Help information for Windows commands.");
        if (help.equals("cd"))
        System.out.println("TYPE" + align(4) + "Displays the contents of a text file.");
        System.out.println();
    }

    private String[] input() {
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        String hold = "";
        boolean is = false;
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == '\"') is = !is;
            else if (is && input.charAt(i) == ' ') {
                hold += "*";
            } else hold += input.charAt(i);
        }
        String[] split = hold.split(" ");
        for (int i = 0; i < split.length; i++) {
            if (split[i].contains("*")) split[i] = split[i].replace("*", " ");
        }
        return split;
    }

//    TODO
//    commands[7] = "copy";
//    commands[9] = "find";
//    commands[8] = "edit";

    private void unalias(String[] args) {
        if (args.length == 1) System.out.println("The syntax of the command is incorrect.");
        else {
            for (int i = 0; i < aliases.length; i++) {
                if (aliases[i].split(" - ")[1].equalsIgnoreCase(args[1])) {
                    String[] newAliases = new String[aliases.length - 1];
                    int k = 0;
                    for (int j = 0; j < aliases.length; j++) {
                        if (j != i) {
                            newAliases[k++] = aliases[j];
                            System.out.println(aliases[j]);
                        }
                    }
                    write(newAliases, "aliases.txt");
                    aliases = aliases();
                }
            }
        }
    }

    private void alias(String[] args) {
        if (args.length == 1) {
            for (String alias : aliases) {
                String[] split = alias.split(" - ");
                System.out.println(split[0] + align(split[0].length()) + " - " + split[1]);
            }
        } else if (args.length != 3) System.out.println("The syntax of the command is incorrect. Eg. alias type cat.");
        else {
            String[] newAliases = new String[aliases.length + 1];
            System.arraycopy(aliases, 0, newAliases, 0, aliases.length);
            newAliases[aliases.length] = args[2] + " - " + args[1];
            write(newAliases, "aliases.txt");
            aliases = aliases();
        }
    }

    private void write(String[] array, String filename) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            for (int i = 0; i < array.length; i++) {
                writer.write(array[i]);
                if (i != array.length - 1) writer.newLine();
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String[] aliases() {
        ReadFile rf = new ReadFile("aliases.txt");
        rf.read();
        return rf.getFile();
    }

    private String[] aliasConvert(String[] input) {
        for (String alias: aliases) {
            String[] split = alias.split(" - ");
            if (split[1].equals(input[0])) {
                String[] newSplit = split[0].split(" ");
                if (newSplit.length > 1) {
                    String[] newInput = new String[input.length + (newSplit.length - 1)];
                    System.arraycopy(newSplit, 0, newInput, 0, newSplit.length);
                    int j = 0;
                    for (int i = newSplit.length; i < newInput.length; i++) newInput[i] = input[j++];
                    input = newInput;
                } else input[0] = split[0];
            }
        }
        return input;
    }
}
