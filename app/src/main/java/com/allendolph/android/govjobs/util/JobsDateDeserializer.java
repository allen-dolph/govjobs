package com.allendolph.android.govjobs.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by allendolph on 1/26/15.
 */
public class JobsDateDeserializer implements JsonDeserializer<java.util.Date> {
    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        String[] shortDate = json.getAsString().split("-");

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Integer.parseInt(shortDate[0]));
        cal.set(Calendar.MONTH, Integer.parseInt(shortDate[1]));
        cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(shortDate[2]));

        return cal.getTime();
    }
}
