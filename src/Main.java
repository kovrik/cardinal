import kovrik.cardinal.HyperLogLog;

public class Main {

    public static void main(String[] args) {
        HyperLogLog hll = new HyperLogLog();
        int elements = 100000;
        for (int i = 0; i < elements; i++) {
            hll.add(new Object());
        }
        double cardinality = hll.cardinality();
        System.out.println("Elements: " + elements);
        System.out.println("Cardinality: " + cardinality);

        double epsilon = (1.0 - (cardinality / elements)) * 100;
        System.out.println("Epsilon: " + epsilon + "%");
    }
}
