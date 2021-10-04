package ru.leadpogrommer.oop.stringfinder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class with cool method for finding strings in files
 */
public class StringFinder {
    /**
     * Finds all occurrences  of string in files
     *
     * @param filepath Path to the file where we should search for string
     * @param needle   String that should be searched
     * @return List with string positions
     * @throws IOException If file does not exist
     */
    public static List<Integer> findSubstring(String filepath, String needle) throws IOException {
        var rdr = new BufferedReader(new FileReader(filepath));


        var m = needle.length();
        var s = needle.toCharArray();
        var suffshift = new int[m + 1];
        Arrays.fill(suffshift, m);
        var z = new int[m];
        for (int j = 1, maxZidx = 0, maxZ = 0; j < m; j++) {
            if (j <= maxZ) z[j] = Math.min(maxZ - j + 1, z[j - maxZidx]);
            while (j + z[j] < m && s[m - 1 - z[j]] == s[m - 1 - (j + z[j])]) z[j]++;
            if (j + z[j] - 1 > maxZ) {
                maxZidx = j;
                maxZ = j + z[j] - 1;
            }
        }
        for (int j = m - 1; j > 0; j--) suffshift[m - z[j]] = j;
        for (int j = 1, r = 0; j <= m - 1; j++) {
            if (j + z[j] == m) {
                for (; r <= j; r++) {
                    if (suffshift[r] == m) suffshift[r] = j;
                }
            }
        }

        int j, bound = 0;
        int i = 0;

        var cbuf = new char[m];
        var ret = new ArrayList<Integer>();

        while (true) {
            rdr.mark(2 * m);
            if (rdr.read(cbuf, 0, m) != m) break;
            for (j = m - 1; j >= bound && s[j] == cbuf[j]; ) j--;
            if (j < bound) {
                ret.add(i);
                bound = m - suffshift[0];
                j = -1;
            } else {
                bound = 0;
            }
            rdr.reset();
            if (rdr.skip(suffshift[j + 1]) != suffshift[j + 1]) break;
            i += suffshift[j + 1];
        }

        return ret;
    }
}
