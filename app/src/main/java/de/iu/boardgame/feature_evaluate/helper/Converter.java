package de.iu.boardgame.feature_evaluate.helper;

import android.os.Build;

import androidx.room.TypeConverter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class Converter {

    @TypeConverter
    public static Long fromLocalDateTime(LocalDateTime dateTime) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return dateTime == null
                    ? null
                    : dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        }
        return null;
    }

    @TypeConverter
    public static LocalDateTime toLocalDateTime(Long millis) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return millis == null
                    ? null
                    : LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault());
        }
        return null;
    }
}
