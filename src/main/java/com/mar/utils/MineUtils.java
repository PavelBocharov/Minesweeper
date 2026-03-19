package com.mar.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@UtilityClass
public class MineUtils {

    public static final Character CLOSE_CELL = ' ';
    public static final Character OPEN_CELL = '0';
    public static final Character MINE_CELL = 'M';
    public static final Character MINE_BOOM = 'X';

    public static Character[][] createMinefield(int w, int h, int minesCount) throws ManyMinesException {
        int cellCount = w * h;

        if (cellCount <= minesCount) {
            throw new ManyMinesException();
        }

        Character[] fieldLine = new Character[cellCount];
        Arrays.fill(fieldLine, CLOSE_CELL);

        Random rand = new Random();
        for (int i = 0; i < minesCount; i++) {
            int minePos = rand.nextInt(0, cellCount);
            while (minePos < cellCount) {
                if (fieldLine[minePos] == CLOSE_CELL) {
                    fieldLine[minePos] = MINE_CELL;
                    break;
                }
                minePos++;
                if (minePos == cellCount) {
                    minePos = 0;
                }
            }
        }

        Character[][] mineField = new Character[w][h];
        int l = 0;
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                mineField[i][j] = fieldLine[l++];
            }
        }

        return mineField;
    }

    public static Character[][] calcUserStep(Character[][] field, int col, int row) throws BoomException, CheckOpenCellException {
        if (field[col][row] != CLOSE_CELL && field[col][row] != MINE_CELL) {
            throw new CheckOpenCellException();
        }
        Set<Pair<Integer, Integer>> stack = new HashSet<>();
        stack.add(Pair.of(col, row));

        while (!stack.isEmpty()) {
            Pair<Integer, Integer> cell = stack.stream().findFirst().get();
            stack.remove(cell);

            int i = cell.getKey();
            int j = cell.getValue();

            if (field[i][j] == MINE_CELL) {
                throw new BoomException();
            }

            Pair<Integer, Set<Pair<Integer, Integer>>> pair = calcMineCountWithEmptyCells(field, i, j);
            int mineCount = pair.getKey();
            Set<Pair<Integer, Integer>> newCells = pair.getValue();

            if (mineCount == 0) {
                field[i][j] = OPEN_CELL;
                stack.addAll(newCells);
            } else {
                field[i][j] = (char) ('0' + mineCount);
            }
        }

        return field;
    }

    private static Pair<Integer, Set<Pair<Integer, Integer>>> calcMineCountWithEmptyCells(Character[][] field, int i, int j) {
        int mineCount = 0;
        Set<Pair<Integer, Integer>> newCells = new HashSet<>();

        mineCount += checkLeft(field, i, j, newCells);
        mineCount += checkRight(field, i, j, newCells);
        mineCount += checkUp(field, i, j, newCells);
        mineCount += checkDown(field, i, j, newCells);

        mineCount += checkLeftUp(field, i, j, newCells);
        mineCount += checkLeftDown(field, i, j, newCells);
        mineCount += checkRightUp(field, i, j, newCells);
        mineCount += checkRightDown(field, i, j, newCells);

        return Pair.of(mineCount, newCells);
    }

    public static Integer calcMineCount(Character[][] field, int i, int j) {
        return calcMineCountWithEmptyCells(field, i, j).getKey();
    }


    private static void saveAdd(Character[][] field, Set<Pair<Integer, Integer>> newCells, int i, int j) {
        if (i < 0 || i >= field.length) {
            return;
        }
        if (j < 0 || j >= field[0].length) {
            return;
        }
        newCells.add(Pair.of(i, j));
    }

    private static int checkLeft(Character[][] field, int i, int j, Set<Pair<Integer, Integer>> newCells) {
        if (i <= 0) {
            return 0;
        }
        if (field[i - 1][j] == MINE_CELL || field[i - 1][j] == MINE_BOOM) {
            return 1;
        }
        if (field[i - 1][j] == CLOSE_CELL) {
            saveAdd(field, newCells, i - 1, j);
        }
        return 0;
    }

    private static int checkRight(Character[][] field, int i, int j, Set<Pair<Integer, Integer>> newCells) {
        if (i >= field.length - 1) {
            return 0;
        }
        if (field[i + 1][j] == MINE_CELL || field[i + 1][j] == MINE_BOOM) {
            return 1;
        }
        if (field[i + 1][j] == CLOSE_CELL) {
            saveAdd(field, newCells, i + 1, j);
        }
        return 0;
    }

    private static int checkUp(Character[][] field, int i, int j, Set<Pair<Integer, Integer>> newCells) {
        if (j <= 0) {
            return 0;
        }
        if (field[i][j - 1] == MINE_CELL || field[i][j - 1] == MINE_BOOM) {
            return 1;
        }
        if (field[i][j - 1] == CLOSE_CELL) {
            saveAdd(field, newCells, i, j - 1);
        }
        return 0;
    }

    private static int checkDown(Character[][] field, int i, int j, Set<Pair<Integer, Integer>> newCells) {
        if (j >= field[0].length - 1) {
            return 0;
        }
        if (field[i][j + 1] == MINE_CELL || field[i][j + 1] == MINE_BOOM) {
            return 1;
        }
        if (field[i][j + 1] == CLOSE_CELL) {
            saveAdd(field, newCells, i, j + 1);
        }
        return 0;
    }

    private static int checkLeftUp(Character[][] field, int i, int j, Set<Pair<Integer, Integer>> newCells) {
        if (i <= 0 || j <= 0) {
            return 0;
        }
        if (field[i - 1][j - 1] == MINE_CELL || field[i - 1][j - 1] == MINE_BOOM) {
            return 1;
        }
        if (field[i - 1][j - 1] == CLOSE_CELL) {
            saveAdd(field, newCells, i - 1, j - 1);
        }
        return 0;
    }

    private static int checkLeftDown(Character[][] field, int i, int j, Set<Pair<Integer, Integer>> newCells) {
        if (i <= 0 || j >= field[0].length - 1) {
            return 0;
        }
        if (field[i - 1][j + 1] == MINE_CELL || field[i - 1][j + 1] == MINE_BOOM) {
            return 1;
        }
        if (field[i - 1][j + 1] == CLOSE_CELL) {
            saveAdd(field, newCells, i - 1, j + 1);
        }
        return 0;
    }

    private static int checkRightUp(Character[][] field, int i, int j, Set<Pair<Integer, Integer>> newCells) {
        if (i >= field.length - 1 || j <= 0) {
            return 0;
        }
        if (field[i + 1][j - 1] == MINE_CELL || field[i + 1][j - 1] == MINE_BOOM) {
            return 1;
        }
        if (field[i + 1][j - 1] == CLOSE_CELL) {
            saveAdd(field, newCells, i + 1, j - 1);
        }
        return 0;
    }

    private static int checkRightDown(Character[][] field, int i, int j, Set<Pair<Integer, Integer>> newCells) {
        if (i >= field.length - 1 || j >= field[0].length - 1) {
            return 0;
        }
        if (field[i + 1][j + 1] == MINE_CELL) {
            return 1;
        }
        if (field[i + 1][j + 1] == CLOSE_CELL) {
            saveAdd(field, newCells, i + 1, j + 1);
        }
        return 0;
    }

    private static void printMatrix(Object[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(String.valueOf(matrix[i][j] + "\t"));
            }
            System.out.println();
        }
    }

    public static class BoomException extends Exception {

    }

    public static class CheckOpenCellException extends Exception {

    }

    public static class ManyMinesException extends Exception {

    }

}
