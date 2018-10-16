import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class CompilationEngine {

    private Tokenizer tknzr;
    private VMWriter vmWriter;
    private int ifCounter = 0;
    private int whileCounter = 0;
    private int counterForLocals = 0;
    private SymbolTable symbolTable;
    private String currentClass;
    private String nextToken = "";
    private String nextTokenType = "";
    private String methodOrConstructorOrFunction = "";
    private String currentFunctionName = "";
    private static HashMap<String, String> mapForOperations = new HashMap<>();

    static {

        mapForOperations.put("+", "add");
        mapForOperations.put("-", "sub");
        mapForOperations.put("*", "Math.multiply");
        mapForOperations.put("/", "Math.divide");
        mapForOperations.put("|", "or");
        mapForOperations.put("=", "eq");
        mapForOperations.put("&lt;", "lt");
        mapForOperations.put("&gt;", "gt");
        mapForOperations.put("&amp;", "and");
        
    }


    public CompilationEngine(File jackFile) throws IOException {
        tknzr = new Tokenizer(jackFile);
        vmWriter = new VMWriter(jackFile);
        symbolTable = new SymbolTable();
    }


    //DONE
    public void compileClass() throws IOException {

        tknzr.advance();
        tknzr.advance();
        currentClass = tknzr.getCurrentToken();
        tknzr.advance();
        updateNextToken();

        while(nextToken.equals("field") || nextToken.equals("static")){
            compileVarDec();
            updateNextToken();
        }

        updateNextToken();

        while(nextToken.equals("method") || nextToken.equals("constructor") || nextToken.equals("function")){
            compileSubroutineDecAndBody();
            updateNextToken();
        }

        tknzr.advance();

        //todo
        symbolTable.clear();

        vmWriter.close();
    }

    //DONE
    // (constructor|method|function) (void|type) name (parameter list) body
    private void compileSubroutineDecAndBody() throws IOException
    {
        tknzr.advance();
        methodOrConstructorOrFunction = tknzr.getCurrentToken();
        tknzr.advance();
        tknzr.advance();
        currentFunctionName = currentClass + "." + tknzr.getCurrentToken();
        symbolTable.clearSub();

        //todo
        whileCounter = 0;
        ifCounter = 0;


        tknzr.advance();
        compileParameterList();
        tknzr.advance();
        tknzr.advance();
        updateNextToken();

        while(nextToken.equals("var")){
            compileVarDec();
            updateNextToken();
        }
        vmWriter.writeFunction(currentFunctionName, symbolTable.VarCount("var"));

        int numOfLocals = 0;

        if(methodOrConstructorOrFunction.equals("method")){
            vmWriter.writePush("argument", 0);
            vmWriter.writePop("pointer", 0);
        }
        if(methodOrConstructorOrFunction.equals("constructor")){
            numOfLocals += symbolTable.VarCount("field");
            vmWriter.writePush("constant", numOfLocals);
            vmWriter.writeCall("Memory.alloc", 1);
            vmWriter.writePop("pointer", 0);
        }

        compileStatements();

        tknzr.advance();

    }

    //DONE
    private void compileParameterList() // (parameter (, parameter)*)?
    {
        if(methodOrConstructorOrFunction.equals("method")){
            symbolTable.add("this", "self", "argument");
        }
        updateNextToken();

        while(!nextTokenType.equals("symbol")){

            tknzr.advance();
            String type = tknzr.getCurrentToken();
            tknzr.advance();
            String tempName = tknzr.getCurrentToken();
            symbolTable.add(tempName, type, "argument");
            updateNextToken();

            if(nextToken.equals(",")){
                tknzr.advance();
                updateNextToken();
            }
        }
    }

    //DONE
    private void compileVarDec() // type varname (, varname)* ;
    {
        tknzr.advance();
        String kind = tknzr.getCurrentToken();
        tknzr.advance();
        String type = tknzr.getCurrentToken();
        tknzr.advance();
        String tempName = tknzr.getCurrentToken();
        symbolTable.add(tempName, type, kind);
        updateNextToken();

        while(nextToken.equals(",")){
            tknzr.advance();
            tknzr.advance();
            tempName = tknzr.getCurrentToken();
            symbolTable.add(tempName, type, kind);
            updateNextToken();
        }
        tknzr.advance();
    }
    //DONE
    private void compileStatements() throws IOException {

        updateNextToken();
         while(nextToken.equals("do") || nextToken.equals("let") || nextToken.equals("if") ||
                 nextToken.equals("while") || nextToken.equals("return")){

             if(nextToken.equals("do")){
                 compileDo();
                 updateNextToken();
             } else if(nextToken.equals("let")){
                 compileLet();
                 updateNextToken();
             } else if(nextToken.equals("if")){
                 compileIf();
                 updateNextToken();
             } else if(nextToken.equals("while")){
                 compileWhile();
                 updateNextToken();
             } else if(nextToken.equals("return")){
                 compileReturn();
                 updateNextToken();
             }
             updateNextToken();

         }

    }
    //DONE
    private void compileDo() throws IOException {

        int localsNum = 0;

        tknzr.advance();
        tknzr.advance();

        String tempName = tknzr.getCurrentToken();

        updateNextToken();

        String nameOfFunc = "";

        if(nextToken.equals(".")){
            tknzr.advance();
            tknzr.advance();

            String nameOfClass = tknzr.getCurrentToken();
            Symbol symbol = symbolTable.find(tempName);

            if(symbol != null){
                vmWriter.writePush(findSegment(symbol.getKind()), symbol.getIndex());
                nameOfFunc = symbol.getType() + "." + nameOfClass;
                localsNum++;
            } else {
                nameOfFunc = tempName + "." + nameOfClass;
            }
        } else {
            vmWriter.writePush("pointer", 0);
            localsNum++;
            nameOfFunc = currentClass + "." + tempName;
        }
        tknzr.advance();
        compileExpressionList();
        vmWriter.writeCall(nameOfFunc, localsNum + counterForLocals);
        tknzr.advance();
        vmWriter.writePop("temp", 0);
        tknzr.advance();

    }

    //DONE
    private void compileLet() throws IOException {

        boolean hasArray = false;

        tknzr.advance();
        tknzr.advance();

        String tempName = tknzr.getCurrentToken();
        updateNextToken();

        if(nextToken.equals("[")){
            hasArray = true;
            tknzr.advance();
            compileExpression();
            tknzr.advance();
            Symbol tempSymbol = symbolTable.find(tempName);
            vmWriter.writePush(findSegment(tempSymbol.getKind()), tempSymbol.getIndex());
            vmWriter.writeArithmetic("add");
        }
        tknzr.advance();
        compileExpression();

        if(hasArray){
            vmWriter.writePop("temp", 0);
            vmWriter.writePop("pointer", 1);
            vmWriter.writePush("temp", 0);
            vmWriter.writePop("that", 0);
        } else {
            Symbol symbol = symbolTable.find(tempName); //TODO
            vmWriter.writePop(findSegment(symbol.getKind()), symbol.getIndex());
        }
        tknzr.advance();
    }
    //DONE
    private void compileWhile() throws IOException {

        whileCounter++;
        String whileStr = Integer.toString(whileCounter);
        vmWriter.writeLabel("WHILE_" + whileStr);
        tknzr.advance();
        tknzr.advance();
        compileExpression();
        vmWriter.writeArithmetic("not");
        vmWriter.writeIf("finish_While_" + whileStr);
        tknzr.advance();
        tknzr.advance();
        compileStatements();
        vmWriter.writeGoto("WHILE_" + whileStr);
        vmWriter.writeLabel("finish_While_" + whileStr);
        tknzr.advance();

    }
    //DONE
    private void compileReturn() throws IOException {
        tknzr.advance();

        boolean hasReturn = false;
        updateNextToken();

        while(nextTokenType.equals("intConst") || nextTokenType.equals("stringConst") ||
                nextTokenType.equals("identifier") || nextToken.equals("-") || nextToken.equals("~")
                || nextToken.equals("true")  || nextToken.equals("false") || nextToken.equals("null") ||
                nextToken.equals("this") || nextToken.equals("(")){

            hasReturn = true;
            compileExpression();
            updateNextToken();
        }

        if(!hasReturn){
            vmWriter.writePush("constant", 0);
        }
        vmWriter.writeReturn();
        tknzr.advance();
    }
    //DONE
    private void compileIf() throws IOException {
        tknzr.advance();
        tknzr.advance();
        compileExpression();
        tknzr.advance();

        ifCounter++;
        String countIfStr = Integer.toString(ifCounter);

        vmWriter.writeIf("TRUE_CONDITION_" + countIfStr);

        vmWriter.writeGoto("FALSE_CONDITION_" + countIfStr);
        vmWriter.writeLabel("TRUE_CONDITION_" + countIfStr);

        tknzr.advance();
        compileStatements();
        tknzr.advance();

        updateNextToken();

        if(nextToken.equals("else")){
            vmWriter.writeGoto("END_OF_CONDITION_" + countIfStr);
            vmWriter.writeLabel("FALSE_CONDITION_" + countIfStr);
            tknzr.advance();
            tknzr.advance();
            compileStatements();
            tknzr.advance();
            vmWriter.writeLabel("END_OF_CONDITION_" + countIfStr);
        } else {
            vmWriter.writeLabel("FALSE_CONDITION_" + countIfStr);
        }
    }
    //DONE
    private void compileExpression() throws IOException {

        compileTerm();

        updateNextToken();

        while(mapForOperations.containsKey(nextToken)){
            tknzr.advance();
            String operation = tknzr.getCurrentToken();
            compileTerm();

            if(operation.equals("*") || operation.equals("/")){
                vmWriter.writeCall(mapForOperations.get(operation), 2);
                updateNextToken();
            } else {
                vmWriter.writeArithmetic(mapForOperations.get(operation));
                updateNextToken();
            }
        }
    }
    //DONE
    private void updateNextToken(){
        tknzr.advance();
        nextToken = tknzr.getCurrentToken();
        nextTokenType = tknzr.tokenType();
        tknzr.reverse();
    }

    //DONE
    private void compileTerm() throws IOException {

        boolean hasArray = false;
        updateNextToken();
        if(nextTokenType.equals("intConst")){
            tknzr.advance();
            String s = tknzr.getCurrentToken();
            vmWriter.writePush("constant", Integer.parseInt(s));
        } else if(nextToken.equals("true") || nextToken.equals("false") ||
                nextToken.equals("null") || nextToken.equals("this")){
            tknzr.advance();
            String s = tknzr.getCurrentToken();
            if(s.equals("this")){
                vmWriter.writePush("pointer", 0);
            } else {
                vmWriter.writePush("constant", 0);
                if(s.equals("true")){
                    vmWriter.writeArithmetic("not");
                }
            }
        } else if(nextTokenType.equals("stringConst")){
            tknzr.advance();
            String s = removeQuotations(tknzr.getCurrentToken());

            int length1 = 0;
            for (int i = 0; i < s.length(); i++){
                char c = s.charAt(i);
                if(c=='\n' || c=='\b'|| c=='\r'|| c=='\t'){
                    length1++;
                    length1++;
                } else {
                    length1++;
                }
            }

            vmWriter.writePush("constant", length1);
            vmWriter.writeCall("String.new", 1);

            for (int i = 0; i < s.length(); i++){
                char c = s.charAt(i);
                if(c=='\n' || c=='\b'|| c=='\r'|| c=='\t'){

                    int m;
                    char escape = '\\';
                    m = (int) escape;
                    vmWriter.writePush("constant", m);
                    vmWriter.writeCall("String.appendChar", 2);

                    if(c=='\n'){
                        int n1;
                        char newLine = 'n';
                        n1 = (int) newLine;
                        vmWriter.writePush("constant", n1);
                        vmWriter.writeCall("String.appendChar", 2);

                    } else if(c=='\b'){
                        int n1;
                        char newLine = 'b';
                        n1 = (int) newLine;
                        vmWriter.writePush("constant", n1);
                        vmWriter.writeCall("String.appendChar", 2);

                    } else if(c=='\r'){
                        int n1;
                        char newLine = 'r';
                        n1 = (int) newLine;
                        vmWriter.writePush("constant", n1);
                        vmWriter.writeCall("String.appendChar", 2);

                    } else {
                        int n1;
                        char newLine = 't';
                        n1 = (int) newLine;
                        vmWriter.writePush("constant", n1);
                        vmWriter.writeCall("String.appendChar", 2);

                    }
                } else {
                    int n;
                    n = (int) c;
                    vmWriter.writePush("constant", n);
                    vmWriter.writeCall("String.appendChar", 2);

                }
            }
        } else if(nextTokenType.equals("identifier")){
            int localsNum = 0;
            tknzr.advance();
            String tempName = tknzr.getCurrentToken();

            updateNextToken();

            if(nextToken.equals("[")){
                hasArray = true;
                tknzr.advance();
                compileExpression();
                tknzr.advance();

                if(symbolTable.kindOf(tempName).equals("var")){
                    vmWriter.writePush("local", symbolTable.indexOf(tempName));
                } else if(symbolTable.kindOf(tempName).equals("argument")){
                    vmWriter.writePush("argument", symbolTable.indexOf(tempName));
                } else if(symbolTable.kindOf(tempName).equals("static")){
                    vmWriter.writePush("static", symbolTable.indexOf(tempName));
                } else if(symbolTable.kindOf(tempName).equals("field")){
                    vmWriter.writePush("this", symbolTable.indexOf(tempName));
                }

                vmWriter.writeArithmetic("add");
                updateNextToken();
            }
            if(nextToken.equals("(")){
                localsNum++;
                vmWriter.writePush("pointer", 0);
                tknzr.advance();
                compileExpressionList();
                localsNum = localsNum + counterForLocals;
                tknzr.advance();
                vmWriter.writeCall(currentClass + "." + Integer.toString(localsNum), 0);
            } else if(nextToken.equals(".")){
                tknzr.advance();
                tknzr.advance();
                String nameOfFunction = tknzr.getCurrentToken();
                Symbol symbol = symbolTable.find(tempName);
                if(symbol != null){
                    vmWriter.writePush(findSegment(symbol.getKind()), symbol.getIndex());
                    tempName = symbolTable.typeOf(tempName) + "." + nameOfFunction;
                    localsNum++;
                } else {
                    tempName = tempName + "." + nameOfFunction;
                }
                tknzr.advance();
                compileExpressionList();
                localsNum = localsNum + counterForLocals;
                tknzr.advance();
                vmWriter.writeCall(tempName, localsNum);
            } else {
                if(hasArray){
                    vmWriter.writePop("pointer", 1);
                    vmWriter.writePush("that", 0);
                } else {
                    Symbol tempSymbol = symbolTable.find(tempName);
                    vmWriter.writePush(findSegment(tempSymbol.getKind()), tempSymbol.getIndex());
                }
            }
        } else if(nextToken.equals("-") || nextToken.equals("~")){
            tknzr.advance();
            String unaryOperation = "";
            if(nextToken.equals("-")){
                unaryOperation = "neg";
            } else {
                unaryOperation = "not";
            }
            compileTerm();
            vmWriter.writeArithmetic(unaryOperation);
        } else if(nextToken.equals("(")){
            tknzr.advance();
            compileExpression();
            tknzr.advance();
        }
    }

    //DONE
    private void compileExpressionList() throws IOException {
       int numOfExpressions = 0;

       updateNextToken();

       if(nextToken.equals("-") || nextToken.equals("~") || nextToken.equals("true") || nextToken.equals("false") ||
               nextToken.equals("null") || nextToken.equals("this") || nextToken.equals("(") ||
               nextTokenType.equals("intConst") || nextTokenType.equals("stringConst") ||
               nextTokenType.equals("identifier")){

           compileExpression();
           numOfExpressions++;

           updateNextToken();
       }

       while (nextToken.equals(",")){
           tknzr.advance();
           compileExpression();
           numOfExpressions++;
           updateNextToken();
       }
        counterForLocals = numOfExpressions;
    }

    //DONE
    private String findSegment(String kind){
        if(kind.equals("field")){
            return "this";
        } else if(kind.equals("var")){
            return "local";
        }
        else if(kind.equals("")){
            return "";
        } else {
            return kind;
        }
    }

    //DONE
    private String removeQuotations(String val)
    {
        return val.substring(val.indexOf('"')+1, val.lastIndexOf('"'));
    }
}

