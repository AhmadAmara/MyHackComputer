

import java.io.*;

public class Assembler{

    private static final String A_COMMAND = "A_COMMAND";
    private static final String L_COMMAND = "L_COMMAND";;
    private static final String C_COMMAND = "C_COMMAND";
    private static final String INITIAL_A_COMMAND = "0";
    private static final String INITIAL_C_COMMAND = "1";


    /**
     * Initializes I/O files and drives the show.
     * @param args -file input 
     */
    public static void main(String args[]) {
        int comCounter = 0;
        SymbolTable sTable = new SymbolTable();
        if (args.length != 1) {
            System.out.println("there is something in the inputs arguments");
        }
        Code code = new Code();
        Parser parseCommands =new Parser(args[0]);
        String outputName = args[0].substring(0, args[0].indexOf(".")) + ".hack";
        try {
            BufferedWriter outputFile = new BufferedWriter(new FileWriter(outputName));
            while (parseCommands.hasMoreCommands()) {

                parseCommands.advance();
                if (parseCommands.endOfFile) {

                    break;
                }
                if (parseCommands.commandType() == L_COMMAND) {
                    String label = (parseCommands.curCommand).substring(1, parseCommands.curCommand.length() - 1);
                    sTable.addEntry(label, comCounter + "");
                } else {
                    comCounter++;
                }
            }
            Parser parseCommands1 = new Parser(args[0]);
            comCounter = 16;
            while (parseCommands1.hasMoreCommands()) {
                parseCommands1.advance();
                if (parseCommands1.endOfFile) {
                    break;
                }
                if (parseCommands1.commandType() == C_COMMAND) {
                    outputFile.write(INITIAL_C_COMMAND + code.comp(parseCommands1.comp()) +
                                            code.dest(parseCommands1.dest()) + code.jump(parseCommands1.jump()) + "\n");
                } else if (parseCommands1.commandType() == A_COMMAND) {
                    String comSymbol = parseCommands1.curCommand.substring(1, parseCommands1.curCommand.length());
                    boolean flag;
                    try {
                        Integer.parseInt(comSymbol);
                        flag = true;
                    } catch (NumberFormatException e) {
                        flag = false;
                    }
                    if (flag) {
                        outputFile.write(INITIAL_A_COMMAND + code.aInstruction((parseCommands1.curCommand).
                                substring(1, (parseCommands1.curCommand).length())) + "\n");
                    } else {
                        if (!sTable.contain(comSymbol)) {
                            sTable.addEntry(comSymbol, comCounter + "");
                            outputFile.write(INITIAL_A_COMMAND + sTable.getAddress(comSymbol) + "\n");
                            comCounter++;
                        } else {
                            outputFile.write(INITIAL_A_COMMAND + sTable.getAddress(comSymbol) + "\n");
                        }
                    }
                }
            }
            outputFile.close();
        } catch (IOException e) {
            System.err.format("Exception occurred trying to open .", outputName);
            e.printStackTrace();
        }
    }
}





