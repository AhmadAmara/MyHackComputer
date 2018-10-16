public class Code {

    private tablesTools codesTabl = new tablesTools();
    private static final String INITIAL_A_COMMAND = "0";


    /**
     *
     * @param destStr -mnemonic(String)
     * @return - the binary code of the dest mnemonic.
     */
    public String dest(String destStr){
        return codesTabl.destTable.get(destStr);

    }

    /**
     *
     * @param compStr-mnemonic(String)
     * @return- the binary code of the comp mnemonic.
     */
    public String comp(String compStr){
            return codesTabl.compTable.get(compStr);
    }


    /**
     *
     * @param jumpStr-mnemonic(String)
     * @return- the binary code of the jump mnemonic.
     */
    public String jump(String jumpStr){
        return codesTabl.jumpTable.get(jumpStr);
    }


    /**
     * convert A_Command to binary code
     * @param aCommand - the A_Command
     * @return- A_Command into binary .
     */
    public String aInstruction(String aCommand)
    {
        int decimalValue = Integer.parseInt(aCommand.substring(0,aCommand.length()));
        String binaryValue = Integer.toBinaryString(decimalValue);
        int initialBinaryLength =binaryValue.length();
        for (int i=15 ; i>initialBinaryLength; i--) {

            binaryValue = INITIAL_A_COMMAND + binaryValue;
        }

        return binaryValue;
    }
}
