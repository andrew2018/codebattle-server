package com.codenjoy.dojo.services;

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


import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;

/**
 * Это интерфейс указывает на тим игры. Как только ты его реулизуешь -
 * на админке (http://localhost:8080/codenjoy-contest/admin31415)
 * будет возможность переключиться на твою игру.
 */
public interface GameType extends Tickable {

    /**
     * @param score значения очков перед началом игры (используется например при загрузке игры из save)
     * @return Возвращается объект который умеет в зависимости от типа события на карте подчитывать очки игроков
     */
    PlayerScores getPlayerScores(Object score);

    /**
     * Так фреймворк будет стартовать новую игру для каждого пользователя
     * @param listener Через этот интерфейс фреймворк будет слушать какие ивенты возникают в твоей игре
     * @param factory Через этот интерфейс фреймворк будет инджектить принтер для отрисовки поля
     * @param save Если игре надо передать что-то чтобы ее настроить, например сейв игрока - это то самое место
     * @param playerName Имейл игровка зарегавшегося на сервере
     * @return Экземпляр игры пользователя
     */
    Game newGame(EventListener listener, PrinterFactory factory, String save, String playerName);

    default Game newGame(EventListener listener, PrinterFactory factory, String save, String playerName, boolean bot) {
        return newGame(listener, factory, save, playerName);
    }

    /**
     * @return Размер доски. Важно, чтобы у всех пользователей были одинаковые по размеру поля
     */
    Parameter<Integer> getBoardSize();

    /**
     * @return Имя твоей игры
     */
    String name();

    /**
     * @return Список элементов отображаеммых на доске
     * Смотри класс com.codenjoy.dojo.sample.model.Elements конкретной игры
     */
    Enum[] getPlots();

    /**
     * @return Настройки игры
     * @see Settings
     */
    Settings getSettings();

    /**
     * Существует два режима игры. Для начала реализуй - каждый на своей отдельной доске.
     * Позже можешь пробовать мультиплеерную игру создать.
     * Смотри com.codenjoy.dojo.sample.model.Single
     * Смотри com.codenjoy.dojo.sample.model.Sample
     * @return false - если каждый будет играть на своей отдельной доске, true - если все на одной доске
     */
    boolean isSingleBoard();

    /**
     * Каждая игра должна предоставить своего AI который будет развлекать новопришедших игроков.
     * @param aiName имя бота
     */
    boolean newAI(String aiName);

    /**
     * Если подложить в 'src\main\resources\gameName\version.properties' игры строчку 'version=${project.version}'
     * то ее потом мождно будет прочитать с помощью VersionReader.getCurrentVersion();
     * @return версия игры
     */
    String getVersion();

    Object getGame();
}



















