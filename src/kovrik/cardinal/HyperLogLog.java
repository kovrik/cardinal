package kovrik.cardinal;

/**
 * Cardinality estimator.
 * Basic HyperLogLog implementation.
 *
 * See http://algo.inria.fr/flajolet/Publications/FlFuGaMe07.pdf
 *
 * @author kovrik
 */

/**
 * TODO:
 * - accuracy optimization
 * - variable params
 * - merging
 **/
public final class HyperLogLog {

    private static final int INDEX_BITS = 6; // how many bits are used to get bucket index
    private static final int VALUE_BITS = 15;// how many bits are used to count value
    private final double ALPHA   = getAlpha(); // bias correction constant
    private final double ALPHAMM = getAlphaMM(); // alpha * m * m

    private static final int INDEX_MASK = (1 << INDEX_BITS) - 1;
    private static final int VALUE_MASK = (1 << VALUE_BITS) - 1;

    private static final int M = (1 << INDEX_BITS); // number of buckets

    private final BucketSet buckets = new BucketSet(M);

    /**
     * Count given object
     * @param o
     */
    public void add(Object o) {
        if (o == null) {
            return;
        }
        int hash  = o.hashCode();
        int index = getIndex(hash);
        int value = getValue(hash);
        buckets.set(index, max(value, buckets.get(index)));
    }

    /**
     * Estimate cardinality
     * @return
     */
    /** TODO: CHECK */
    public double cardinality() {
        double invSum = 0d;
        double zeros  = 0d;
        for (int i = 0; i < M; i++) {
            int val = buckets.get(i);
            invSum += 1.0 / (1L << val); // inverted sum
            if (val == 0) {
                zeros += 1; // count zeros
            }
        }
        double est = ALPHAMM * (1.0 / invSum); // base estimation

        if (est <= (5.0 / 2.0) * M) {
            // small range correction
            return Math.round(linearCounting(M, zeros));
        }
        if (est <= (1.0 / 30) * (1L << 32)) {
            // intermediate range - no correction
            return Math.round(est);
        } else {
            // large range correction
            return -(1L << 32)*(Math.log(1.0 - (est / (1L << 32))));
        }
    }

    /**
     * Get bias correction constant.
     * @return
     */
    private static double getAlpha() {
        if (M == 16) {
            return 0.673;
        } else if (M == 32) {
            return 0.697;
        } else if (M == 64){
            return 0.789;
        } else {
            return 0.7213 / (1 + (1.079 / M));
        }
    }

    /**
     * Get bias correction constant * m * m.
     * @return
     */
    private static double getAlphaMM() {
        return getAlpha() * M * M;
    }

    /**
     * Count bucket index.
     * @param hash
     * @return
     */
    private static int getIndex(int hash) {
        return hash & INDEX_MASK;
    }

    /**
     * Count bucket value.
     * @param hash
     * @return
     */
    private static int getValue(int hash) {
        if (hash == 0) {
            return 32; /** Assumes 32 bit integers */
        }
        return Integer.numberOfTrailingZeros((hash >> INDEX_BITS) & VALUE_MASK) + 1;
    }

    private static double linearCounting(int m, double v) {
        return m * Math.log(m / v);
    }

    private static int max(int first, int second) {
        if (first >= second) {
            return first;
        } else {
            return second;
        }
    }

    @Override
    public String toString() {
        return "HyperLogLog{" + "buckets=" + buckets + '}';
    }
}
