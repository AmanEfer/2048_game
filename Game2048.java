package com.javarush.games.game2048;

import com.javarush.engine.cell.*;

public class Game2048 extends Game {

    private static final int SIDE = 4;
    private int[][] gameField = new int[SIDE][SIDE];
    private boolean isGameStopped;
    private int score;


    @Override
    public void initialize() {
        setScreenSize(SIDE, SIDE);
        createGame();
        drawScene();
    }


    private void createGame() {
        gameField = new int[SIDE][SIDE];
        createNewNumber();
        createNewNumber();
    }


    private void drawScene() {
        for (int y = 0; y < gameField.length; y++) {
            for (int x = 0; x < gameField.length; x++) {
                setCellColoredNumber(x, y, gameField[y][x]);
            }
        }
    }


    private void createNewNumber() {
        if (getMaxTileValue() == 2048) {
            win();
        }
        int x, y;
        do {
            x = getRandomNumber(SIDE);
            y = getRandomNumber(SIDE);
        } while (gameField[y][x] != 0);

        gameField[y][x] = getRandomNumber(10) == 9 ? 4 : 2;
    }


    private Color getColorByValue(int value) {
        Color result = null;
        switch (value) {
            case 0:
                result = Color.WHITE;
                break;
            case 2:
                result = Color.LIGHTPINK;
                break;
            case 4:
                result = Color.PALEVIOLETRED;
                break;
            case 8:
                result = Color.BLUE;
                break;
            case 16:
                result = Color.LIGHTBLUE;
                break;
            case 32:
                result = Color.LIGHTGREEN;
                break;
            case 64:
                result = Color.LIMEGREEN;
                break;
            case 128:
                result = Color.ORANGE;
                break;
            case 256:
                result = Color.YELLOW;
                break;
            case 512:
                result = Color.DARKORANGE;
                break;
            case 1024:
                result = Color.PINK;
                break;
            case 2048:
                result = Color.DARKVIOLET;
        }
        return result;
    }


    private void setCellColoredNumber(int x, int y, int value) {
        Color colorByValue = getColorByValue(value);
        if (value == 0) {
            setCellValueEx(x, y, colorByValue, "");
        } else {
            setCellValueEx(x, y, colorByValue, String.valueOf(value));
        }
    }


    private boolean compressRow(int[] row) {
        boolean result = false;
        for (int i = 0; i < row.length - 1; i++) {
            if (row[i] == 0) {
                for (int j = i + 1; j < row.length; j++) {
                    if (row[j] > 0) {
                        row[i] = row[j];
                        row[j] = 0;
                        result = true;
                        break;
                    }
                }
            }
        }
        return result;
    }


    private boolean mergeRow(int[] row) {
        boolean result = false;
        for (int i = 0; i < row.length - 1; i++) {
            if (row[i] > 0) {
                if (row[i] == row[i + 1]) {
                    row[i] += row[i + 1];
                    row[i + 1] = 0;
                    result = true;
                    score += row[i];
                    setScore(score);
                }
            }
        }
        return result;
    }


    @Override
    public void onKeyPress(Key key) {
        if (isGameStopped && key == Key.SPACE) {
            isGameStopped = false;
            createGame();
            drawScene();
            score = 0;
            setScore(score);
        }

        if (!canUserMove()) {
            gameOver();
            return;
        }

        if (key == Key.LEFT && !isGameStopped) {
            moveLeft();
            drawScene();
        } else if (key == Key.RIGHT && !isGameStopped) {
            moveRight();
            drawScene();
        } else if (key == Key.UP && !isGameStopped) {
            moveUp();
            drawScene();
        } else if (key == Key.DOWN && !isGameStopped) {
            moveDown();
            drawScene();
        }
    }


    private void moveLeft() {
        int count = 0;
        boolean isMovedFirst;
        boolean isMerged;
        for (int[] element : gameField) {
            isMovedFirst = compressRow(element);
            isMerged = mergeRow(element);
            compressRow(element);
            if (isMovedFirst || isMerged) {
                count++;
            }
        }
        if (count > 0) {
            createNewNumber();
        }
    }


    private void moveRight() {
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
    }


    private void moveUp() {
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
    }


    private void moveDown() {
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
    }


    private void rotateClockwise() {
        int[][] temp = new int[SIDE][SIDE];
        for (int x = 0; x < gameField.length; x++) {
            for (int y = 0; y < gameField.length; y++) {
                temp[x][y] = gameField[gameField.length - 1 - y][x];
            }
        }
        gameField = temp;
    }


    private int getMaxTileValue() {
        int max = 0;
        for (int[] row : gameField) {
            for (int element : row) {
                max = Math.max(max, element);
            }
        }
        return max;
    }


    private boolean canUserMove() {
        int count = 0;
        for (int[] row : gameField) {
            for (int element : row) {
                if (element == 0) {
                    count++;
                }
            }
        }
        if (count > 0) {
            return true;
        }

        for (int[] row : gameField) {
            for (int x = 0; x < gameField.length - 1; x++) {
                if (row[x] == row[x + 1]) {
                    return true;
                }
            }
        }

        for (int y = 0; y < gameField.length - 1; y++) {
            for (int x = 0; x < gameField.length; x++) {
                if (gameField[y][x] == gameField[y + 1][x]) {
                    return  true;
                }
            }
        }
        return false;
    }


    private void win() {
        isGameStopped = true;
        showMessageDialog(Color.WHITE, "YOU WIN", Color.GREEN, 70);
    }

    private void gameOver() {
        isGameStopped = true;
        showMessageDialog(Color.BLACK, "YOU LOSE", Color.RED, 70);
    }
}