package fr.alkanife.amiria;

import java.text.MessageFormat;
import java.util.HashMap;

public class Lang {

    private static HashMap<String, Object> translations = new HashMap<>();

    public static boolean load() {
        try {
            HashMap<String, Object> data = YmlReader.read("lang");

            if (data == null) {
                Amiria.getLogger().error("Data is null");
                return false;
            }

            translations = data;
        } catch (Exception exception) {
            Amiria.getLogger().error("Error while reading YML File", exception);
            Amiria.broadcastError("Error while reading YML File");
            return false;
        }

        return true;
    }

    public static String t(String key, String... values) {
        if (translations.containsKey(key)) {
            MessageFormat messageFormat = new MessageFormat(String.valueOf(translations.get(key)));
            return messageFormat.format(values);
        } else return "{Traduction manquante : " + key + "}";
    }

    public static HashMap<String, Object> getTranslations() {
        return translations;
    }
}
