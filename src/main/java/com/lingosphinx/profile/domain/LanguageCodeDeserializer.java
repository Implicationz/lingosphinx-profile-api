package com.lingosphinx.profile.domain;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

class LanguageCodeDeserializer extends JsonDeserializer<LanguageCode> {
    @Override
    public LanguageCode deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return LanguageCode.valueOf(p.getValueAsString());
    }
}
