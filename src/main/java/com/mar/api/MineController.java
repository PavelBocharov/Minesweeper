package com.mar.api;

import com.mar.api.dto.ErrorResponse;
import com.mar.api.dto.GameInfoResponse;
import com.mar.api.dto.GameTurnRequest;
import com.mar.api.dto.NewGameRequest;
import com.mar.exception.UserException;
import com.mar.service.MineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class MineController {

    private final MineService mineService;

    @PostMapping("new")
    @CrossOrigin(origins = "https://minesweeper-test.studiotg.ru")
    public GameInfoResponse newGame(@Valid @RequestBody NewGameRequest rq) {
        log.debug("Create new game RQ: {}", rq);
        return mineService.createNewGame(rq.getWidth(), rq.getHeight(), rq.getMinesCount());
    }

    @PostMapping("turn")
    @CrossOrigin(origins = "https://minesweeper-test.studiotg.ru")
    public GameInfoResponse userStep(@RequestBody GameTurnRequest rq) {
        log.debug("User step RQ: {}", rq);
        return mineService.userStep(rq.getGameId(), rq.getCol(), rq.getRow());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorResponse> userHandleException(Exception ex) {
        log.warn("User/UI exception.", ex);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(ex.getLocalizedMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> serverHandleException(Exception ex) {
        log.error("Server exception.", ex);

        if (ex instanceof BindException bindException) {
            String validError = bindException.getAllErrors().stream()
                    .map(err -> {
                        if (err instanceof FieldError) {
                            return "'%s': %s.".formatted(((FieldError) err).getField(), err.getDefaultMessage());
                        }
                        return String.valueOf(err);
                    }).collect(Collectors.joining("\n"));
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(validError));
        }

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(ex.getLocalizedMessage()));
    }
}
