package ostrovski.joao.common.helpers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Logger {

    public static void log(Exception exception) {

        List<String> lines = getLog(exception);
        writeLog(lines);

    }

    public static synchronized List<String> getLog(Exception exception) {

        List<String> lines = new ArrayList<>();
        int size = exception.getStackTrace().length - 1;
        StackTraceElement[] e = exception.getStackTrace();
        for (int i = size; i > -1; i--) {
            String methodName = e[i].getMethodName();
            String className = e[i].getClassName();
            String getFileName = e[i].getFileName();
            int getLineNumber = e[i].getLineNumber();

            StringBuilder clickableStack = new StringBuilder();
            if (i > 0) {
                clickableStack.append("\t ");
            }

            if (i == 0) {
                clickableStack
                        .append(LocalDateTime.now())
                        .append(" : ");
            }

            clickableStack.append(className);
            clickableStack.append(" on method -> ");
            clickableStack.append(methodName);
            clickableStack.append(" --> ").append("(").append(getFileName).append(":").append(getLineNumber).append(")");

            if (i == 0) {
                clickableStack
                        .append(" : ")
                        .append(exception.getMessage())
                        .append("\n");
            }

            System.err.println(clickableStack);

            lines.add(clickableStack.toString());
        }
        return lines;
    }

    public static void writeLog(List<String> lines) {

        Path path = Paths.get("logs/logger-" + LocalDate.now() + ".txt");
        try {
            Files.createDirectories(path.getParent());
            Files.write(path, lines,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException ex) {
            System.err.println("Error writing log file " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}