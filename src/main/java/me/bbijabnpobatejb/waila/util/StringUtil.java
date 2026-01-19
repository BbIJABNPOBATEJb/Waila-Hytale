package me.bbijabnpobatejb.waila.util;

import lombok.experimental.UtilityClass;
import lombok.val;

import java.util.Locale;

@UtilityClass
public class StringUtil {


    public String fromFloat(float f, int count) {
        val format = "%." + count + "f";
        return String.format(Locale.US, format, f);
    }
}
