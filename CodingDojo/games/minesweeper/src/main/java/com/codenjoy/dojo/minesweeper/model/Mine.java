package com.codenjoy.dojo.minesweeper.model;

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


import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;

/**
 * User: oleksii.morozov
 * Date: 10/14/12
 * Time: 12:44 PM
 */
public class Mine extends PointImpl implements State<Elements, Object> {

    private Field board;

    public Mine(Point point) {
        super(point);
    }

    public Mine(int x, int y) {
        super(x, y);
    }

    @Override
    public Elements state(Object player, Object... alsoAtPoint) {
        if (!board.isGameOver()) return null;

        if (board.isFlag(this)) {
            return Elements.DESTROYED_BOMB;
        } else {
            return Elements.HERE_IS_BOMB;
        }
    }

    public void setBoard(Field board) {
        this.board = board;
    }
}
