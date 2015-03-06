package brs.fifteen;

/**
 *
 * @author brovko_rs
 */
public class FifteenRules2 extends FifteenRules {

    /**
     * Эвристика: нарушение порядка на первых строках штрафуется сильнее.
     */
    @Override
    public int getH(FifteenState state) {
        int res = 0;
        int penalty = sideSize;
        for (int i = 0; i < size; i++) {
            if ((i + 1) % sideSize == 0) {
                penalty--;
            }
            if (state.getField()[i] != terminateState[i]) {
                res += penalty;
            }
        }
        return res;
    }

    /**
     * @param fieldSize размер поля (количество клеток на одной стороне).
     * @param terminateState конечное сотояние.
     */
    public FifteenRules2(int fieldSize, byte[] terminateState) {
        super(fieldSize, terminateState);
    }
}
