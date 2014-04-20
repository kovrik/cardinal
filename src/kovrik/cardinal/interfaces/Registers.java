package kovrik.cardinal.interfaces;

public interface Registers {

    int get(int index);

    void set(int index, int value);

    /**
     * Return number of registers.
     * @return
     */
    int size();
}
