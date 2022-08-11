package mc.bowwars;

import java.lang.annotation.*;

/**
 * <b>Аннотация показа констант перечисленя</b>
 * @author pleer__gr45
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ShowEnumConstant {
    /**
     * <b>Показ</b>
     * <br><b>true</b> - Показать
     * <br><b>false</b> - Не показать
     * <p><b>Значение по умолчанию - <i>true</i></b>
     */
    boolean show() default true;
}
