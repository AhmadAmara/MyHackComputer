import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.File;

public class Tokenizer {

    private static String keywords = "class|constructor|function|method|field|static|var|int|" +
            "char|boolean|void|true|false|null|this|let|do|if|else|while|return";
    private static String symbols = "[\\{\\}\\(\\)\\[\\]\\.\\,\\;\\+\\-\\*\\/\\&\\|\\<\\>\\=\\~]";
    private static String identifierRegexString = "[a-zA-Z_]+[a-zA-Z_0-9]*";
    private static String intRegexString = "\\d+";
    private static String stringRegexString = "\"[^\"\n]*\"";
    private static Pattern allTokenPatterns = Pattern.compile(identifierRegexString + "|" + symbols + "|"
            + stringRegexString + "|" + keywords + "|" + intRegexString);
    private static String currentToken = "";
    private static String currentTokenType = "";
    private static Pattern stringPattern = Pattern.compile(stringRegexString);
    private static Pattern emptyLine = Pattern.compile("\\s*");
    private ArrayList<String> tokensList = new ArrayList<>();
    private static Integer counter = -1;


    @Override
    public String toString() {
        return "in " + counter + " : " + currentToken;
    }

    /**
     * Opens the input file/stream and gets ready
     to tokenize it.
     * @param jackFile the input file.
     */
    public Tokenizer(File jackFile)
    {
        ArrayList<String> jackCodeLines = new ArrayList<>();
        jackCodeLines = getCodeLines(jackFile.getAbsolutePath());
        jackCodeLines = getParsableLines(jackCodeLines);
        String joinedCode;
        joinedCode = String.join("\n", jackCodeLines);
        joinedCode += "\n";
        joinedCode = noComments(joinedCode);

        Matcher matcher = allTokenPatterns.matcher(joinedCode);
        tokensList = new ArrayList<>();
        counter = -1;

        while (matcher.find()){
            if(matcher.group().equals(">")){
                tokensList.add("&gt;");
            } else if(matcher.group().equals("<")){
                tokensList.add("&lt;");
            } else if (matcher.group().equals("&")){
                tokensList.add("&amp;");
            } else {
                tokensList.add(matcher.group());
            }
        }


        currentToken = "";
        currentTokenType = "";

    }

    /**
     * Do we have more tokens in the input?
     * @return boolean.
     */
    public boolean hasMoreTokens() {

        return counter + 1 < tokensList.size();
    }

    /**
     * updates token's type.
     */
    private void updateTokenType(){
        if(currentToken.matches(keywords)){
            currentTokenType = "keyword";
        } else if(currentToken.matches(symbols)){
            currentTokenType = "symbol";
        } else if(currentToken.matches(identifierRegexString)){
            currentTokenType = "identifier";
        } else if(currentToken.matches(intRegexString)){
            currentTokenType = "intConst";
        } else if(currentToken.matches(stringRegexString)){
            currentTokenType = "stringConst";
        } else if(currentToken.equals("&gt;") || currentToken.equals("&lt;") || currentToken.equals("&amp;")){
            currentTokenType = "symbol";
        }
    }

    /**
     * Gets the next token from the input and
     makes it the current token. This method
     should only be called if hasMoreTokens()
     is true. Initially there is no current token.
     */
    public void advance() {
        if(hasMoreTokens()){
            counter++;
            currentToken = tokensList.get(counter);
            updateTokenType();
        } else {
            System.err.println("Cannot advance - last token");
        }

    }

    /**
     * Gets the previous token from the input and
     makes it the current token. This method
     should only be called if we are not in
     first token. else: do nothing.
     */
    public void reverse() {
        if(counter > 0){
            counter--;
            currentToken = tokensList.get(counter);
            updateTokenType();
        } else {
            System.err.println("Cannot reverse - first token");
        }
    }

    /**
     * Returns the type of the current token.
     */
    public String tokenType()
    {

        return currentTokenType;
    }

    /**
     * Returns the keyword which is the current
     token. Should be called only when
     tokenType() is KEYWORD.
     */
    public String keyWord()
    {
        if(currentTokenType.equals("keyword")){
            return currentToken;
        } else {
            return "";
        }
    }

    /**
     * Returns the string which is the current
     token. Should be called only when
     tokenType() is SYMBOL.
     */
    public String symbol()
    {
        if(currentTokenType.equals("symbol")){
            return currentToken;
        } else {
            return "";
        }
    }

    /**
     * Returns the identifier which is the current
     token. Should be called only when
     tokenType() is IDENTIFIER.
     */
    public String identifier()
    {
        if(currentTokenType.equals("identifier")){
            return currentToken;
        } else {
            return "";
        }
    }

    /**
     * Returns the integer value of the current
     token. Should be called only when
     tokenType() is INT_CONST.
     */
    public String intVal()
    {
        if(currentTokenType.equals("intConst")){
            return currentToken;
        } else {
            return "";
        }
    }

    /**
     * Returns the string value of the current
     token, without the double quotes. Should
     be called only when tokenType() is
     STRING_CONST.
     */
    public String stringVal()
    {
        if(currentTokenType.equals("stringConst")){
            return currentToken;
        } else {
            return "";
        }
    }

    /*
Removes comments and spaces for the actual parsing
*/
    private static ArrayList<String> getParsableLines(ArrayList<String> rawLines){
        ArrayList<String> codeLines = new ArrayList<>();
        for(String line : rawLines){

            Matcher matcherEmptyLine = emptyLine.matcher(line);

            if(!matcherEmptyLine.matches()){

                line = line.trim();
                codeLines.add(line);

            }
        }
        return codeLines;
    }


    /*
Parses given file to a list of code
 */
    private static ArrayList<String> getCodeLines(String fileName)
    {
        ArrayList<String> codeLines = new ArrayList<>();
        try {
            BufferedReader reader;
            reader = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = reader.readLine()) != null) {
                codeLines.add(line);
            }
            return codeLines;
        } catch (Exception e) // not really necessary since we assume valid input
        {
            return null;
        }
    }

    /**
     * Removes all comments from the jack code.
     * @param str the raw code represented as a string.
     * @return the string of uncommented code.
     */
    private static String noComments(String str) {

        String WHITE_SPACE = " ";
        Integer i = 0;
        String finalStr = "";
        Integer j = 0;
        while (i < str.length()){
            Character c = str.charAt(i);
            String c1 = c.toString();
            if (c1.equals("\"")){
                j = str.indexOf("\"", i+1);
                finalStr += str.substring(i, j+1);
                i = j + 1;
            } else if (c1.equals("/")){
                Character d = str.charAt(i+1);
                String d1 = d.toString();
                if (d1.equals("/")){
                    j = str.indexOf("\n", i + 1);
                    i = j + 1;
                    finalStr += WHITE_SPACE;
                } else if (d1.equals("*")){
                    j = str.indexOf("*/", i + 1);
                    i = j + 2;
                    finalStr += WHITE_SPACE;
                } else {
                    finalStr += c1;
                    i += 1;
                }

            } else {
                finalStr += c1;
                i += 1;
            }
        }
        str = finalStr;
        return str;
    }

    public String getCurrentToken(){
        return currentToken;
    }
}

