import kovrik.cardinal.HyperLogLog;

public class Main {

    public static void main(String[] args) {
        HyperLogLog hll1 = new HyperLogLog();
        int elements = 20000000;
        for (int i = 0; i < elements; i++) {
            hll1.add(new Object());
        }

        HyperLogLog hll2 = new HyperLogLog();
        for (int i = 0; i < elements; i++) {
            hll2.add(new Object());
        }
        hll1.merge(hll2);

        elements = 2*elements; // merged
        double cardinality = hll1.cardinality();
        System.out.println("Elements: " + elements);
        System.out.println("Cardinality: " + cardinality);

        double epsilon = (1.0 - (cardinality / elements)) * 100;
        System.out.println("Epsilon actual: " + epsilon + "%");

        System.out.println("Absolute epsilon expected: " + hll1.standardError() * 100 + "%");
        System.out.println("Number of registers: " + hll1.size());
        System.out.println(hll1);

    }
}
