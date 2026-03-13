package com.mar.service;

import com.mar.api.dto.GameInfoResponse;
import com.mar.exception.SystemException;
import com.mar.exception.UserException;
import com.mar.utils.MineUtils;
import lombok.extern.slf4j.Slf4j;
import org.ehcache.core.Ehcache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Slf4j
@Service
public class MineServiceImpl implements MineService {

    @Autowired
    private Ehcache<String, GameInfoResponse> userGames;

    @Override
    public GameInfoResponse createNewGame(int width, int height, int minesCount) {
        String gameId = UUID.randomUUID().toString();
        GameInfoResponse rs = GameInfoResponse.builder()
                .gameId(gameId)
                .width(width)
                .height(height)
                .minesCount(minesCount)
                .completed(false)
                .field(createMinefield(width, height, minesCount))
                .build();

        userGames.put(gameId, rs);
        return GameInfoResponse.builder()
                .gameId(gameId)
                .width(width)
                .height(height)
                .minesCount(minesCount)
                .completed(false)
                .field(copyWithHiddenMines(rs.getField()))
                .build();
    }

    private Character[][] createMinefield(int width, int height, int minesCount) {
        try {
            return MineUtils.createMinefield(width, height, minesCount);
        } catch (MineUtils.ManyMinesException e) {
            throw new UserException("Указанное количество мин слишком большое. Их количество должно быть меньше ячеек поля.");
        }
    }

    @Override
    public GameInfoResponse userStep(String gameId, int col, int row) {
        if (isBlank(gameId)) {
            throw new UserException("Для продолжения игры необходимо идентификатор сессии 'game_id'.");
        }

        GameInfoResponse game = userGames.get(gameId);
        if (game == null) {
            throw new SystemException("Не найдена игровая сессия. Начните новую игру, пожалуйста.");
        }
        if (game.getCompleted()) {
            throw new UserException("Нельзя продолжать игру, т.к. она закончена.");
        }

        Character[][] uiField;
        try {
            game.setField(calcUserStep(game.getField(), col, row));
            if (checkEndgame(game.getField())) {
                game.setCompleted(true);
                uiField = game.getField();
            } else {
                uiField = copyWithHiddenMines(game.getField());
            }
        } catch (MineUtils.BoomException e) {
            game.setCompleted(true);
            uiField = copyWithBoomMines(game.getField());
        } catch (MineUtils.CheckOpenCellException e) {
            throw new UserException("Нельзя проверять уже открытую ячейку.");
        }

        return GameInfoResponse.builder()
                .gameId(gameId)
                .width(game.getWidth())
                .height(game.getHeight())
                .minesCount(game.getMinesCount())
                .completed(game.getCompleted())
                .field(uiField)
                .build();
    }

    private boolean checkEndgame(Character[][] field) {
        int closeCell = 0;
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                if (field[i][j] == MineUtils.CLOSE_CELL) {
                    closeCell++;
                }
            }
        }
        return closeCell == 0;
    }

    private Character[][] copyWithHiddenMines(Character[][] field) {
        Character[][] f = new Character[field.length][field[0].length];
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                f[i][j] = field[i][j] == MineUtils.MINE_CELL ? MineUtils.CLOSE_CELL : field[i][j];
            }
        }
        return f;
    }

    private Character[][] copyWithBoomMines(Character[][] field) {
        Character[][] f = new Character[field.length][field[0].length];
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                if (field[i][j] == MineUtils.MINE_CELL) {
                    f[i][j] = MineUtils.MINE_BOOM;
                } else {
                    f[i][j] = (char) ('0' + MineUtils.calcMineCount(field, i, j));
                }
            }
        }
        return f;
    }

    private Character[][] calcUserStep(Character[][] field, int col, int row) throws MineUtils.BoomException, MineUtils.CheckOpenCellException {
        return MineUtils.calcUserStep(field, row, col);
    }
}
