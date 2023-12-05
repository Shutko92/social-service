package ru.skillbox.diplom.group42.social.service.utils.email;

import java.util.UUID;

public class EmailRecoveryUtil {

    public static final String THEME_EMAIL = "Изменение данных в учетной записи";

    public static String createLink(String host, UUID uuid) {
        return host + "/shift-email?uuid=" + uuid;
    }

    public static String buildEmailBody(String link) {
        return "Для изменения учетных данных вашего аккаунта перейдите по ссылке:\n\n" +
                link +
                "\n\n С уважением,\n Команда JAVAPRO42";
    }

    public static String getRefererUUID(String referer) {
        try {
            String[] uuidArray = referer.split("\\?");
            return uuidArray[1].substring(5);
        } catch (Exception ex) {
            return null;
        }
    }
}
