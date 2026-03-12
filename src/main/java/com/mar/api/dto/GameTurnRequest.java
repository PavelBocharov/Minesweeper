package com.mar.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameTurnRequest implements Serializable {

    @JsonProperty("game_id")
    private String gameId;
    private Integer col;
    private Integer row;

}
