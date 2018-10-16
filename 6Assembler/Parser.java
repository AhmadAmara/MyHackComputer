import java.io.BufferedReader;
import java.io.FileReader;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Parser {


    public  String curCommand ;
    static BufferedReader reader;
    public boolean endOfFile = false;
    private static final String A_COMMAND = "A_COMMAND";
    private static final String L_COMMAND = "L_COMMAND";;
    private static final String C_COMMAND = "C_COMMAND";




    /**
     * Encapsulates access to the input code. Reads an assembly language, parses it,
     * and provides convinient access to the commands components. in addition , removes
     * all white space and comments.
     * @param fileName - input code
     */
    public Parser(String fileName) {
        try {
            reader = new BufferedReader(new FileReader(fileName));
        } catch (Exception e) {
            System.err.format("Exception occurred trying to read "+ fileName);
            e.printStackTrace();
        }
    }



    /*
     * remove perfix space
     */
    private static String removePerfixSpace(String line){
        Pattern pattern = Pattern.compile("\\s*(.*)");
        Matcher matcher = pattern.matcher(line);
        if(matcher.find()){
            line = matcher.group(1);
        }
        return line;
    }


    /**
     * Are there more commands in the input
     * @return -true if there are more commands in the input
     * -false otherwise
     */
    boolean hasMoreCommands() {
        try {
            return reader.ready();
        } catch (Exception e) {
            System.err.format("Exception occurred trying to check.", "id the buffer ready to read from it");
            return false;
        }
    }


    /**
     * Reads the next command from the input and makes it the current command.
     * Should be called only if  hasMoreCommands() is true. Initially ther is no
     * current command.
     */
    public void advance() {
        try {
            curCommand = reader.readLine();

            if (curCommand.contains("/") && (curCommand.contains("<<") || curCommand.contains(">>"))) {
                curCommand = curCommand.substring(0, curCommand.indexOf('/') - 1);
            }
            if(curCommand.startsWith("/"))
            {
                if(this.hasMoreCommands()) {
                    advance();
                }
                else{
                    endOfFile = true;
                }
            }
            if (curCommand.contains("/")) {

                curCommand = curCommand.substring(0, curCommand.indexOf('/'));
            }
            curCommand = removePerfixSpace(curCommand);
            curCommand = curCommand.replaceAll("\\s+", "");
            if (curCommand.equals("")) {
                if(this.hasMoreCommands()) {
                    advance();
                }
                else{
                    endOfFile = true;
                }
            }
        }
        catch (IOException e){
            System.err.format("Exception occurred trying to read '%s'.", "the next line");
            e.printStackTrace();
        }
    }


    /**
     * A_COMMAND for @Xxx where Xxx is either a symbolor a decimal number.
     * C_COMMAND for dest=comp;jump
     * L_COMMAND for (Xxx) where Xxx is a symbol
     * @return - the type of the current command
     */
    public String commandType() {


        if (curCommand.contains("@")) {
            return A_COMMAND;
        }
        else if(curCommand.charAt(0) =='(' && curCommand.charAt(curCommand.length()-1)==')')
        {
            return L_COMMAND ;
        }
        else {
            return C_COMMAND;
        }
    }


    /**
     * Should be called only when commandType() is C-command.
     * @return - the comp mnemonic in the current C-command
     */
    public String comp() {
        if (curCommand.contains(";")) {
            return curCommand.substring(curCommand.indexOf("=") + 1, curCommand.indexOf(";"));
        } else {
            return curCommand.substring(curCommand.indexOf("=") + 1, curCommand.length());
        }
    }


    /**
     * Should be called only when commandType() is C-command.
     * @return- the dest mnemonic in the current C-command
     */
    public String dest() {

        if(curCommand.contains("=")) {
            return curCommand.substring(0, curCommand.indexOf("="));
        }
        else
        {
            return "";
        }
    }


    /**
     * Should be called only when commandType() is C-command.
     * @return - the jump mnemonic in the current C-command
     */
    public String jump() {
        if (curCommand.contains(";")) {
            return curCommand.substring(curCommand.indexOf(";") + 1, curCommand.length());

        } else {
            return "";
        }
    }
}

