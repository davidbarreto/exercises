package fragment.submissions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DavidFerreira {

    private static final Pattern splitAtSemicolon = Pattern.compile(";");

    public static void main(String[] args) throws IOException {

        try (BufferedReader in = new BufferedReader(new FileReader(args[0]))) {
            in.lines()
                    .map(DavidFerreira::reassemble)
                    .forEach(System.out::println);
        }
    }

    private static String reassemble(String fragmentedText) {
        return reassemble(splitAtSemicolon.splitAsStream(fragmentedText).collect(Collectors.toSet()));
    }

    private static String reassemble(Set<String> fragments) {

        while (fragments.size() > 1) {
            String fragment = fragments.iterator().next();
            fragments.remove(fragment);

            FragmentComparisonResult result = longestSuffix(fragment, fragments);
            String combinedChunks = combine(result);

            String toRemove = result.chunkWithLongestCommonSuffix.equals(fragment) ?
                                      result.chunk : result.chunkWithLongestCommonSuffix;
            fragments.remove(toRemove);
            fragments.add(combinedChunks);
        }

        // At the end, the set will contain just one element: the reassempled text
        return fragments.iterator().next();
    }

    private static FragmentComparisonResult longestSuffix(String chunk, Set<String> chunks) {

        FragmentComparisonResult ans = new FragmentComparisonResult(chunk, null, false, 0);
        for (String searchedChunk : chunks) {
            ans = longestSuffixLength(ans, chunk, searchedChunk);
        }
        return ans;
    }

    private static FragmentComparisonResult longestSuffixLength(FragmentComparisonResult currentMax, String chunk1, String chunk2) {

        FragmentComparisonResult data1 = longestSuffix(chunk1, chunk2);
        FragmentComparisonResult data2 = longestSuffix(chunk2, chunk1);
        FragmentComparisonResult data = data1.commonSuffixSize > data2.commonSuffixSize ? data1 : data2;

        if (data.commonSuffixSize >= currentMax.commonSuffixSize) {
            return data;
        }
        return currentMax;
    }

    private static FragmentComparisonResult longestSuffix(String chunk1, String chunk2) {

        if (chunk1.contains(chunk2)) {
            return new FragmentComparisonResult(chunk1, chunk2, true, chunk2.length());
        }

        for (int i = 0; i < chunk1.length(); i++) {
            String substring = chunk1.substring(i);
            if (chunk2.startsWith(substring)) {
                return new FragmentComparisonResult(chunk1, chunk2, false, substring.length());
            }
        }

        return new FragmentComparisonResult(chunk1, chunk2, false, 0);
    }

    private static String combine(FragmentComparisonResult data) {

        if (data.contained) {
            return data.chunk;
        }

        if (data.commonSuffixSize == 0) {
            return data.chunk + data.chunkWithLongestCommonSuffix;
        }
        return data.chunk + data.chunkWithLongestCommonSuffix.substring(data.commonSuffixSize);
    }

    static class FragmentComparisonResult {
        private final String chunk;
        private final String chunkWithLongestCommonSuffix;

        private final boolean contained;

        private final int commonSuffixSize;

        public FragmentComparisonResult(String chunk, String chunkWithLongestCommonSuffix, boolean contained, int commonSuffixSize) {
            this.chunk = chunk;
            this.chunkWithLongestCommonSuffix = chunkWithLongestCommonSuffix;
            this.contained = contained;
            this.commonSuffixSize = commonSuffixSize;
        }

        public String getChunk() {
            return chunk;
        }

        public String getChunkWithLongestCommonSuffix() {
            return chunkWithLongestCommonSuffix;
        }

        public boolean isContained() {
            return contained;
        }

        public int getCommonSuffixSize() {
            return commonSuffixSize;
        }
    }
}

