package ostrovski.joao.ui.helpers;

import ostrovski.joao.common.helpers.ExceptionMessage;
import ostrovski.joao.common.helpers.Logger;

import java.util.Locale;
import java.util.ResourceBundle;

public class ResourceBundleService {

    private static Locale locale = Locale.getDefault();
    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("ostrovski/joao/ui/i18n/Bundle", locale);

    public static ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    public static String getString(String key) {
        return resourceBundle.getString(key);
    }

    public static void setLocale(String language) {
        if (language == null || language.isEmpty()) {
            Logger.log(new NullPointerException(ExceptionMessage.NULL_PARAM.getMessage()));
            return;
        }

        locale = new Locale.Builder().setLanguage(language).build();
        resourceBundle = ResourceBundle.getBundle("ostrovski/joao/ui/i18n/Bundle", locale);
    }
}
