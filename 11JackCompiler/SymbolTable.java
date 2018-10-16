import java.util.HashMap;
import java.util.HashSet;

public class SymbolTable {

    private HashMap<String, HashMap<String, Symbol>> symbolTable;


    public SymbolTable()
    {
        symbolTable = new HashMap<>();
        symbolTable.put("static", new HashMap<>());
        symbolTable.put("field", new HashMap<>());
        symbolTable.put("argument", new HashMap<>());
        symbolTable.put("var", new HashMap<>());
    }

    public void add(String name, String type, String kind)
    {

        Symbol s = new Symbol(kind, type, symbolTable.get(kind).size());

        if(kind.equals("static")){
            symbolTable.get("static").put(name, new Symbol(kind, type, symbolTable.get(kind).size()));
        } else if(kind.equals("field")){
            symbolTable.get("field").put(name, new Symbol(kind, type, symbolTable.get(kind).size()));
        } else if(kind.equals("argument")){
            symbolTable.get("argument").put(name, new Symbol(kind, type, symbolTable.get(kind).size()));
        } else if(kind.equals("var")){
            symbolTable.get("var").put(name, new Symbol(kind, type, symbolTable.get(kind).size()));
        }

    }

    public void clear(){
        for(HashMap<String, Symbol> map1 : symbolTable.values()){
            map1.clear();
        }
    }

    public void clearSub(){
        symbolTable.get("var").clear();
        symbolTable.get("argument").clear();
    }

    public int VarCount(String kind){
        return symbolTable.get(kind).size();
    }

    public String kindOf(String name){
        Symbol symbol = find(name);
        if(symbol != null){
            return symbol.getKind();
        }

        return "";
    }

    public String typeOf(String name){
        Symbol symbol = find(name);
        if(symbol != null){
            return symbol.getType();
        }

        return "";
    }

    public int indexOf(String name){

        Symbol symbol = find(name);
        if(symbol != null){
            return symbol.getIndex();
        }

        return -1;
    }

    public Symbol find(String name){
        if(symbolTable.get("var").containsKey(name)){
                return symbolTable.get("var").get(name);
        } else if(symbolTable.get("argument").containsKey(name)){
            return symbolTable.get("argument").get(name);
        } else if(symbolTable.get("static").containsKey(name)){
            return symbolTable.get("static").get(name);
        } else if(symbolTable.get("field").containsKey(name)){
            return symbolTable.get("field").get(name);
        }

        return null;
    }

}

