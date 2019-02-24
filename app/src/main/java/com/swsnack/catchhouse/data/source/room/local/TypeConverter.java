package com.swsnack.catchhouse.data.source.room.local;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class TypeConverter {

    @androidx.room.TypeConverter
    public static List<String> convertToJson(String jsonList) {
        if(jsonList == null) {
            return new ArrayList<>();
        }

        return new Gson().fromJson(jsonList, new TypeToken<List<String>>() {}.getType());
    }

    @androidx.room.TypeConverter
    public static String convertToString(List<String> stringList) {
        if(stringList == null) {
            return null;
        }

        return new Gson().toJson(stringList);
    }
}
