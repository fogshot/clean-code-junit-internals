package com.capgemini.pvonnieb;

/**
 * This class will produce a error message detailing how two Strings differ.
 * Any part of the strings outside the context will be compacted into "...".
 * <p>
 * Example 1: expected = "ABCDEF", actual = "ABCDDEF", contextLength = 1 will yield:
 * "expected:<...D[]E...> but was:<...D[D]E...>"
 * <p>
 * Example 2: expected = "ab", actual = "ab", contextLength = 1 will yield:
 * "expected:<ab> but was:<ab>"
 */
public class ComparisonCompactor {
    private static final String ELLIPSIS = "...";
    private static final String DELTA_END = "]";
    private static final String DELTA_START = "[";

    private int contextLength;
    private String expected;
    private String actual;
    private int prefixLength;
    private int suffixLength;

    /**
     * Initialize a new ComparisonCompactor.
     *
     * @param contextLength the size of the context around an error to display. 0 means no context.
     * @param expected      the String that was expected
     * @param actual        the String that was actually encountered
     */
    public ComparisonCompactor(int contextLength, String expected, String actual) {
        this.contextLength = contextLength;
        this.expected = expected;
        this.actual = actual;
    }

    /**
     * Explains where the actual string differs from the expected string.
     *
     * @param message An optional message prepended to the output. May be null.
     * @return A message explaining the divergence between expected and actual.
     */
    public String formatCompactedComparison(String message) {
        String compactExpected = expected;
        String compactActual = actual;
        if (shouldBeCompacted()) {
            findCommonPrefixAndSuffix();
            compactExpected = compact(expected);
            compactActual = compact(actual);
        }
        return Assert.format(message, compactExpected, compactActual);
    }

    private boolean shouldBeCompacted() {
        return !shouldNotCompact();
    }

    private boolean shouldNotCompact() {
        return expected == null || actual == null || expected.equals(actual);
    }

    private void findCommonPrefixAndSuffix() {
        findCommonPrefix();
        suffixLength = 0;
        for (; !suffixOverlapsPrefix(suffixLength); suffixLength++) {
            if (charFromEnd(expected, suffixLength) !=
                    charFromEnd(actual, suffixLength)) {
                break;
            }
        }
    }

    private static char charFromEnd(String s, int distance) {
        return s.charAt(s.length() - distance - 1);
    }

    private boolean suffixOverlapsPrefix(int suffixLength) {
        return actual.length() - suffixLength <= prefixLength ||
                expected.length() - suffixLength <= prefixLength;
    }

    private void findCommonPrefix() {
        prefixLength = 0;
        int end = Math.min(expected.length(), actual.length());
        for (; prefixLength < end; prefixLength++) {
            if (expected.charAt(prefixLength) != actual.charAt(prefixLength)) {
                break;
            }
        }
    }

    private String compact(String s) {
        return startingEllipsis() +
                startingContext() +
                DELTA_START +
                delta(s) +
                DELTA_END +
                endingContext() +
                endingEllipsis();
    }

    private String startingEllipsis() {
        return prefixLength > contextLength ? ELLIPSIS : "";
    }

    private String startingContext() {
        int contextStart = Math.max(0, prefixLength - contextLength);
        int contextEnd = prefixLength;
        return expected.substring(contextStart, contextEnd);
    }

    private String delta(String s) {
        int deltaStart = prefixLength;
        int deltaEnd = s.length() - suffixLength;
        return s.substring(deltaStart, deltaEnd);
    }

    private String endingContext() {
        int contextStart = expected.length() - suffixLength;
        int contextEnd = Math.min(contextStart + contextLength, expected.length());
        return expected.substring(contextStart, contextEnd);
    }

    private String endingEllipsis() {
        return expected.length() - suffixLength < expected.length() -
                contextLength ? ELLIPSIS : "";
    }
}
