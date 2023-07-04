package assembler;

import java.io.FileWriter;
import java.io.IOException;
import java.util.NoSuchElementException;

public class HackAssembler {
    private static void validateExtension(String fileName) {
        String extension = fileName.substring(fileName.length() - 4);
        if (extension.equals(".asm")) return;
        throw new IllegalArgumentException("Error: supplied file isn't an .asm file.");
    }

    public static void main(String[] args) throws IOException {
        String asmFile = args[0];
        validateExtension(asmFile);

        Code code = new Code();
        SymbolTable symbolTable = new SymbolTable();
        Parser parser = new Parser(asmFile, code);
        int n = 0;

        // First pass.
        while (parser.hasMoreCommands()) {
            try {parser.advance();} catch (NoSuchElementException e) {break;}

            if (parser.commandType().equals(CommandType.L))
                symbolTable.put(parser.symbol(), n);
            else n++;
        } parser.close();

        parser = new Parser(asmFile, code);
        String hackFile = asmFile.replaceAll(".asm", ".hack");
        FileWriter writer = new FileWriter(hackFile);
        n = 16;

        // Second pass.
        while (parser.hasMoreCommands()) {
            try {parser.advance();} catch (NoSuchElementException e) {break;}

            if (parser.commandType().equals(CommandType.L)) continue;

            StringBuilder sb = new StringBuilder();

            if (parser.commandType().equals(CommandType.C)) {
                sb.append("111");
                sb.append(parser.comp());
                sb.append(parser.dest());
                sb.append(parser.jump());
            }

            else if (parser.commandType().equals(CommandType.A)) {
                String symbol = parser.symbol();
                int address;

                if (Character.isDigit(symbol.charAt(0)))
                    address = Integer.parseInt(symbol);

                else {
                    if (!symbolTable.contains(symbol))
                        symbolTable.put(symbol, n++);
                    address = symbolTable.get(symbol);
                }

                String binary = Integer.toBinaryString(address);
                while (sb.length() < 16 - binary.length())
                    sb.append("0");
                sb.append(binary);
            }

            sb.append("\n");

            writer.append(sb);
        }
        writer.close();
    }
}
