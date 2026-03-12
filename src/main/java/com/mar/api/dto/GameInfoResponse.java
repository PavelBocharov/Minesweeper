package com.mar.api.dto;

import com.fasterxml.jackson.annotation.JsonKey;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameInfoResponse implements Serializable {

    @JsonProperty("game_id")
    private String gameId;
    private Integer width;
    private Integer height;
    @JsonProperty("mines_count")
    private Integer minesCount;
    private Boolean completed;
    private Character[][] field;

}
