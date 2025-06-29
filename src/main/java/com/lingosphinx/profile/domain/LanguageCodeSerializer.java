package com.lingosphinx.profile.domain;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

class LanguageCodeSerializer extends JsonSerializer<LanguageCode> {
    @Override
    public void serialize(LanguageCode value, com.fasterxml.jackson.core.JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(value.getValue());
    }
}
