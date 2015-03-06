package brs.fifteen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Random;

import brs.algorithms.astar.Astar;
import brs.algorithms.astar.State;

/**
 *
 * @author brovko_rs
 */
public class Fifteen {

    public static void main(String[] args) {
        /* Установка кодировки консоли */
        consoleEncoding = System.getProperty("consoleEncoding");
        if (consoleEncoding != null) {
            try {
                System.setOut(new PrintStream(System.out, true, consoleEncoding));
            } catch (java.io.UnsupportedEncodingException ex) {
                System.err.println("Unsupported encoding set for console: "
                        + consoleEncoding);
            }
        }

        parseArgs(args);

        if (isReadInStream) {
            try {
                startField = readStartState();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
            if (!FifteenState.checkState(startField, sideSize)) {
                System.out
                        .println("\nДанное состояние нельзя привести к терминальному.\n"
                                + "См. http://ru.wikipedia.org/wiki/Пятнашки\n");
                System.exit(1);
            }
        }

        int size = sideSize * sideSize;
        terminateField = getTerminalState(sideSize, size);

        FifteenRules rules = new FifteenRules2(sideSize, terminateField);
        FifteenState startState = new FifteenState(null, sideSize);

        if (startField == null) {
            startField = generateStartState(rules, stepCount);
        }
        startState.setField(startField);

        Astar<FifteenState, FifteenRules> astar = new Astar<FifteenState, FifteenRules>(
                rules);
        long time = System.currentTimeMillis();
        Collection<State> res = astar.search(startState);
        time = System.currentTimeMillis() - time;

        if (res == null) {
            System.out.println("Solution not found.");
            return;
        } else {
            for (State s : res) {
                System.out.println(s.toString());
            }
        }
        if (isShowStatistic) {
            System.out.println("Time: " + time + "ms");
            /* Начальное состояние за ход не считается */
            System.out.println("Solution length: " + (res.size() - 1));
            System.out
                    .println("Opened states: " + astar.getClosedStatesCount());
        }
    }

    /**
     * Считывает начальное состояние из входного потока, определяя размерность
     * поля по количеству строк в потоке.
     *
     * @return массив байт, описывающий начальное состояние или null, если не
     * удалось прочесть начальное состояние.
     * @throws IOException
     */
    private static byte[] readStartState() throws IOException {
        System.out.println("Reading state from input stream...");
        InputStreamReader istr = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(istr);
        String line = null;
        sideSize = 0;
        StringBuffer buf = new StringBuffer();
        while ((line = reader.readLine()) != null) {
            if (line.isEmpty()) {
                break;
            }
            buf.append(line + "\n");
            sideSize++;
        }
        String state = buf.toString();
        if (state.isEmpty()) {
            return null;
        } else {
            return FifteenState.parseField(state);
        }
    }

    /**
     * Генерирует начальное состояние путем swapCount начальных перестановок.
     *
     * @param rules
     * @param swapCount количество перестановок.
     * @return сгенерированное начальное состояние.
     */
    private static byte[] generateStartState(FifteenRules rules, int swapCount) {
        int stepCount = swapCount;
        byte[] startState = rules.getTerminateState();

        int[] actions = rules.getActions();
        Random r = new Random();
        while (stepCount > 0) {
            int j = r.nextInt(actions.length);
            byte[] state = rules.doAction(startState, actions[j]);
            if (state != null) {
                startState = state;
                stepCount--;
            }
        }
        return startState;
    }

    /**
     * Генерирует терминальное состояние, как упорядоченную последовательность
     * чисел.
     *
     * @param sideSize
     * @param size
     * @return
     */
    private static byte[] getTerminalState(int sideSize, int size) {
        if (terminateField == null) {
            terminateField = new byte[size];
            byte k = 0;
            for (int i = 0; i < sideSize; i++) {
                for (int j = 0; j < sideSize; j++) {
                    terminateField[j + i * sideSize] = ++k;
                }
            }
            terminateField[size - 1] = 0;
        }
        return terminateField;
    }

    /**
     * Разбирает аргументы запуска приложения.
     *
     * @param args
     */
    private static void parseArgs(String[] args) {
        if (args == null || args.length == 0) {
            return;
        }
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-h")) {
                try {
                    showHelp();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                continue;
            }
            if (args[i].equals("-v")) {
                isShowStatistic = true;
                continue;
            }
            if (args[i].equals("-s")) {
                isReadInStream = false;
                sideSize = Integer.parseInt(args[i]);
                i++;
                continue;
            }
            if (args[i].equals("-c")) {
                isReadInStream = false;
                stepCount = Integer.parseInt(args[i]);
                i++;
                continue;
            }
            throw new IllegalArgumentException("Unknown argument: " + args[i]);
        }
    }

    private static void showHelp() throws IOException {
        String fileName = consoleEncoding == null ? "help.en" : "help.ru";
        InputStreamReader strm = new InputStreamReader(
                Fifteen.class.getResourceAsStream(fileName), "UTF-8");
        BufferedReader reader = new BufferedReader(strm);

        PrintStream out = consoleEncoding == null 
                ? new PrintStream(System.out, true) 
                : new PrintStream(System.out, true, consoleEncoding);

        String str = null;
        while ((str = reader.readLine()) != null) {
            out.println(str);
        }
        reader.close();
        System.exit(0);
    }

    private static byte[] startField;

    private static byte[] terminateField;

    private static int stepCount = 10;

    private static int sideSize = 4;

    private static boolean isReadInStream = true;

    private static boolean isShowStatistic = false;

    private static String consoleEncoding;
}
