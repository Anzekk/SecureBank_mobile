package com.secure_bank.bank.Util;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Date;

public class CustomGsonBuilder {
    public static GsonBuilder getBuilder() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            public Date deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                Date date;
                String string = jsonElement.getAsJsonPrimitive().getAsString();
                Date date2 = new Date();
                try {
                    if (string.toLowerCase().contains("z")) {
                        date = DateTimeHelper.convertToDateFromString(string, "yyyy-MM-dd'T'HH:mm:s'Z'");
                    } else {
                        date = DateTimeHelper.convertToDateFromString(string, "yyyy-MM-dd'T'HH:mm:s");
                    }
                    return date;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return date2;
                }
            }
        });
        return gsonBuilder;
    }

}
