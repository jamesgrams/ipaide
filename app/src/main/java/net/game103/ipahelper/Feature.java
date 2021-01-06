package net.game103.ipahelper;

/**
 * Class representing a feature
 */
public class Feature {
    // The name of the feature
    private String name;

    /**
     * Constructor
     * @param name  the name of the feature
     */
    public Feature(String name) {
        this.name = name;
    }

    /**
     * Get the name of the feature
     * @return  the name of the feature
     */
    public String getName() {
        return this.name;
    }
}
