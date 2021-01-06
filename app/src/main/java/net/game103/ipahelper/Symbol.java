package net.game103.ipahelper;

/**
 * Class representing a Symbol
 */
public class Symbol {
    // The name of the symbol
    private String name;

    /**
     * Constructor.
     * @param name  the name of this symbol
     */
    public Symbol(String name) {
        this.name = name;
    }

    /**
     * Get the name of this symbol.
     * @return  the name of this symbol
     */
    public String getName() {
        return this.name;
    }
}
