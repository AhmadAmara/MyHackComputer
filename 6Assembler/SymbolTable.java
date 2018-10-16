
import java.util.*;


public class SymbolTable {
    private Code c = new Code();

    public    Hashtable<String,String> symbolTable;

    /**
     * A symbol table that keeps correspondence between symbolic tables
     * and numeric addresses
     */
    public SymbolTable()
    {
        symbolTable = new Hashtable<String, String>();
        symbolTable.put("SP",c.aInstruction("0"));
        symbolTable.put("LCL",c.aInstruction("1"));
        symbolTable.put("ARG",c.aInstruction("2"));
        symbolTable.put("THIS",c.aInstruction("3"));
        symbolTable.put("THAT",c.aInstruction("4"));
        symbolTable.put("R0",c.aInstruction("0"));
        symbolTable.put("R1",c.aInstruction("1"));
        symbolTable.put("R2",c.aInstruction("2"));
        symbolTable.put("R3",c.aInstruction("3"));
        symbolTable.put("R4",c.aInstruction("4"));
        symbolTable.put("R5",c.aInstruction("5"));
        symbolTable.put("R6",c.aInstruction("6"));
        symbolTable.put("R7",c.aInstruction("7"));
        symbolTable.put("R8",c.aInstruction("8"));
        symbolTable.put("R9",c.aInstruction("9"));
        symbolTable.put("R10",c.aInstruction("10"));
        symbolTable.put("R11",c.aInstruction("11"));
        symbolTable.put("R12",c.aInstruction("12"));
        symbolTable.put("R13",c.aInstruction("13"));
        symbolTable.put("R14",c.aInstruction("14"));
        symbolTable.put("R15",c.aInstruction("15"));
        symbolTable.put("SCREEN",c.aInstruction("16384"));
        symbolTable.put("KBD",c.aInstruction("24576"));

    }


    /**
     *Adds the pair (symbol,address) to the table
     * @param symbol (String)
     * @param address (string)
     */
    public void addEntry(String symbol, String address)
    {

           symbolTable.put(symbol, c.aInstruction(address));
    }


    /**
     *
     * @param symbol (String)
     * @return - true if the symbol table contain the given symbol
     *  false otherwise.
     */
    public Boolean contain(String symbol)
    {

        return symbolTable.containsKey(symbol);
    }


    /**
     *
     * @param symbol (String)
     * @return - the address associated with the symbol.
     */
    public String getAddress(String symbol)
    {
        return symbolTable.get(symbol);
    }

}
