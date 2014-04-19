package kovrik.cardinal;

import java.util.Arrays;

/**
 * Set of registers to store counts.
 */
public class RegisterSet {

    private final int[] registers;

    public RegisterSet(int numberOfRegisters) {
        this.registers = new int[numberOfRegisters];
    }

    public int get(int index) {
        return registers[index];
    }

    public void set(int index, int value) {
        registers[index] = value;
    }

    @Override
    public String toString() {
        return "RegisterSet{" + "registers=" + Arrays.toString(registers) + '}';
    }
}
