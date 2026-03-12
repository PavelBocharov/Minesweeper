package com.mar.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewGameRequest implements Serializable {

    private Integer width;
    private Integer height;
    @JsonProperty("mines_count")
    private Integer minesCount;

}
