package ostrovski.joao.common.helpers;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DateTimeParser {
  public static LocalDate getParsedLocalDate(String localDateString) {
    LocalDate parsedLocalDate = null;
    if (localDateString != null && !localDateString.isEmpty()) {
      parsedLocalDate = LocalDate.parse(localDateString);
    }
    return parsedLocalDate;
  }

  public static LocalDateTime getParsedLocalDateTime(String localDateTimeString) {
    LocalDateTime parsedLocalDateTime = null;
    if (localDateTimeString != null && !localDateTimeString.isEmpty()) {
      parsedLocalDateTime = LocalDateTime.parse(localDateTimeString);
    }
    return parsedLocalDateTime;
  }
}
