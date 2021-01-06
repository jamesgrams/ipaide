package net.game103.ipahelper;

/**
 * Class representing a mapping between a feature and a sign
 */
public class FeatureSign {
    // The feature for this mapping
    private Feature feature;
    // The sign for this mapping
    // note that + is true
    // - is false
    // 0 is null
    private Boolean sign;

    /**
     * Constructor.
     * @param feature   The feature object for this mapping
     * @param sign  The sign for this mapping
     */
    public FeatureSign(Feature feature, Boolean sign) {
        this.feature = feature;
        this.sign = sign;
    }

    /**
     * Get the feature for this mapping
     * @return the feature for this mapping
     */
    public Feature getFeature() {
        return this.feature;
    }

    /**
     * Get the sign for this mapping
     * @return the sign for this mapping
     */
    public Boolean getSign() {
        return this.sign;
    }
}
