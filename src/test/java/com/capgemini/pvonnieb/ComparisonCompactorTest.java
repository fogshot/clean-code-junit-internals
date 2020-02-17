package com.capgemini.pvonnieb;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ComparisonCompactorTest {

    @Test
    public void testMessage() {
        String failure = new ComparisonCompactor(0, "b", "c").formatCompactedComparison("a");
        assertThat(failure).isEqualTo("a expected:<[b]> but was:<[c]>");
    }

    @Test
    public void testStartSame() {
        String failure = new ComparisonCompactor(1, "ba", "bc").formatCompactedComparison(null);
        assertThat(failure).isEqualTo("expected:<b[a]> but was:<b[c]>");
    }

    @Test
    public void testEndSame() {
        String failure = new ComparisonCompactor(1, "ab", "cb").formatCompactedComparison(null);
        assertThat(failure).isEqualTo("expected:<[a]b> but was:<[c]b>");
    }

    @Test
    public void testSame() {
        String failure = new ComparisonCompactor(1, "ab", "ab").formatCompactedComparison(null);
        assertThat(failure).isEqualTo("expected:<ab> but was:<ab>");
    }

    @Test
    public void testNoContextStartAndEndSame() {
        String failure = new ComparisonCompactor(0, "abc", "adc").formatCompactedComparison(null);
        assertThat(failure).isEqualTo("expected:<...[b]...> but was:<...[d]...>");
    }

    @Test
    public void testStartAndEndContext() {
        String failure = new ComparisonCompactor(1, "abc", "adc").formatCompactedComparison(null);
        assertThat(failure).isEqualTo("expected:<a[b]c> but was:<a[d]c>");
    }

    @Test
    public void testStartAndEndContextWithEllipses() {
        String failure = new ComparisonCompactor(1, "abcde", "abfde").formatCompactedComparison(null);
        assertThat(failure).isEqualTo("expected:<...b[c]d...> but was:<...b[f]d...>");
    }

    @Test
    public void testComparisonErrorStartSameComplete() {
        String failure = new ComparisonCompactor(2, "ab", "abc").formatCompactedComparison(null);
        assertThat(failure).isEqualTo("expected:<ab[]> but was:<ab[c]>");
    }

    @Test
    public void testComparisonErrorEndSameComplete() {
        String failure = new ComparisonCompactor(0, "bc", "abc").formatCompactedComparison(null);
        assertThat(failure).isEqualTo("expected:<[]...> but was:<[a]...>");
    }

    @Test
    public void testComparisonErrorEndSameCompleteContext() {
        String failure = new ComparisonCompactor(2, "bc", "abc").formatCompactedComparison(null);
        assertThat(failure).isEqualTo("expected:<[]bc> but was:<[a]bc>");
    }

    @Test
    public void testComparisonErrorOverlappingMatches() {
        String failure = new ComparisonCompactor(0, "abc", "abbc").formatCompactedComparison(null);
        assertThat(failure).isEqualTo("expected:<...[]...> but was:<...[b]...>");
    }

    @Test
    public void testComparisonErrorOverlappingMatchesContext() {
        String failure = new ComparisonCompactor(2, "abc", "abbc").formatCompactedComparison(null);
        assertThat(failure).isEqualTo("expected:<ab[]c> but was:<ab[b]c>");
    }

    @Test
    public void testComparisonErrorOverlappingMatches2() {
        String failure = new ComparisonCompactor(0, "abcdde", "abcde").formatCompactedComparison(null);
        assertThat(failure).isEqualTo("expected:<...[d]...> but was:<...[]...>");
    }

    @Test
    public void testComparisonErrorOverlappingMatchesContext2() {
        String failure = new ComparisonCompactor(1, "ABCDEF", "ABCDDEF").formatCompactedComparison(null);
        assertThat(failure).isEqualTo("expected:<...D[]E...> but was:<...D[D]E...>");
    }

    @Test
    public void testComparisonErrorOverlappingMatches2Context() {
        String failure = new ComparisonCompactor(2, "abcdde", "abcde").formatCompactedComparison(null);
        assertThat(failure).isEqualTo("expected:<...cd[d]e> but was:<...cd[]e>");
    }

    @Test
    public void testComparisonErrorWithActualNull() {
        String failure = new ComparisonCompactor(0, "a", null).formatCompactedComparison(null);
        assertThat(failure).isEqualTo("expected:<a> but was:<null>");
    }

    @Test
    public void testComparisonErrorWithActualNullContext() {
        String failure = new ComparisonCompactor(2, "a", null).formatCompactedComparison(null);
        assertThat(failure).isEqualTo("expected:<a> but was:<null>");
    }

    @Test
    public void testComparisonErrorWithExpectedNull() {
        String failure = new ComparisonCompactor(0, null, "a").formatCompactedComparison(null);
        assertThat(failure).isEqualTo("expected:<null> but was:<a>");
    }

    @Test
    public void testComparisonErrorWithExpectedNullContext() {
        String failure = new ComparisonCompactor(2, null, "a").formatCompactedComparison(null);
        assertThat(failure).isEqualTo("expected:<null> but was:<a>");
    }

    @Test
    public void testBug609972() {
        String failure = new ComparisonCompactor(10, "S&P500", "0").formatCompactedComparison(null);
        assertThat(failure).isEqualTo("expected:<[S&P50]0> but was:<[]0>");
    }

}