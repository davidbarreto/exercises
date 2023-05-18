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
        return new FragmentReassembler().reassemble(splitAtSemicolon.splitAsStream(fragmentedText).collect(Collectors.toSet()));
    }

    static class FragmentReassembler {
        public String reassemble(Set<String> fragments) {

            while (fragments.size() > 1) {
                String fragment = fragments.iterator().next();
                fragments.remove(fragment);

                FragmentComparisonResult result = longestSuffix(fragment, fragments);

                fragments.remove(result.baseFragment);
                fragments.remove(result.pairFragment);

                fragments.add(merge(result));
            }
            // At the end, the set will contain just one element: the reassembled text
            return fragments.iterator().next();
        }

        public FragmentComparisonResult longestSuffix(String fragment, Set<String> fragments) {

            FragmentComparisonResult ans = new FragmentComparisonResult(fragment, null, false, 0);
            for (String searchingFragment : fragments) {
                FragmentComparisonResult result1 = longestSuffix(fragment, searchingFragment);
                FragmentComparisonResult result2 = longestSuffix(searchingFragment, fragment);
                FragmentComparisonResult result = result1.overlappingSize > result2.overlappingSize ? result1 : result2;

                if (result.overlappingSize >= ans.overlappingSize) {
                    ans = result;
                }
            }
            return ans;
        }

        public FragmentComparisonResult longestSuffix(String fragment1, String fragment2) {

            if (fragment1.contains(fragment2)) {
                return new FragmentComparisonResult(fragment1, fragment2, true, fragment2.length());
            }

            for (int i = 0; i < fragment1.length(); i++) {
                String substring = fragment1.substring(i);
                if (fragment2.startsWith(substring)) {
                    return new FragmentComparisonResult(fragment1, fragment2, false, substring.length());
                }
            }
            return new FragmentComparisonResult(fragment1, fragment2, false, 0);
        }

        public String merge(FragmentComparisonResult fragmentComparisonResult) {

            if (fragmentComparisonResult.fragmentBaseContainsPairFragment) {
                return fragmentComparisonResult.baseFragment;
            }
            return fragmentComparisonResult.baseFragment
                           + fragmentComparisonResult.pairFragment.substring(fragmentComparisonResult.overlappingSize);
        }

        public static class FragmentComparisonResult {
            private final String baseFragment;
            private final String pairFragment;
            private final boolean fragmentBaseContainsPairFragment;
            private final int overlappingSize;

            public FragmentComparisonResult(String baseFragment, String pairFragment, boolean fragmentBaseContainsPairFragment, int overlappingSize) {
                this.baseFragment = baseFragment;
                this.pairFragment = pairFragment;
                this.fragmentBaseContainsPairFragment = fragmentBaseContainsPairFragment;
                this.overlappingSize = overlappingSize;
            }
        }
    }
}

