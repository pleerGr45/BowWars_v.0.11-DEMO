package mc.bowwars;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * <b>Перечисление сообщений об убийствах</b>
 * @author pleer__gr45
 */
public enum DeathMessages {

    @ShowEnumConstant COMMON("%p &r&fбыл убит игроком &r%k"),
    @ShowEnumConstant BULLING("&fИгрока &r%p &r&fжёстко забуллил игрок &r%k"),
    @ShowEnumConstant HUMILIATION("&fИгрок &r%p &r&fбыл унижен игроком &r%k");

    /**
     * <b>Сообщение</b>
     */
    String message;

    /**
     * <b>онструктор перечисления {@link DeathMessages}</b>
     * @param message
     */
    DeathMessages(String message) {
        this.message = message;
    }

    /**
     * <b>Метод получения всех констант данного перечисления</b>
     * <br>Метод берёт все константы аннотирование {@link ShowEnumConstant @ShowEnumConstant}
     * @see ShowEnumConstant @ShowEnumConstant
     * @return Колличество всех констант в перечислении
     */
    public static byte getSize() {
        byte size = 0;
        for (Field field : DeathMessages.class.getDeclaredFields()) {
            for (Annotation annotation : field.getDeclaredAnnotations()) {
                if (annotation.annotationType().equals(ShowEnumConstant.class)) {
                    size++;
                }
            }
        }
        return size;
    }

    /**
     * <b>Метод преобразование строки</b>
     * <br>Преобразует %p и %k в имена игроков
     * <br><b>%p</b> - Игрок
     * <br><b>%k</b> - Убийца
     * @param message <i>Сообщение ({@link DeathMessages})</i>
     * @param player <i>Игрок, котого убили</i>
     * @param killer <i>Убийца</i>
     * @return Преобразованна строка
     */
    private static String convertToFinalMessage(String message, String player, String killer) {
        String finalMessage = "";
        for(byte i = 0; i < message.length(); i++) {
            if(message.charAt(i) == '%') {
                if ((i+1) < message.length())
                    if (message.charAt(i + 1) == 'p') {
                        finalMessage+=player;
                        i++;
                        continue;
                    } else if(message.charAt(i + 1) == 'k'){
                        finalMessage+=killer;
                        i++;
                        continue;
                    }
            }
            finalMessage += message.charAt(i);
        }
        return finalMessage;
    }

    /**
     * <b>Метод получения сообщения об убийстве</b>
     * @param player <i>Игрок, котого убили</i>
     * @param killer <i>Убийца</i>
     * @param message <i>Сообщение ({@link DeathMessages})</i>
     * @return Строка сообщения
     */
    public static String getDeathMessage(String player, String killer, DeathMessages message){
        return convertToFinalMessage(message.message, player, killer);
    }

    /**
     * <b>Метод получения рандомного сообщения об убийстве</b>
     * @see DeathMessages#getSize()
     * @param player <i>Игрок, котого убили</i>
     * @param killer <i>Убийца</i>
     * @return Строка сообщения
     */
    @Deprecated
    public static String getRandomDeathMessage(String player, String killer){
        switch ((byte)(Math.random()*getSize())){
            case 1: return convertToFinalMessage(BULLING.message, player, killer);
            case 2: return convertToFinalMessage(HUMILIATION.message, player, killer);
            default: return convertToFinalMessage(COMMON.message, player, killer);
        }
    }

}
