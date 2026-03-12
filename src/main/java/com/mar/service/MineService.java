package com.mar.service;

import com.mar.api.dto.GameInfoResponse;

public interface MineService {

    GameInfoResponse createNewGame(int width, int height, int minesCount);
    GameInfoResponse userStep(String gameId, int col, int row);

}
