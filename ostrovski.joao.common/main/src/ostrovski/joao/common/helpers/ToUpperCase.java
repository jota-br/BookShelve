package ostrovski.joao.common.helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ToUpperCase {

  public static String firstLetters(String string) {
    String[] stringsArray = string.split(" ");

      return Arrays.stream(stringsArray)
                  .map(s -> Character.toUpperCase(s.charAt(0)) + s.substring(1))
                  .collect(Collectors.joining(" "));
  }

  public static List<String> firstLetters(List<String> strings) {
    List<String> newStrings = new ArrayList<>();
    for (String strs : strings) {
      String[] stringsArray = strs.split(" ");
      String finalStr = Arrays.stream(stringsArray)
              .map(s -> Character.toUpperCase(s.charAt(0)) + s.substring(1))
              .collect(Collectors.joining(" "));
      newStrings.add(finalStr);
    }

    return newStrings;
  }
}
