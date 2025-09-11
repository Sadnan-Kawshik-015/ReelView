package com.example.demo.dtos.output;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseModelDTO {
    private String status;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String correlation_id;
    @Builder.Default
    private String timestamp = LocalDateTime.now().toString();
    private Object data;
}

