package kovrik.cardinal;

public final class Util {

    private Util() {}

    /**
     * Return `nrBits` bits of `l` starting from `offset`
     * @param l
     * @param nrBits
     * @param offset
     * @return
     */
    public static int extractBits(int l, int nrBits, int offset) {
        final int rightShifted = l >>> offset;
        final int mask = (1 << nrBits) - 1;
        return rightShifted & mask;
    }

    /**
     * Return linear counting cardinality estimate.
     * @param m
     * @param v
     * @return
     */
    public static double linearCounting(double m, double v) {
        return Math.log(m / v);
    }
}
