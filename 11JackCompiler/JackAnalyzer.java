import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class JackAnalyzer {

    private static ArrayList<File> getJackFiles(String input) {
        ArrayList<File> jackFiles = new ArrayList<>();
        if (input.endsWith(".jack")) // got only one file
        {
            jackFiles.add(new File(input));
        } else // got a directory
        {
            File dir = new File(input);
            File[] directoryListing = dir.listFiles();
            if (directoryListing != null) {
                for (File child : directoryListing) {
                    input = child.getPath();
                    if (input.endsWith(".jack")) {
                        jackFiles.add(new File(input));
                    }
                }
            }
        }
        return jackFiles;
    }

    public static void main(String[] args) throws IOException {

        ArrayList<File> jackFiles = getJackFiles(args[0]);
        for(File file: jackFiles)
        {
            CompilationEngine compilationEngine = new CompilationEngine(file);
            compilationEngine.compileClass();
        }

    }
}
