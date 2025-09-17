package com.example.demo.dtos.input;

import com.example.demo.utils.deserializer.IntegerDeserializer;
import com.example.demo.utils.deserializer.LongDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;
import jakarta.validation.constraints.NotNull;

public class PaginationDTO {
    @JsonDeserialize(using = IntegerDeserializer.class)
    private Integer current;
    @NotNull
    private Integer pageSize;
    @JsonDeserialize(using = LongDeserializer.class)
    private Long total;
}
