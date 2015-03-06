package brs.fifteen;

import brs.algorithms.astar.State;

import java.util.Arrays;

/**
 * Представляет состояние игрового поля головоломки "Пятнашки".
 *
 * @author brovko_rs
 */
public class FifteenState extends State {

    public static byte[] parseField(String str) {
        int i = 0;
        String[] lines = str.split("\n");
        byte[] res = new byte[lines.length * lines.length];
        for (String line : lines) {
            String[] vals = line.trim().split("\t");
            for (String v : vals) {
                res[i] = Byte.parseByte(v.trim());
                i++;
            }
        }
        return res;
    }

    /**
     * Проверяет, возможно ли привести состояние к терминальному.
     *
     * @param field состояние игрового поля.
     * @param sideSize размер стороны игрового поля.
     * @return true - если можно привести к терминальному.
     *
     * @see http://ru.wikipedia.org/wiki/Пятнашки
     */
    public static boolean checkState(byte[] field, int sideSize) {
        int N = 0;
        int e = 0;
        for (int i = 0; i < field.length; i++) {
            /* Определяется номер ряда пустой клетки (считая с 1). */
            if (field[i] == 0) {
                e = i / sideSize + 1;
            }
            if (i == 0) {
                continue;
            }
            /* Производится подсчет количества клеток меньших текущей */
            for (int j = i + 1; j < field.length; j++) {
                if (field[j] < field[i]) {
                    N++;
                }
            }
        }
        N = N + e;
        /* Если N является нечётной, то решения головоломки не существует. */
        return (N & 1) == 0; // Первый бит четного числа равен 0
    }

    /**
     * Возвращает состояние игрового поля в виду одномерного массива байт.
     */
    public byte[] getField() {
        return field;
    }

    /**
     * Устанавливает состояние игрового поля.
     */
    public void setField(byte[] field) {
        this.field = field;
        hash = Arrays.hashCode(field);
    }

    @Override
    public String toString() {
        if (field == null) {
            return "" + null;
        }
        StringBuffer sbf;
        sbf = new StringBuffer(field.length);
        for (int i = 0; i < sideSize; i++) {
            for (int j = 0; j < sideSize; j++) {
                sbf.append(field[j + i * sideSize]);
                sbf.append("\t");
            }
            sbf.append("\n");
        }
        return sbf.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof FifteenState)) {
            return false;
        }
        return hash == obj.hashCode();
    }

    @Override
    public int hashCode() {
        return hash;
    }

    /**
     * Создает описание состояния игрового поля.
     *
     * @param parent предшествуюущее состояние.
     * @param sideSize размер стороны поля.
     */
    public FifteenState(State parent, int sideSize) {
        super(parent);
        this.sideSize = sideSize;
    }

    private byte[] field;
    private int sideSize;
    private int hash;
}
