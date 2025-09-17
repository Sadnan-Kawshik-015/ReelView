package com.example.demo.utils.deserializer;


import com.example.demo.utils.customexceptions.FieldValidationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class IntegerDeserializer extends JsonDeserializer<Integer> {
    @Override
    public Integer deserialize(JsonParser p, DeserializationContext dContext) throws IOException {
        String value = p.getText();
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new FieldValidationException(p.getCurrentName(),"Value must be a valid Integer number",value);

        }
    }
}