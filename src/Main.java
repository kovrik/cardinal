import kovrik.cardinal.HyperLogLog;

import java.util.Random;

public class Main {

    public static void main(String[] args) {
        System.out.println(r);
        System.out.println(Integer.toBinaryString(r));
        HyperLogLog hll = new HyperLogLog();
        int elements = 10000000;
        for (int i = 0; i < elements; i++) {
            hll.add(new Object());
        }
        double cardinality = hll.cardinality();
        System.out.println("Elements: " + elements);
        System.out.println("Cardinality: " + cardinality);

        double epsilon = (1.0 - (cardinality / elements)) * 100;
        System.out.println("Epsilon actual: " + epsilon + "%");

        System.out.println("Absolute epsilon expected: " + hll.standardError() * 100 + "%");
        System.out.println("Number of registers: " + hll.numberOfRegisters());
        System.out.println(hll);

    }
}
