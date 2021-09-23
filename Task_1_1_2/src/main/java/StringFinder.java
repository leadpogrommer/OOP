import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Class with cool method for finding strings in files
 */
public class StringFinder {
    /**
     * Finds all occupancies of string in files
     * @param filepath Path to the file where we should search for strinf
     * @param substring String that should be searched
     * @return ArrayList with string positions
     * @throws IOException Because IDEA wants me to write 'throws'
     */
    public static ArrayList<Integer> findSubstring(String filepath, String substring) throws IOException {
        var file = new FileReader(filepath);

        var ret = new ArrayList<Integer>();

        int substrPos = 0;
        int filePos = 0;
        int currentIndex = 0;
        while (true) {
            var nextChar = file.read();
            filePos++;
            if (nextChar == -1) break;
            if (nextChar == substring.charAt(substrPos)) {
                substrPos++;
            } else {
                substrPos = 0;
                currentIndex = filePos;
            }
            if (substrPos == substring.length()) {
                substrPos = 0;
                ret.add(currentIndex);
            }
        }

        return ret;
    }
}
