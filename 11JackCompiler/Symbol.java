public class Symbol {

    private String kind, type;
    private int index;

    public Symbol(String kind, String type, int index){
        this.kind = kind;
        this.type = type;
        this.index = index;
    }


    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
