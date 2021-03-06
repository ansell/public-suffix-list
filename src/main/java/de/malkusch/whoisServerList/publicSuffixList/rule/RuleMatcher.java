package de.malkusch.whoisServerList.publicSuffixList.rule;

import javax.annotation.concurrent.Immutable;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import de.malkusch.whoisServerList.publicSuffixList.util.DomainUtil;

/**
 * The rule matcher.
 *
 * The matcher is case insensitive.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
@Immutable
final class RuleMatcher {

    /**
     * Rule labels in reversed order.
     */
    private final String[] reversedLabels;

    /**
     * Sets the rule labels.
     *
     * The labels are stored as a reversed copy in {@link #reversedLabels}.
     *
     * @param labels  the rule labels, not null
     */
    RuleMatcher(final String[] labels) {
        this.reversedLabels = labels.clone();
        ArrayUtils.reverse(reversedLabels);
    }

    /**
     * Sets the rule labels.
     *
     * The labels are stored as a reversed copy in {@link #reversedLabels}.
     *
     * @param pattern  the rule pattern, not null
     */
    RuleMatcher(final String pattern) {
        this(DomainUtil.splitLabels(pattern));
    }

    /**
     * Returns the matched public suffix.
     *
     * Matching is case insensitive.
     *
     * @param domain  the domain name, may be null
     * @return the matched public suffix or null
     */
    String match(final String domain) {
        if (domain == null) {
            return null;

        }

        String[] reversedDomainLabels = DomainUtil.splitLabels(domain);
        ArrayUtils.reverse(reversedDomainLabels);
        if (reversedDomainLabels.length < reversedLabels.length) {
            return null;

        }

        String[] reversedMatchedLabels = new String[reversedLabels.length];
        for (int i = 0; i < reversedLabels.length; i++) {
            if (i > reversedDomainLabels.length) {
                return null;

            }
            String matchLabel = reversedLabels[i];
            String domainLabel = reversedDomainLabels[i];

            LabelMatcher matcher = new LabelMatcher(matchLabel);
            if (!matcher.isMatch(domainLabel)) {
                return null;

            }
            reversedMatchedLabels[i] = domainLabel;

        }
        ArrayUtils.reverse(reversedMatchedLabels);
        return DomainUtil.joinLabels(reversedMatchedLabels);
    }

    /**
     * Returns the rule pattern.
     *
     * @return the rule pattern, not null
     */
    String getPattern() {
        String[] labels = reversedLabels.clone();
        ArrayUtils.reverse(labels);
        return DomainUtil.joinLabels(labels);
    }

    @Override
    public String toString() {
        return getPattern();
    }

    @Override
    public boolean equals(final Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
