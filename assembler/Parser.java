package assembler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Parser {
    private final Scanner scanner;
    private final Code code;
    private String command;

    public Parser(String asmFile, Code code) throws FileNotFoundException {
        File file = new File(asmFile);
        this.scanner = new Scanner(file);
        this.code = code;
    }

    public boolean hasMoreCommands() {
        return scanner.hasNext();
    }

    public void advance() {
        command = scanner.nextLine();
        command = command.replace(" ", "");
        if (command.contains("//")) command = command.split("//")[0];
        if (command.length() == 0 && scanner.hasNext()) advance();
        else if (command.length() == 0) throw new NoSuchElementException("Finished processing.");
    }

    public CommandType commandType() {
        if (command.length() == 0) return CommandType.END;
        return switch (command.charAt(0)) {
            case '@' -> CommandType.A;
            case '(' -> CommandType.L;
            default -> CommandType.C;
        };
    }

    public String symbol() {
        return switch (commandType()) {
            case A -> command.substring(1);
            case L -> command.substring(1, command.length() - 1);
            default -> null;
        };
    }

    public String dest() {
        if (commandType() != CommandType.C) return null;
        if (command.contains("=")) return code.dest(command.split("=")[0]);
        return "000";
    }

    public String comp() {
        if (commandType() != CommandType.C) return null;

        String key = command;
        if (key.contains("=")) key = key.split("=")[1];
        if (key.contains(";")) key = key.split(";")[0];
        return code.comp(key);
    }

    public String jump() {
        if (commandType() != CommandType.C) return null;
        if (command.contains(";")) return code.jump(command.split(";")[1]);
        return "000";
    }

    public void close() {
        scanner.close();
    }
}
