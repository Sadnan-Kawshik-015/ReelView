package com.example.demo.dtos.output;

import com.example.demo.utils.deserializer.IntegerDeserializer;
import com.example.demo.utils.deserializer.LongDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaginationOutputDTO {
    @JsonDeserialize(using = IntegerDeserializer.class)
    private Integer current;
    @NotNull
    private Integer pageSize;
    @JsonDeserialize(using = LongDeserializer.class)
    private Long total;
}
