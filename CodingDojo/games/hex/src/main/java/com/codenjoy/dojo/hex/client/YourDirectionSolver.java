package com.codenjoy.dojo.hex.client;


import com.codenjoy.dojo.hex.client.utils.BoardImpl;
import com.codenjoy.dojo.hex.client.utils.Dice;
import com.codenjoy.dojo.hex.client.utils.Point;

/**
 * User: your name
 */
public class YourDirectionSolver implements DirectionSolver {

    private Dice dice;
    private BoardImpl board;

    public YourDirectionSolver(Dice dice) {
        this.dice = dice;
    }

    @Override
    public String get(BoardImpl board) {
        this.board = board;

        Point point = board.get(Element.HERO1).get(0);

        boolean jump = false;
        return command(point.getX(), point.getY(), Direction.UP, jump);
    }

    private String command(int x, int y, Direction direction, boolean jump) {
        return Direction.ACT.toString() + "(" + x + "," + y + ((jump)?",1":"")  + ")," + direction.toString();
    }

}