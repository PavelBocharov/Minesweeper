package com.mar.api;

import com.mar.api.dto.GameInfoResponse;
import com.mar.api.dto.GameTurnRequest;
import com.mar.api.dto.NewGameRequest;
import com.mar.service.MineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class MineController {

    private final MineService mineService;

    @PostMapping("new")
    public GameInfoResponse newGame(@RequestBody NewGameRequest rq) {
        log.debug("Create new game RQ: {}", rq);
        return mineService.createNewGame(rq.getWidth(), rq.getHeight(), rq.getMinesCount());
    }

    @PostMapping("turn")
    public GameInfoResponse userStep(@RequestBody GameTurnRequest rq) {
        log.debug("User step RQ: {}", rq);
        return mineService.userStep(rq.getGameId(), rq.getCol(), rq.getRow());
    }

}
