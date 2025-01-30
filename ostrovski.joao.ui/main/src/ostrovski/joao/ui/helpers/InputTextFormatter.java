package ostrovski.joao.ui.helpers;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import ostrovski.joao.common.helpers.ExceptionMessage;
import ostrovski.joao.common.helpers.Logger;

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class InputTextFormatter {
    
    public static void inputTextFormatter(String regex, TextField instance) {
        if (regex == null || regex.isEmpty() || instance == null) {
            Logger.log(new NullPointerException(ExceptionMessage.NULL_PARAM.getMessage()));
            return;
        }
        
        Pattern pattern = Pattern.compile(regex);
        UnaryOperator<TextFormatter.Change> filter = change -> {
            if (pattern.matcher(change.getControlNewText()).matches()) {
                return change;
            }
            return null;
        };

        TextFormatter<String> textFormatter = new TextFormatter<>(filter);
        instance.setTextFormatter(textFormatter);
    }
}