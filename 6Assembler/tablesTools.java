
import java.util.*;



public class tablesTools {


    public Hashtable<String, String> destTable;
    public Hashtable<String, String> compTable;
    public Hashtable<String, String> jumpTable;

    /**
     * constructor for tables tools
     */
    public tablesTools() {
        MakeCompTable();
        MakeDestTable();
        MakeJumpTable();
    }

    /**
     * make dest table
     * @return- the dest command into binary
     */
    public Hashtable MakeDestTable() {
        destTable = new Hashtable<String, String>();
        destTable.put("", "000");
        destTable.put("M", "001");
        destTable.put("D", "010");
        destTable.put("MD", "011");
        destTable.put("A", "100");
        destTable.put("AM", "101");
        destTable.put("AD", "110");
        destTable.put("AMD", "111");

        return destTable;

    }

    /**
     * make comp table
     * @return- the comp command into binary
     */
    public Hashtable MakeCompTable() {
        compTable = new Hashtable<String, String>();
        compTable.put("0", "110101010");
        compTable.put("1", "110111111");
        compTable.put("-1", "110111010");
        compTable.put("D", "110001100");
        compTable.put("A", "110110000");
        compTable.put("!D", "110001101");
        compTable.put("!A", "110110001");
        compTable.put("-D", "110001111");
        compTable.put("D+1", "110011111");
        compTable.put("A+1", "110110111");
        compTable.put("D-1", "110001110");
        compTable.put("A-1", "110110010");
        compTable.put("D+A", "110000010");
        compTable.put("D-A", "110010011");
        compTable.put("A-D", "110000111");
        compTable.put("D&A", "110000000");
        compTable.put("D|A", "110010101");
        compTable.put("M", "111110000");
        compTable.put("!M", "111110001");
        compTable.put("-M", "111110011");
        compTable.put("M+1", "111110111");
        compTable.put("M-1", "111110010");
        compTable.put("D+M", "111000010");
        compTable.put("D-M", "111010011");
        compTable.put("M-D", "111000111");
        compTable.put("D&M", "111000000");
        compTable.put("D|M", "111010101");
        compTable.put("D>>", "010010000");
        compTable.put("A>>", "010000000");
        compTable.put("M>>", "011000000");
        compTable.put("D<<", "010110000");
        compTable.put("A<<", "010100000");
        compTable.put("M<<", "011100000");

        return compTable;
    }

    /**
     * make jump table
     * @return - the jump command into binary
     */
    public Hashtable MakeJumpTable() {
        jumpTable = new Hashtable<String, String>();
        jumpTable.put("", "000");
        jumpTable.put("JGT", "001");
        jumpTable.put("JEQ", "010");
        jumpTable.put("JGE", "011");
        jumpTable.put("JLT", "100");
        jumpTable.put("JNE", "101");
        jumpTable.put("JLE", "110");
        jumpTable.put("JMP", "111");

        return jumpTable;
    }

}
