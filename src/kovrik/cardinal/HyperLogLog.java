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
 * - FIXME check accuracy
 * - variable params
 * - merging
 **/
public final class HyperLogLog {
    /**
     * i.e. we have the number
     * n = 162302747
     *
     * in binary:
     * 0000 1001 1010 1100 1000 1011 0001 1011
     *
     * Then, rightmost INDEX_BITS bits show register index
     *       othe VALUE_BITS bits are used to get value.
     *
     * value = numberOfLeadingZeros(value_bits);
     *
     * assume, INDEX_BITS = 8
     *         VALUE_BITS = Integer.SIZE - INDEX_BITS = 32 - 8 = 24
     *
     * Then,
     *
     * get index bits
     *                              | 8 bits
     * 0000 1001 1010 1100 1000 1011|0001 1011
     *
     * 0001 1011 = 27
     *
     * get value bits
     * |         24 bits             |
     * |0000 1001 1010 1100 1000 1011|0001 1011
     *
     * Number of leading zeros = 4 (four leftmost zeros)
     *
     * So, we set:
     *
     * register[27] = max(register[27], 4);
     *
     * where max(a, b) - function which returns maximum of two values
     */

    private static final int INDEX_BITS = 8;  // b : how many bits are used to get register index
    private static final int VALUE_BITS = Integer.SIZE  - INDEX_BITS; // how many bits are used to count value

    private static final int M = (1 << INDEX_BITS); // m = 2^b : number of registers
    private static final double ALPHA_MM = getAlphaMM(); // alpha * m * m

    private final RegisterSet registers = new RegisterSet(M);

    public static void main(String[] args) {
        System.out.println(getValue(162302747));
    }

    /**
     * Count object
     * @param o
     */
    public void add(Object o) {
        if (o == null) {
            return;
        }
        int hash  = o.hashCode(); /* TODO: Replace with MurmurHash ! */
        int index = getIndex(hash);
        int value = getValue(hash);
        registers.set(index, Math.max(value, registers.get(index)));
    }

    /**
     * Estimate cardinality
     * @return
     */
    public double cardinality() {
        double est;

        double invSum = 0d;
        double zeros  = 0d; // v
        for (int i = 0; i < M; i++) {
            int val = registers.get(i);
            invSum += 1.0 / (1L << val); // inverted sum
            if (val == 0) {
                zeros += 1; // count zeros
            }
        }
        double z = (1.0 / invSum); // indicator function
        double e = ALPHA_MM * z;    // raw estimation

        if (e <= (5.0 / 2.0) * M) {
            // small range correction
            if (zeros != 0) {
                est =  Math.round(M * Math.log(M / zeros));
            } else {
                est = e;
            }
        } else if (e <= (1L << 32) / 30.0) {
            // intermediate range - no correction
            est = Math.round(e);
        } else {
            // large range correction
            est = -(1L << 32) * (Math.log(1.0 - (e / (1L << 32))));  /* Beware of NaNs ! */
        }
        return est;
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
     * Count register index.
     * @param hash
     * @return
     */
    private static int getIndex(int hash) {
        return extractBits(hash, INDEX_BITS, 0);
    }

    /**
     * Count register value.
     * @param hash
     * @return
     */
    private static int getValue(int hash) {
        if (hash == 0) {
            return VALUE_BITS;
        }
        return Integer.numberOfLeadingZeros(extractBits(hash >>> INDEX_BITS, VALUE_BITS, 0)) - (Integer.SIZE - VALUE_BITS);
    }

    /**
     * Return `nrBits` bits of `l` starting from `offset`
     * @param l
     * @param nrBits
     * @param offset
     * @return
     */
    private static int extractBits(int l, int nrBits, int offset) {
        final int rightShifted = l >>> offset;
        final int mask = (1 << nrBits) - 1;
        return rightShifted & mask;
    }

    /**
     * Return number of registers
     * @return
     */
    public int numberOfRegisters() {
        return M;
    }

    /**
     * Return standard error (accuracy).
     * Formula is:
     *
     * 1.04 / sqrt(m)
     *
     * where m - is a number of registers
     *
     * @return
     */
    public double standardError() {
        return 1.04 / Math.sqrt(M);
    }

    @Override
    public String toString() {
        return "HyperLogLog{" + "registers=" + registers + '}';
    }
}
