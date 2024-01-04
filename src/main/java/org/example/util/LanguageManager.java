package org.example.util;

import org.springframework.stereotype.Component;
import java.util.Locale;
import java.util.ResourceBundle;
@Component
public class LanguageManager {
    private Locale locale;
    private ResourceBundle messages;

    public LanguageManager() {
        this.locale = Locale.getDefault();
        this.messages = ResourceBundle.getBundle("messages", locale);
    }
    public Locale getLocale() {
        return locale;
    }
    public void setLocale(Locale locale) {
        this.locale = locale;
        this.messages = ResourceBundle.getBundle("messages", locale);
    }

    public String getString(String key) {
        return messages.getString(key);
    }
}
