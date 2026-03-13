package com.mar.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewGameRequest implements Serializable {

    @Min(2)
    @Max(50)
    private Integer width;

    @Min(2)
    @Max(50)
    private Integer height;

    @JsonProperty("mines_count")
    private Integer minesCount;

}
