package ostrovski.joao.common.helpers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateChecker {

    public static boolean dateChecker(String date) {

        Pattern pattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
        Matcher matcher = pattern.matcher(date);

        return matcher.matches();
    }
}
