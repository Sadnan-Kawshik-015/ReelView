package com.example.demo.utils.deserializer;

import com.example.demo.utils.customexceptions.FieldValidationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class LongDeserializer extends JsonDeserializer<Long> {
    @Override
    public Long deserialize(JsonParser p, DeserializationContext dContext) throws IOException {
        String value = p.getText();
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            //throw InvalidFormatException.from(p, "Invalid format for Long data type. Rejected value: " + value, value, Integer.class);
            throw new FieldValidationException(p.getCurrentName(),"Value must be a valid Integer number",value);

        }
    }
}
