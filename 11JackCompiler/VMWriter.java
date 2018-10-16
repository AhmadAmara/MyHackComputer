import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class VMWriter {

//    private FileOutputStream fileOutputStream;
    private FileWriter outputFile;

    public VMWriter(File file) throws IOException {
        outputFile = new FileWriter(file.getAbsolutePath().replaceAll(".jack", ".vm"));


    }

    public void writePush(String segment, int index) throws IOException {

        outputFile.write("push " + segment + " " + Integer.toString(index) + "\n");
    }

    public void writePop(String segment, int index) throws IOException {

        outputFile.write("pop " + segment + " " + Integer.toString(index) + "\n");
    }

    public void writeArithmetic(String command) throws IOException {
        outputFile.write(command + "\n");
    }

    public void writeLabel(String label) throws IOException {
        outputFile.write("label " + label + "\n");
    }

    public void writeGoto(String label) throws IOException {
        outputFile.write("goto " + label + "\n");
    }

    public void writeIf(String label) throws IOException {
        outputFile.write("if-goto " + label + "\n");
    }

    public void writeCall(String name ,int nArgs) throws IOException {
        outputFile.write("call "+ name + " "+ nArgs+ "\n");
    }


    public void writeFunction(String name ,int nLocals) throws IOException {
        outputFile.write("function "+ name + " "+ nLocals+ "\n");
    }

    public void writeReturn() throws IOException {
        outputFile.write("return"+"\n");
    }

    public void close() throws IOException {
        outputFile.close();
    }
}
