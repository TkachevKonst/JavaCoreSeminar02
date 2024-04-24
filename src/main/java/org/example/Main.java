package org.example;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();
    private static final int WIN_COUNT = 4;
    private static final char DOT_HUMAN = 'X';
    private static final char DOT_AI = '0';
    private static final char DOT_EMPTY = '*';
    private static int fieldSizeX;
    private static int fieldSizeY;
    private static char[][] field;


    public static void main(String[] args) {
        while (true) {
            initialize();
            printField();
            while (true) {
                humanMove();
                printField();
                if (checkState(DOT_HUMAN, WIN_COUNT, "Вы победили!"))
                    break;
                aiMove();
                printField();
                if (checkState(DOT_AI, WIN_COUNT, "Победил компьютер!"))
                    break;
            }
            System.out.println("Желаете сыграть еще раз? (Y - да): ");
            if (!scanner.next().equalsIgnoreCase("Y"))
                break;
        }
    }

    /**
     * Создание пустого игрового поля
     */
    static void initialize() {
        fieldSizeX = 5;
        fieldSizeY = 5;
        field = new char[fieldSizeX][fieldSizeY];
        for (int i = 0; i < fieldSizeX; i++) {
            for (int j = 0; j < fieldSizeY; j++) {
                field[i][j] = DOT_EMPTY;
            }
        }
    }

    /**
     * Печать игрового поля
     */
    static void printField() {
        System.out.print("+");
        for (int i = 0; i < fieldSizeX; i++) {
            System.out.print("|" + (i + 1));
        }
        System.out.println();
        for (int i = 0; i < fieldSizeX; i++) {
            System.out.print(i + 1 + "|");
            for (int j = 0; j < fieldSizeY; j++) {
                System.out.print(field[i][j] + "|");
            }
            System.out.println();
        }
        for (int i = 0; i < fieldSizeX * 2 + 2; i++) {
            System.out.print("_");
        }
        System.out.println();
    }

    /**
     * проверка на пустую ячейку
     *
     * @param x
     * @param y
     * @return
     */
    static boolean isCellEmpty(int x, int y) {
        return field[x][y] == DOT_EMPTY;
    }

    /**
     * Проверка координат на выход из игрового поля
     *
     * @param x
     * @param y
     * @return
     */
    static boolean isCellValid(int x, int y) {
        return x >= 0 && x < fieldSizeX && y >= 0 && y < fieldSizeY;
    }

    /**
     * Ход игрока (человек)
     */
    static void humanMove() {
        int x;
        int y;
        do {
            System.out.printf("Введите координаты хода X и Y \n (от 1 до %d) через пробел\n", fieldSizeX);
            x = scanner.nextInt() - 1;
            y = scanner.nextInt() - 1;
        }
        while (!isCellValid(x, y) || !isCellEmpty(x, y));
        field[x][y] = DOT_HUMAN;
    }

    /**
     * Ход игрока (компьютер)
     */
    static void aiMove() {
        int x;
        int y;

        if (checkAiMove(DOT_HUMAN, DOT_AI, WIN_COUNT)) {
            do {
                x = random.nextInt(fieldSizeX);
                y = random.nextInt(fieldSizeY);
            }
            while (!isCellEmpty(x, y));
            field[x][y] = DOT_AI;
        }
    }

    /**
     * Проверка на победу горизонт и вертикаль
     *
     * @param x
     * @param y
     * @param dot
     * @param win
     * @return
     */
    static boolean checkHorizonVertical(int x, int y, char dot, int win) {
        if (field[x][y] == dot) {
            int countHor = 1;
            int countVer = 1;
            for (int i = 1; i < win; i++) {

                if (isCellValid(x + i, y)) {
                    if (field[x + i][y] == dot) {
                        countVer++;
                    }
                }
                if (isCellValid(x, y + i)) {
                    if (field[x][y + i] == dot) {
                        countHor++;
                    }
                }
                if (countHor == win || countVer == win) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Проверка на ничью
     *
     * @return
     */
    static boolean checkDraw() {
        for (int i = 0; i < fieldSizeX; i++) {
            for (int j = 0; j < fieldSizeY; j++) {
                if (isCellEmpty(i, j)) return false;
            }
        }
        return true;
    }

    /**
     * Проверка на победу по диагонали
     *
     * @param x
     * @param y
     * @param dot
     * @param win
     * @return
     */
    static boolean checkDiagonal(int x, int y, char dot, int win) {
        if (field[x][y] == dot) {
            int countDiag1 = 1;
            int countDiag2 = 1;
            for (int i = 1; i < win; i++) {
                if (isCellValid(x + i, y + i)) {
                    if (field[x + i][y + i] == dot) {
                        countDiag1++;
                    }
                }
                if (isCellValid(x - i, y + i)) {
                    if (field[x - i][y + i] == dot) {
                        countDiag2++;
                    }
                }
                if (countDiag1 == win || countDiag2 == win) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * проверка на победу
     *
     * @param dot
     * @param win
     * @return
     */
    static boolean checWin(char dot, int win) {
        for (int i = 0; i < fieldSizeX; i++) {
            for (int j = 0; j < fieldSizeY; j++) {
                if (checkHorizonVertical(i, j, dot, win) || checkDiagonal(i, j, dot, win)) return true;
            }
        }
        return false;
    }


    /**
     * Проверка игры на состояние
     *
     * @param dot
     * @param s
     * @return
     */
    static boolean checkState(char dot, int win, String s) {
        if (checWin(dot, win)) {
            System.out.println(s);
            return true;
        } else if (checkDraw()) {
            System.out.println("Ничья");
            return true;
        }
        return false;
    }

    /**
     * доработка ходов компьютера (мешает человеку выиграть)
     *
     * @param dot1
     * @param dot2
     * @param win
     * @return
     */
    static boolean checkAiMove(char dot1, char dot2, int win) {
        for (int i = 0; i < fieldSizeX; i++) {
            for (int j = 0; j < fieldSizeY; j++) {
                if (isCellEmpty(i, j)) {
                    field[i][j] = dot1;
                    if (checWin(dot1, win)) {
                        field[i][j] = dot2;
                        return false;
                    }
                    field[i][j] = DOT_EMPTY;
                }
            }
        }
        for (int i = 0; i < fieldSizeX; i++) {
            for (int j = 0; j < fieldSizeY; j++) {
                if (isCellEmpty(i, j)) {
                    field[i][j] = dot1;
                    if (checWin(dot1, win - 1)) {
                        field[i][j] = dot2;
                        return false;

                    }
                    field[i][j] = DOT_EMPTY;
                }
            }
        }
        return true;
    }
}




