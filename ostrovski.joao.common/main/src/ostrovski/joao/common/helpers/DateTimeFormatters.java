package ostrovski.joao.common.helpers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public class DateTimeFormatters {

  public static String getFormattedLocalDateWithLocale(String localDateString) {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd").withLocale(Locale.getDefault());
    LocalDate localDate = LocalDate.parse(localDateString, formatter);

    DateTimeFormatter formatterMedium = DateTimeFormatter.ofPattern("dd MMM, yyyy").withLocale(Locale.getDefault());

      return localDate.format(formatterMedium);
  }

  public static String getFormattedLocalDateWithLocale(LocalDate localDate) {

    DateTimeFormatter formatterMedium = DateTimeFormatter.ofPattern("dd MMM, yyyy").withLocale(Locale.getDefault());

      return localDate.format(formatterMedium);
  }

  public static String getFormattedLocalDateTimeWithLocale(String localDateTimeString) {
    
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd H:m:s").withLocale(Locale.getDefault());
    LocalDateTime localDateTime = LocalDateTime.parse(localDateTimeString, formatter);
    
    DateTimeFormatter formatterMedium = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(Locale.getDefault());

      return localDateTime.format(formatterMedium);
  }
}
