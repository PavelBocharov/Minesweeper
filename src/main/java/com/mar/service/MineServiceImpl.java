package com.mar.service;

import com.mar.api.dto.GameInfoResponse;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static java.util.Objects.nonNull;

@Service
public class MineServiceImpl implements MineService {

    public static final Map<String, GameInfoResponse> userGames = Collections.synchronizedMap(new HashMap<>());

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
        return rs;
    }

    private Character[][] createMinefield(int width, int height, int minesCount) {
        return new Character[width][height];
    }

    @Override
    public GameInfoResponse userStep(String gameId, int col, int row) {
        assert nonNull(gameId);
        assert !gameId.isBlank();

        GameInfoResponse game = userGames.get(gameId);
        assert nonNull(game);

        game.setField(calcUserStep(game.getField(), col, row));

        return game;
    }

    private Character[][] calcUserStep(Character[][] field, int col, int row) {
        // throw end game
        return field;
    }
}
