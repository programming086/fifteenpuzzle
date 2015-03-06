package brs.algorithms.astar;

/**
 * Представляет вершину графа решений.
 *
 * @author brovko_rs
 */
public abstract class State {

    /**
     * Возвращает вес состояния как сумму расстояния(от начального состояния до
     * текущего) и эвристической оценки(предполагаемого расстояния от текущего
     * состояния до терминального).
     */
    public int getF() {
        return g + h;
    }

    /**
     * Возвращает расстояние от начального состояния до текущего.
     */
    public int getG() {
        return g;
    }

    /**
     * Устанавливает значение оценки расстояния от начального состояния до
     * текущего.
     */
    public void setG(int g) {
        this.g = g;
    }

    /**
     * Возвращает эвристическую оценку расстояния от текущего состояния до
     * терминального.
     */
    public int getH() {
        return h;
    }

    /**
     * Устанавливает значение эвристической оценки расстояния от текущего
     * состояния до конечного.
     */
    public void setH(int h) {
        this.h = h;
    }

    /**
     * Возвращает предшествующее состояние.
     */
    public State getParent() {
        return parent;
    }

    public void setParent(State parent) {
        this.parent = parent;
    }

    public State(State parent) {
        this.parent = parent;
    }

    private int g;
    private int h;
    private State parent;
}
