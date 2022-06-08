package com.optus.infosec.api.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

/**
 * @author SM
 *
 * Custom Local Date Time Deserializer
 */
public class CustomLocalDateTimeDeserializer extends StdDeserializer<LocalDateTime> {

    public CustomLocalDateTimeDeserializer() {
        this(null);
    }

    public CustomLocalDateTimeDeserializer(Class t) {
        super(t);
    }

    @Override
    public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {

        String val = jsonParser.getValueAsString();
        LocalDateTime localDateTime = null;
        if(val!=null){
            localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.valueOf(val)), TimeZone.getDefault().toZoneId());
        }
        return localDateTime;
    }
}
