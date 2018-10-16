import java.io.IOException;

import java.io.*;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class CompilationEngine {
    private static Element currentParent;
    private static JackTokenizer curtokenizer;
    private FileOutputStream fileOutStream;
    private static Document doc;
    private static int depth;

    public CompilationEngine(File inFile) throws ParserConfigurationException, IOException {
        this.curtokenizer = new JackTokenizer(inFile);

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        fileOutStream = new FileOutputStream(inFile.getAbsolutePath().replaceAll(".jack","")+".xml");
        doc = docBuilder.newDocument();
    }

    public void CompileClass() throws IOException, TransformerException {
        Element rootElement = doc.createElement("class");
        doc.appendChild(rootElement);
        currentParent = rootElement;
        depth = 0;
        writeIdKeylDoc();
        writeIdKeylDoc();
        writeSymbolDoc();
        while(curtokenizer.getCutToken().equals("field") || curtokenizer.getCutToken().equals("static"))
        {
            CompileClassVarDec();
        }
        while( curtokenizer.keyWord().equals("FUNCTION")||curtokenizer.keyWord().equals("CONSTRUCTOR")||curtokenizer.keyWord().equals("METHOD"))
        {
            CompileSubroutine();
        }
        writeSymbolDoc();
        curtokenizer.close();
        printDocument(doc, fileOutStream);

    }

    private static void printDocument(Document doc, OutputStream out) throws TransformerException, UnsupportedEncodingException {

        TransformerFactory tf = TransformerFactory.newInstance();

        Transformer transformer = tf.newTransformer();

        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");


        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        transformer.transform(new DOMSource(doc),
                new StreamResult(new OutputStreamWriter(out, "UTF-8")));

    }

    private void writeSymbolDoc() {
        Element endVarLine = doc.createElement("symbol");
        currentParent.appendChild(endVarLine);
        endVarLine.appendChild(doc.createTextNode(" " + curtokenizer.getCutToken() + " "));
        curtokenizer.advance();
    }

    private void writeIdKeylDoc(){
        if(curtokenizer.tokenType().equals("KEYWORD")){
            Element classKeyword = doc.createElement("keyword");
            classKeyword.appendChild(doc.createTextNode(" " +curtokenizer.getCutToken()+ " "));
            currentParent.appendChild(classKeyword);
            curtokenizer.advance();
        }
        else if(curtokenizer.tokenType().equals("IDENTIFIER")){
            Element className = doc.createElement("identifier");
            className.appendChild(doc.createTextNode(" "+curtokenizer.getCutToken()+" "));
            currentParent.appendChild(className);
            curtokenizer.advance();
        }
    }

    public void CompileClassVarDec() {
        Element classVarDec = doc.createElement("classVarDec");
        currentParent.appendChild(classVarDec);
        Element copyParent = currentParent;
        currentParent = classVarDec;
        depth++;
        writeIdKeylDoc();
        writeIdKeylDoc();
        writeIdKeylDoc();
        while(!curtokenizer.getCutToken().equals(";")) {
            if(curtokenizer.getCutToken().equals(",")){
                writeSymbolDoc();
            }
            writeIdKeylDoc();
        }
        writeSymbolDoc();
        currentParent = copyParent;
        depth--;
    }


    public void writeSubBodyDoc() throws IOException {

        Element SubroutineBody = doc.createElement("subroutineBody");
        currentParent.appendChild(SubroutineBody);
        Element copyParent = currentParent;
        currentParent = SubroutineBody;
        depth++;
        writeSymbolDoc();
        if(!curtokenizer.getCutToken().equals("}")){
            CompileVarDec();
            while (!curtokenizer.getCutToken().equals("}")){
                CompileStatements();
            }
        }
        writeSymbolDoc();
        currentParent = copyParent;
        depth--;

    }

    public void CompileSubroutine() throws IOException {
        Element classSubroutine = doc.createElement("subroutineDec");
        currentParent.appendChild(classSubroutine);
        Element copyParent = currentParent;
        currentParent = classSubroutine;
        depth++;
        writeIdKeylDoc();
        writeIdKeylDoc();
        writeIdKeylDoc();
        writeSymbolDoc();
        CompileParameterList();
        writeSymbolDoc();
        writeSubBodyDoc();
        currentParent = copyParent;
        depth--;

    }

    private String clacSpaces(){
        String spaces = "";
        for(int i =0; i < depth; i++)
        {
            spaces += "  ";
        }
        return spaces;
    }

    public void CompileParameterList(){
        Element parameter = doc.createElement("parameterList");
        currentParent.appendChild(parameter);
        Element copyParent = currentParent;
        currentParent = parameter;
        depth++;
        if(curtokenizer.getCutToken().equals(")"))
        {
            parameter.appendChild(doc.createTextNode("\n"+ clacSpaces()));
        }

        while(!curtokenizer.getCutToken().equals(")"))
        {
            writeIdKeylDoc();
            writeIdKeylDoc();
            if(curtokenizer.getCutToken().equals(","))
            {
                writeSymbolDoc();
            }
        }

        currentParent = copyParent;
        depth--;

    }

    public void CompileVarDec() {
        while(curtokenizer.keyWord().equals("VAR"))
        {
            Element varBody = doc.createElement("varDec");
            currentParent.appendChild(varBody);
            Element copyParent = currentParent;
            currentParent = varBody;
            depth++;
            writeIdKeylDoc();
            writeIdKeylDoc();
            while(!curtokenizer.getCutToken().equals(";"))
            {
                if(curtokenizer.getCutToken().equals(",")){
                    writeSymbolDoc();
                }
                writeIdKeylDoc();
                if(curtokenizer.getCutToken().equals("=")){
                    writeSymbolDoc();
                    writeIdKeylDoc();
                }
            }
            writeSymbolDoc();
            currentParent = copyParent;
            depth--;

        }
    }



    private void CompileElse() throws IOException {
        writeIdKeylDoc();
        writeSymbolDoc();
        /*else may conatin statments*/
        CompileStatements();
        writeSymbolDoc();
    }

    public void CompileDo() throws IOException {
        Element doSta = doc.createElement("doStatement");
        currentParent.appendChild(doSta);
        Element copyParent = currentParent;
        currentParent = doSta;
        depth++;
        writeIdKeylDoc();
        writeIdKeylDoc();
        if(curtokenizer.getCutToken().endsWith(".")){
            writeSymbolDoc();
            writeIdKeylDoc();
        }
        writeSymbolDoc();
        CompileExpressionList();
        writeSymbolDoc();
        writeSymbolDoc();
        currentParent = copyParent;
        depth--;

    }

    public void CompileLet() throws IOException {

        Element letSta = doc.createElement("letStatement");
        currentParent.appendChild(letSta);
        Element copyParent = currentParent;
        currentParent = letSta;
        depth++;
        writeIdKeylDoc();
        writeIdKeylDoc();
        if(curtokenizer.getCutToken().equals("[")){
            writeSymbolDoc();
            writeEquatationDoc();
            writeSymbolDoc();
        }
        writeSymbolDoc();

        writeEquatationDoc();
        writeSymbolDoc();
        currentParent = copyParent;
        depth--;

    }



    public void CompileWhile() throws IOException {

        Element wihiletSta = doc.createElement("whileStatement");
        currentParent.appendChild(wihiletSta);
        Element copyParent = currentParent;
        currentParent = wihiletSta;
        depth++;

        writeIdKeylDoc();
        writeSymbolDoc();
        writeEquatationDoc();
        writeSymbolDoc();
        writeSymbolDoc();
        CompileStatements();
        writeSymbolDoc();

        currentParent = copyParent;
        depth--;

    }

    public void CompileReturn() throws IOException {


        Element returnStatement = doc.createElement("returnStatement");
        currentParent.appendChild(returnStatement);
        Element copyParent = currentParent;
        currentParent = returnStatement;
        depth++;

        writeIdKeylDoc();
        if(!curtokenizer.getCutToken().equals(";")){
            writeEquatationDoc();
        }
        writeSymbolDoc();

        currentParent = copyParent;
        depth--;

    }

    public void CompileIf() throws IOException {
        Element copyParent = currentParent;

        if(curtokenizer.getCutToken().equals("if")){


            Element ifStatement = doc.createElement("ifStatement");
            currentParent.appendChild(ifStatement);
            currentParent = ifStatement;
            depth++;

            writeIdKeylDoc();
            writeSymbolDoc();
            writeEquatationDoc();
            writeSymbolDoc();
            writeSymbolDoc();
            CompileStatements();
            writeSymbolDoc();
        }
        if(curtokenizer.getCutToken().equals("else")){
            CompileElse();
        }

        currentParent = copyParent;
        depth--;

    }
    public void CompileStatements() throws IOException {
        Element Sta = doc.createElement("statements");
        currentParent.appendChild(Sta);
        Element copyParent = currentParent;
        currentParent = Sta;
        depth++;
        while(!curtokenizer.getCutToken().equals("}"))
        {

            if(curtokenizer.keyWord().equals("IF")){
                CompileIf();
            }else if(curtokenizer.keyWord().equals("LET")){
                CompileLet();
            }else if(curtokenizer.keyWord().equals("RETURN"))
            {
                CompileReturn();
            }else if(curtokenizer.keyWord().equals("WHILE")){
                CompileWhile();

            }else if(curtokenizer.keyWord().equals("DO")){
                CompileDo();
            }
        }
        currentParent = copyParent;
        depth--;

    }
    public void CompileTerm() throws IOException {

        Element termSta = doc.createElement("term");
        currentParent.appendChild(termSta);
        Element copyParent = currentParent;
        currentParent = termSta;
        depth++;

        if(curtokenizer.tokenType().equals("IDENTIFIER")) {
            writeIdKeylDoc();
            if(curtokenizer.getCutToken().equals(".")){
                writeSymbolDoc();
                writeIdKeylDoc();
                writeSymbolDoc();
                CompileExpressionList();
                writeSymbolDoc();
            }else if(curtokenizer.getCutToken().equals("(")){
                writeSymbolDoc();
                CompileExpressionList();
                writeSymbolDoc();
            } else if(curtokenizer.getCutToken().equals("[")){
                writeSymbolDoc();
                writeEquatationDoc();
                writeSymbolDoc();
            }
        }else if(curtokenizer.tokenType().equals("STRING_CONST")){

            Element constString = doc.createElement("stringConstant");
            constString.appendChild(doc.createTextNode(" " +curtokenizer.getCutToken().substring(1,curtokenizer.getCutToken().length()-1) + " "));
            currentParent.appendChild(constString);

            curtokenizer.advance();
        }
        else if(curtokenizer.tokenType().equals("INT_CONST")) {

            Element classKeyword = doc.createElement("integerConstant");
            classKeyword.appendChild(doc.createTextNode(" " +curtokenizer.getCutToken()+ " "));//getcurrent!??????????????????
            currentParent.appendChild(classKeyword);

            curtokenizer.advance();
        }else if(curtokenizer.tokenType().equals("KEYWORD")){
            writeIdKeylDoc();
        }else if(curtokenizer.getCutToken().equals("(")){
            writeSymbolDoc();
            writeEquatationDoc();
            writeSymbolDoc();
        }else if(curtokenizer.getCutToken().equals("~")){
            writeSymbolDoc();
            CompileTerm();
        }

        currentParent = copyParent;
        depth--;

    }

    private void writeEquatationDoc() throws IOException {
        int depthIn = 0;
        boolean negFlag = false;
        Element expressionSta = doc.createElement("expression");
        currentParent.appendChild(expressionSta);
        Element copyParent = currentParent;
        currentParent = expressionSta;
        depth++;
        while (!curtokenizer.getCutToken().equals(")") && !curtokenizer.getCutToken().equals(",") &&
                !curtokenizer.getCutToken().equals(";") && !curtokenizer.getCutToken().equals("]")) {
            if (curtokenizer.getCutToken().equals("[") || curtokenizer.getCutToken().equals("~")
                    || curtokenizer.getCutToken().equals("(")) {
                if (depthIn != 0 || !curtokenizer.getCutToken().equals("-")) {
                    CompileTerm();
                }
            } else if (!curtokenizer.tokenType().equals("SYMBOL")) {
                CompileTerm();
            }
            else {
                if (curtokenizer.getCutToken().equals("-") && depthIn == 0) {
                    Element termSta = doc.createElement("term");
                    currentParent.appendChild(termSta);
                    Element copyParent2 = currentParent;
                    currentParent = termSta;
                    depth++;
                    writeSymbolDoc();
                    CompileTerm();
                    currentParent = copyParent2;
                    depth--;
                } else {
                    if (curtokenizer.getCutToken().equals("(") || curtokenizer.getCutToken().equals("&gt;") ||
                            curtokenizer.getCutToken().equals("&lt;") || curtokenizer.getCutToken().equals("=")) {
                        negFlag = true;
                        depthIn = 0;
                    }
                    writeSymbolDoc();
                }

            }
            if (negFlag != true) {
                depthIn++;
            } else {
                negFlag = false;
                depthIn = 0;
            }
        }
        currentParent = copyParent;
        depth--;
    }


//    private void writeTermDoc() throws IOException
//    {
//        Element termSta = doc.createElement("term");
//        currentParent.appendChild(termSta);
//        Element copyParent2 = currentParent;
//        currentParent = termSta;
//        depth++;
//        System.out.println(curtokenizer.getCutToken());
//        writeSymbolDoc();
//        CompileTerm();
//        currentParent = copyParent2;
//        depth--;
//    }


    public void CompileExpressionList() throws IOException {

        Element expressionListSta = doc.createElement("expressionList");
        currentParent.appendChild(expressionListSta);
        Element copyParent = currentParent;
        currentParent = expressionListSta;
        depth++;

        if(curtokenizer.getCutToken().equals(")"))
        {
            expressionListSta.appendChild(doc.createTextNode("\n"+ clacSpaces()));
        }

        while (!curtokenizer.getCutToken().equals(")")){
            writeEquatationDoc();
            while(!curtokenizer.getCutToken().equals(")")){
                if(curtokenizer.getCutToken().equals(",")){
                    writeSymbolDoc();
                    writeEquatationDoc();
                }
            }

        }
        currentParent = copyParent;
        depth--;
    }



    public static void main(String[] args) throws IOException, TransformerException, ParserConfigurationException {

        ArrayList<File> jacksArray = new ArrayList<File>();
        File folder = new File(args[0]);
        File[] ListFiles = folder.listFiles();
        if (folder.isDirectory()) {
            for (File f : ListFiles) {
                String fileName = f.getName();
                if (f.isFile() && fileName.endsWith(".jack")) {
                    jacksArray.add(f);
                }
            }

            for (File file : jacksArray) {
                try {
                    CompilationEngine comp = new CompilationEngine(file);
                    comp.CompileClass();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            if (folder.isFile() && folder.getName().endsWith(".jack")) {
                CompilationEngine comp = new CompilationEngine(folder);
                comp.CompileClass();
            }
        }
    }
    }




