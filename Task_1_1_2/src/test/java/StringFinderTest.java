import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

class StringFinderTest {
    /**
     * @param content File content
     * @return Absolute path of created file
     * @throws IOException Because IDEA
     */
    static String createTempFile(String content) throws IOException {
        var t = File.createTempFile("___", ".txt");
        t.deleteOnExit();

        var writer = new FileWriter(t);
        writer.write(content);

        writer.close();

        return t.getAbsolutePath();
    }


    /**
     * Helper function because JUnit cannot compare collections
     * @param res Return value of StringFinder.findSubstring()
     * @param nums Expected indexes
     */
    static void assertResult(ArrayList<Integer> res, Integer... nums) {
        assert (res.equals(Arrays.asList(nums)));
    }


    @Test
    void findSubstring() throws IOException {
        var filename = createTempFile("Hello, world!");
        assertResult(StringFinder.findSubstring(filename, "Hello"), 0);
        assertResult(StringFinder.findSubstring(filename, "World"));
        assertResult(StringFinder.findSubstring(filename, "world"), 7);

        filename = createTempFile("Some cool multiline text with\nsome cool words");
        assertResult(StringFinder.findSubstring(filename, "cool"), 5, 35);


        filename = createTempFile("aaabb");
        assertResult(StringFinder.findSubstring(filename, "aabb"), 1);
    }
}