package net.game103.ipahelper;

/**
 * Class representing a FeatureSignSymbol
 * This is a mapping between a FeatureSign and a Symbol
 */
public class FeatureSignSymbol {
    // The featureSign
    private FeatureSign featureSign;
    // The symbol
    private Symbol symbol;

    /**
     * Constructor.
     * @param featureSign   The featureSign object for this mapping
     * @param symbol    The symbol object for this mapping
     */
    public FeatureSignSymbol(FeatureSign featureSign, Symbol symbol) {
        this.featureSign = featureSign;
        this.symbol = symbol;
    }

    /**
     * Get the featureSign object for this mapping.
     * @return  the featureSign object for this mapping
     */
    public FeatureSign getFeatureSign() {
        return this.featureSign;
    }

    /**
     * Get the symbol object for this mapping.
     * @return  the symbol object for this mapping
     */
    public Symbol getSymbol() {
        return this.symbol;
    }
}
