package kovrik.cardinal;

import java.util.Arrays;

/**
 * Set of buckets to store counters.
 */
public class BucketSet {

    private final int[] buckets;

    public BucketSet(int numberOfBuckets) {
        this.buckets = new int[numberOfBuckets];
    }

    public int get(int index) {
        return buckets[index];
    }

    public void set(int index, int value) {
        buckets[index] = value;
    }

    @Override
    public String toString() {
        return "BucketSet{" + "buckets=" + Arrays.toString(buckets) + '}';
    }
}
