package kovrik.cardinal;

import kovrik.cardinal.interfaces.Registers;

import java.util.Arrays;

/**
 * Set of registers to store counts.
 */

/** TODO:
 * - add interface
 */
public class RegisterSet implements Registers {

    private final int[] registers;

    public RegisterSet(int numberOfRegisters) {
        this.registers = new int[numberOfRegisters];
    }

    @Override
    public int get(int index) {
        return registers[index];
    }

    @Override
    public void set(int index, int value) {
        registers[index] = value;
    }

    @Override
    public int size() {
        return registers.length;
    }

    @Override
    public String toString() {
        return "RegisterSet{" + "registers=" + Arrays.toString(registers) + '}';
    }
}
