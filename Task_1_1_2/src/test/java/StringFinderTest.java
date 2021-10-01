import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class StringFinderTest {
    static Stream<Arguments> getStrings() {
        return Stream.of(
                Arguments.of("Hello, world!", "Hello", new int[]{0}),
                Arguments.of("Hello, world!", "World", new int[]{}),
                Arguments.of("Hello, world!", "world", new int[]{7}),
                Arguments.of("aaabb", "aabb", new int[]{1}),
                Arguments.of("Some cool multiline text with\nsome cool words", "cool", new int[]{5, 35}),
                Arguments.of("", "a", new int[]{}),
                Arguments.of("cc", "cc", new int[]{0})
        );
    }


    /**
     * @param haystack String to search in
     * @param needle   String to search for
     * @param nums     Array of expected indexes
     * @throws IOException On temporary file creation error
     */
    @ParameterizedTest(name = "[{index}] Search {1} in {0}")
    @MethodSource("getStrings")
    void findSubstring(String haystack, String needle, int[] nums) throws IOException {
        File f = null;
        try {
            f = File.createTempFile("---", "---");
            var writer = new FileWriter(f);
            writer.write(haystack);

            writer.close();

            var res = StringFinder.findSubstring(f.getPath(), needle).stream().mapToInt(Integer::intValue).toArray();
            assertArrayEquals(nums, res);

        } finally {
            if (f != null) //noinspection ResultOfMethodCallIgnored
                f.delete();
        }
    }
}