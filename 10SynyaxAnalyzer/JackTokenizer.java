
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JackTokenizer {
    private static BufferedReader reader;
    private static ArrayList<String> linesArray;
    private static ArrayList<String> splitedLinesArray;
    private static ArrayList<String> tokensArray;
    private static int tokensNum;
    private static int curTokenNum;
    public static String cutToken;

    private String keywords = "class|constructor|function|method|field|static|var|int|char|boolean|void|true|false|" +
            "null|this|let|do|if|else|while|return";
    private String symbols = "\\{|\\}|\\(|\\)|\\[|\\]|\\.|,|;|\\+|-|\\*|/|&|\\||<|>|=|&amp;|&gt;|&lt;|~";

    public String operation  = "\\+|-|\\*|/|&|\\||<|>|=";;


    public JackTokenizer(File file) throws IOException {
        reader =  new BufferedReader(new FileReader(file.getAbsolutePath()));
        linesArray = new ArrayList<String>();
        tokensArray = new ArrayList<String>();
        splitedLinesArray = new ArrayList<String>();
        curTokenNum = 0;
        cutToken = "";
        readLines();

        cleanLines();
        splitLines();
        makeTokensArray();
        advance();
        tokensNum = tokensArray.size();
    }

    public Boolean hasMoreTokens(){
        return (curTokenNum < tokensNum);
    }

    public void advance()
    {
        if(curTokenNum == 0)
        {
            cutToken = tokensArray.get(0);
            curTokenNum++;
        }
        else if(curTokenNum < tokensArray.size()-1) {
            cutToken = tokensArray.get(curTokenNum);
            curTokenNum++;
        }
    }

    public String tokenType(){
        Pattern pSymbol = Pattern.compile(symbols);
        Pattern pkeywords = Pattern.compile(keywords);
        Pattern pInteger = Pattern.compile("\\d+");
        Pattern pConstString = Pattern.compile("\"[^\n]*\\s*\"\\s*");

        Matcher mConstString = pConstString.matcher(cutToken);
        Matcher mathcInt = pInteger.matcher(cutToken);
        Matcher mathcKey = pkeywords.matcher(cutToken);
        Matcher mathcSym = pSymbol.matcher(cutToken);

        if(mConstString.matches())
        {
            return "STRING_CONST";
        }else if (mathcInt.matches()){
            return "INT_CONST";
        }else if(mathcKey.matches()){
            return "KEYWORD";
        }else if (mathcSym.matches()){
            return "SYMBOL";
        }else{
            return "IDENTIFIER";
        }
    }

    public String keyWord(){
        if(tokenType().equals("KEYWORD"))
        {
            return cutToken.toUpperCase();
        }else{
            return "it is not KeyWord token !!";
        }
    }


    public String symbol(){
        if(tokenType().equals("SYMBOL"))
        {
            return cutToken;
        }else{
            return "Not Symbol token";// to take care id it not symbol token there is something wrong
        }
    }


    public String identifier(){
        if(tokenType().equals("IDENTIFIER"))
        {
            return cutToken;
        }else{
            return "its not IDENTIFIER you should not call this method with this token. ";
        }
    }


    public int intVal()
    {
            return Integer.parseInt(cutToken);//should be called just when tokenType is INT_CONST
    }

    public String stringVal()
    {
    if(tokenType().equals("STRING_CONST")){
        return cutToken.replaceAll("\"", "");
    }else{
        return "its not STRING_CONST you should not call this method with this token. ";
    }

    }

    public String getCutToken() {
        return cutToken;
    }


    private void splitLines(){
        for(String line:linesArray)
        {
            for(String s:line.split("\\s+"))
            {
                splitedLinesArray.add(s);
            }
        }
    }


    private void makeTokensArray(){
        String character = "";
        String constantString = "";
        String identifierString = "";
        for(int j =0;j<splitedLinesArray.size();j++)
        {
            String str = splitedLinesArray.get(j);
            for(int i = 0; i< str.length(); i++)
            {
//                System.out.println("%%%%%%%%%%%%%%");
//                System.out.println(str);
//                System.out.println("%%%%%%%%%%%%%%");

                character = String.valueOf(str.charAt(i));
                Pattern pSymbol = Pattern.compile(symbols);
                Matcher mathcSym = pSymbol.matcher(character);
                if(mathcSym.matches())
                {
                    if(!identifierString.equals(""))
                    {
                        tokensArray.add(identifierString);
                        identifierString = "";
                    }
                        tokensArray.add(character);
                }else if(character.equals("\""))
                {
                    if(!identifierString.equals(""))
                    {
                        tokensArray.add(identifierString);
                        identifierString = "";
                    }


//                    System.out.println("\n^^^^^^^^^^^^^^");

//                    System.out.println(character);
//                    System.out.println("^^^^^^^^^^^^^^\n");


                    if(str.equals("\""))
                    {
//                        System.out.println("fatfatfatfatfatfatfatfatfat");
                        str = splitedLinesArray.get(j+1);
//                        System.out.println(str);
//                        System.out.println("fatfatfatfatfatfatfatfatfat");

                        character = String.valueOf(str.charAt(0));
//                        i++;
//                        System.out.println(character);

                        j++;

                    }else if(str.endsWith("\"")) {
                        str = splitedLinesArray.get(j+1);
//                        System.out.println(str);
//                        System.out.println("fatfatfatfatfatfatfatfatfat");
//                        System.out.println(str.length());
//                        System.out.println(str);
//                        System.out.println(i);
                        character = String.valueOf(str.charAt(0));
//                        i++;
                        i = 0;
//                        System.out.println(character);

                        j++;

                    }else{
//                        System.out.println(str.length());
//                        System.out.println(str);
//                        System.out.println(i);
                        character = String.valueOf(str.charAt(i + 1));
                        i++;

                    }
                    while (!character.equals("\"") && i < str.length())
                    {
                        constantString = constantString+character;

//                        System.out.println("@@@@@@@@@@@@@@@@@@@@");
//                        System.out.println(str.length());
//                        System.out.println(str);
//                        System.out.println(i);
//                        System.out.println(constantString);
//                        System.out.println("@@@@@@@@@@@@@@@@@@@@");
                        if(i+1 > str.length()-1 && !character.equals("\""))
                        {
                            constantString = constantString + " ";
                            str = splitedLinesArray.get(++j);
                            i = -1;
                        }
//                        System.out.println("@@@@@@@@@@@@@@@@@@@@");
//                        System.out.println(str.length());
//                        System.out.println(str);
//                        System.out.println(i);
//                        System.out.println(constantString);
//                        System.out.println("@@@@@@@@@@@@@@@@@@@@");

                        character = String.valueOf(str.charAt(++i));

                    }
                    tokensArray.add("\""+constantString+"\"");
                    constantString = "";
                }else {
                    if(i == 0 )
                    {
                        tokensArray.add(identifierString);
                        identifierString = "";

                    }
                    identifierString = identifierString  + character;

                }


            }
        }
        tokensArray.removeAll(Collections.singleton(""));//remove all empty strings from tokensArray

    }

    public void close() throws IOException {
        reader.close();
    }

    /**
     * read the lines and add them to linesArray
     */
    private void readLines() throws IOException {
        String line = "";
        while((line = reader.readLine()) != null){
            Pattern emptyLine = Pattern.compile("\\s*");
            if(emptyLine.matcher(line).matches()|| line.equals(""))
            {
                continue;
            }

            linesArray.add(line);
        }
    }


    /**
     * this function clean the comments lines and uncesseru commenst from linesArray
     */
    private void cleanLines(){
//        Pattern strContainComment = Pattern.compile(".*\"([^\"]*//[^\"]*)+\".*");
//        Matcher m ;
//        String line, nextLine = "";
//        for(int i = 0; i < linesArray.size(); i++){
//            line = linesArray.get(i);
//            m = strContainComment.matcher(line);
//            if(!m.matches() && ((line.contains("//") || line.contains("/*")))) {
//                Pattern emptyLine = Pattern.compile("\\s*");
//                Matcher m2= emptyLine.matcher("$");
//                if((line.contains("//"))) {
//                    m2 = emptyLine.matcher(line.substring(0, line.indexOf("//")));
//                }else if((line.contains("/*")))
//                {
//                    m2 = emptyLine.matcher(line.substring(0, line.indexOf("/*")));
//                }
//                if (m2.matches()) {
//                    linesArray.set(i, "");
//                } else {
//                    linesArray.set(i, line.substring(0, line.indexOf("//")));
//                }
//                nextLine = linesArray.get(i+1);
//                Pattern lineStartStar = Pattern.compile("\\s*\\*.*");
//                while(lineStartStar.matcher(nextLine).matches())
//                {
//                    linesArray.set(i+1, "");
//                    i++;
//                    nextLine = linesArray.get(i+1);
//                }
//            }
//        }

        String line ;
        for(int i = 0; i < linesArray.size(); i++)
        {
            line = linesArray.get(i);
            int lineLen = line.length();
            for(int j = 0; j < lineLen-1; j++)
            {
//                System.out.println(line);
                if((line.charAt(j) == '/') && (line.charAt(j+1) == '/' ))
                {
//                    int strIdx = line.indexOf("//");
                    line = line.substring(0, j);
                    break;
                }else if((line.charAt(j) == '/') && (line.charAt(j+1) == '*' ))
                {
                    int strIdx = line.indexOf("/*");
                    int endIdx = line.indexOf("*/");
                    if(endIdx == -1)
                    {
                        while(!line.contains("*/"))
                        {
                            linesArray.set(i, "");
                            i++;
                            line = linesArray.get(i);
                        }
                        strIdx = 0;
                        endIdx = line.indexOf("*/");
                    }



                    String toRemove = line.substring(strIdx, endIdx+2);
                    line = line.replace(toRemove, "");
                    lineLen = line.length();
                    j = 0;
                }
            }
            linesArray.set(i, line);

        }


        linesArray.removeAll(Collections.singleton(""));//remove all empty strings from linesArray

    }

    public static void main2 (String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("there is something in the inputs arguments");
            return;
        }
        try {
            File fileIn = new File(args[0]);
            JackTokenizer a = new JackTokenizer(fileIn);
        }

        catch(Exception e){
            System.err.format("Exception occurred trying to read /Write");
            e.printStackTrace();
        }
//
    }

    public void back()
    {
        cutToken = tokensArray.get(curTokenNum-1);
        curTokenNum--;
    }

    public boolean isOperation(){
        Pattern lineStartStar = Pattern.compile(operation);

        return lineStartStar.matcher(cutToken).matches();

    }



}