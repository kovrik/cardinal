package kovrik.cardinal.interfaces;

/**
 * Common cardinality estimator interface.
 */
public interface Cardinal {
    /**
     * Add object.
     * @param o
     */
    void add(Object o);

    /**
     * Return cardinality estimation.
     * @return
     */
    double cardinality();

    /**
     * Return number of registers.
     * @return
     */
    int size();

    /**
     * Return standard error.
     * @return
     */
    double standardError();

    /**
     * Merge two estimators.
     * Must have the same size
     * (otherwise throw IllegalArgumentException).
     *
     * @param cardinal
     * @throws java.lang.IllegalArgumentException
     */
    void merge(Cardinal cardinal) throws IllegalArgumentException;

    /**
     * Return inner data representation.
     */
    Registers getRegisters();
}
