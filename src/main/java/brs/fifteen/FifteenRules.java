package brs.fifteen;

import brs.algorithms.astar.Rules;
import brs.algorithms.astar.State;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Определяет специфичные для задачи правила ее решения. В данной реализации
 * эвристика вычисляется как количество клеток, находящихся не на своих местах.
 *
 * @author brovko_rs
 */
public class FifteenRules implements Rules<FifteenState> {

    public List<FifteenState> getNeighbors(FifteenState currentState) {
        ArrayList<FifteenState> res = new ArrayList<FifteenState>();
        for (int i = 0; i < actions.length; i++) {
            byte[] field = doAction(currentState.getField(), actions[i]);
            if (field == null) {
                continue;
            }
            FifteenState state = new FifteenState(currentState, sideSize);
            state.setField(field);
            res.add(state);
        }
        return res;
    }

    /**
     * Подсчитывает количество родительских сотояний от a до b.
     *
     * @param a первое состояние. Должно быть среди состояний, предшествующих b.
     * @param b второе состояние.
     * @return количество переходов от a до b.
     */
    public int getDistance(FifteenState a, FifteenState b) {
        State c = b;
        int res = 0;
        while ((c != null) && (!c.equals(a))) {
            c = c.getParent();
            res++;
        }
        return res;
        /*
         * На самом деле, в силу специфики реализации А*, данному методу
         * достаточно всегда возвращать 1.
         */
    }

    /**
     * Эвристика вычисляется как количество клеток, находящихся не на своих
     * местах.
     */
    public int getH(FifteenState state) {
        int res = 0;
        for (int i = 0; i < size; i++) {
            if (state.getField()[i] != terminateState[i]) {
                res++;
            }
        }
        return res;
    }

    public boolean isTerminate(FifteenState state) {
        return Arrays.equals(state.getField(), terminateState);
    }

    public byte[] getTerminateState() {
        return terminateState;
    }

    /**
     * Возвращает массив доступных действий.
     */
    public int[] getActions() {
        return actions;
    }

    /**
     * Применяет к состоянию правило.
     *
     * @param field начальное состояние.
     * @param action применяемое правило.
     * @return новое состояние, полученное в результате применения правила. null
     * если состояние недопустимо.
     */
    public byte[] doAction(byte[] field, int action) {
        /* Выполняется поиск пустой клетки */
        int zero = 0;
        for (; zero < field.length; zero++) {
            if (field[zero] == 0) {
                break;
            }
            if (zero >= field.length) {
                return null;
            }
        }
        /* Вычисляется индекс перемещаемой клетки */
        int number = zero + action;
        /* Проверяется допустимость хода */
        if (number < 0 || number >= field.length) {
            return null;
        }
        if ((action == 1) && ((zero + 1) % sideSize == 0)) {
            return null;
        }
        if ((action == -1) && ((zero + 1) % sideSize == 1)) {
            return null;
        }
        /*
         * Создается новый экземпляр поля, на котором меняются местами пустая и
         * перемещаемая клетки
         */
        byte[] newField = Arrays.copyOf(field, field.length);
        byte temp = newField[zero];
        newField[zero] = newField[number];
        newField[number] = temp;

        return newField;
    }

    /**
     * @param fieldSize размер поля (количество клеток на одной стороне).
     * @param terminateState конечное сотояние.
     */
    public FifteenRules(int fieldSize, byte[] terminateState) {
        if (fieldSize < 2) {
            throw new IllegalArgumentException("Invalid field size.");
        }
        if (terminateState == null) {
            throw new IllegalArgumentException("Terminate state can`t be null.");
        }

        this.sideSize = fieldSize;
        size = sideSize * sideSize;

        if (terminateState.length != size) {
            throw new IllegalArgumentException(
                    "Size of terminate state is incorrect.");
        }
        this.terminateState = terminateState;

        top = -sideSize;
        bottom = sideSize;

        actions = new int[]{top, bottom, left, right};
    }

    protected int sideSize;
    protected int size;

    protected byte[] terminateState;

    private int left = -1;
    private int top;
    private int right = 1;
    private int bottom;
    protected int[] actions;
}
