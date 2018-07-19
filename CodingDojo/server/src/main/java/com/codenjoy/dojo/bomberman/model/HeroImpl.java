package com.codenjoy.dojo.bomberman.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import static com.codenjoy.dojo.bomberman.model.Elements.BOMBERMAN;
import static com.codenjoy.dojo.bomberman.model.Elements.BOMB_BOMBERMAN;
import static com.codenjoy.dojo.bomberman.model.Elements.DEAD_BOMBERMAN;
import static com.codenjoy.dojo.bomberman.model.Elements.DEAD_MEAT_CHOPPER;
import static com.codenjoy.dojo.bomberman.model.Elements.MEAT_CHOPPER;
import static com.codenjoy.dojo.bomberman.model.Elements.OTHER_BOMBERMAN;
import static com.codenjoy.dojo.bomberman.model.Elements.OTHER_BOMB_BOMBERMAN;
import static com.codenjoy.dojo.bomberman.model.Elements.OTHER_DEAD_BOMBERMAN;

import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.PointImpl;

/**
 * User: oleksandr.baglai
 * Date: 3/7/13
 * Time: 9:41 AM
 */
public class HeroImpl extends PointImpl implements Hero {
    private static final boolean WITHOUT_MEAT_CHOPPER = false;
    private Level level;
    private Dice dice;
    private Field board;
    private boolean alive;
    private boolean bomb;
    private Direction direction;
    private boolean bot;

    public HeroImpl(Level level, Dice dice) {
        super(-1, -1);
        this.level = level;
        this.dice = dice;
        alive = true;
        direction = null;
    }

    @Override
    public void init(Bomberman board, boolean bot) {
        this.board = board.getLevel();
        this.bot = bot;
        int count = 0;
        do {
            x = dice.next(this.board.size());
            y = dice.next(this.board.size());
            while (isBusy(x, y) && !isOutOfBoard(x, y)) {
                x++;
                if (isBusy(x, y)) {
                    y++;
                }
            }
        } while ((isBusy(x, y) || isOutOfBoard(x, y)) && count++ < 1000);

        if (count >= 1000) {
            throw new RuntimeException("Dead loop at MyBomberman.init(Board)!");
        }
    }

    @Override
    public boolean isBot() {
        return bot;
    }

    private boolean isBusy(int x, int y) {
        for (Hero bomberman : board.getBombermans()) {
            if (bomberman != null && bomberman.itsMe(this) && bomberman != this) {
                return true;
            }
        }

        return this.board.getWalls().itsMe(x, y);
    }

    private boolean isOutOfBoard(int x, int y) {
        return x >= board.size() || y >= board.size() || x < 0 || y < 0;
    }

    @Override
    public void right() {
        if (!alive) return;

        direction = Direction.RIGHT;
    }

    @Override
    public void down() {
        if (!alive) return;

        direction = Direction.DOWN;
    }

    @Override
    public void up() {
        if (!alive) return;

        direction = Direction.UP;
    }

    @Override
    public void left() {
        if (!alive) return;

        direction = Direction.LEFT;
    }

    @Override
    public void act(int... p) {
        if (!alive || bot) return;

        if (direction != null) {
            bomb = true;
        } else {
            setBomb(x, y);
        }
    }

    @Override
    public void message(String command) {
        // do nothing, this should never happen
    }

    @Override
    public void apply() {
        if (!alive) return;

        if (direction == null) {
            return;
        }

        int newX = direction.changeX(x);
        int newY = direction.changeY(y);

        if (!board.isBarrier(iAmBot() ? PointImpl.pt(x, y) : null, newX, newY, WITHOUT_MEAT_CHOPPER)) {
            move(newX, newY);
        }
        direction = null;

        if (bomb && !bot) {
            setBomb(x, y);
            bomb = false;
        }
    }

    private boolean iAmBot() {
        return bot;
    }

    private void setBomb(int bombX, int bombY) {
        if (board.getBombs(this).size() < level.bombsCount().getValue()) {
            board.drop(new Bomb(this, bombX, bombY, level.bombsPower().getValue(), board));
        }
    }

    @Override
    public boolean isAlive() {
        return alive;
    }

    @Override
    public void kill() {
        alive = false;
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        Bomb bomb = null;

        if (alsoAtPoint[1] != null) {
            if (alsoAtPoint[1] instanceof Bomb) {
                bomb = (Bomb) alsoAtPoint[1];
            }
        }

        if (isAlive()) {
            if (this == player.getBomberman()) {
                if (bomb != null) {
                    return BOMB_BOMBERMAN;
                } else {
                    return BOMBERMAN;
                }
            } else {
                if (bot) {
                    return MEAT_CHOPPER;
                }
                if (bomb != null) {
                    return OTHER_BOMB_BOMBERMAN;
                } else {
                    return OTHER_BOMBERMAN;
                }
            }
        } else {
            if (this == player.getBomberman()) {
                return DEAD_BOMBERMAN;
            } else {
                if (bot) {
                    return DEAD_MEAT_CHOPPER;
                }
                return OTHER_DEAD_BOMBERMAN;
            }
        }
    }
}
