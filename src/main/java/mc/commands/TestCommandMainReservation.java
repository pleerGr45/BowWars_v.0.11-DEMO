package mc.commands;
import java.util.Scanner;

/**
 * <p><b>Класс-тест методов класса {@link mc.commands.CommandMain CommandMain}</b>
 * <br> Класс создан для тестирования и улучшения методов класса {@link mc.commands.CommandMain CommandMain}
 * .Впоследствии класс был аннотирован {@link Deprecated @Deprecated} и сохранён в качестве истории
 * <p><b>Методы:</b>
 * <br><i><b>{@link TestCommandMainReservation#logic(int, String, int) logic(...)}</b></i>
 * <br><i><b>{@link TestCommandMainReservation#nextLogic(int, String) nextLogic(...)}</b></i>
 * @author pleer__gr45
 */
@Deprecated
final class TestCommandMainReservation {

    /**
     * <p><b>Запуск теста</b>
     * <br>Выводит интервал от первого введённого числа до второго в консоль
     * @param args аргументы
     */
    @Deprecated
    public static void main(String[] args) {

        for(int x = 4; logic(x, ">=", 0); x = nextLogic(x, ">=")) {
            for(int y = -5; logic(y, "<=", 6); y = nextLogic(y, "<=")) {
                for(int z = -2; logic(z, "<=", 0); z = nextLogic(z, "<=")) {
                    System.out.println("("+x+", "+y+", "+z+")");
                }
            }
        }

        Scanner in = new Scanner(System.in);
        System.out.print("Изменяемое значение - "); int in_x = in.nextInt();
        System.out.print("Неизменяемое значение - "); int parX = in.nextInt();
        in.close();

        for(; logic(in_x, "<=", parX); in_x = nextLogic(in_x, "<=")) {
            System.out.println(in_x+"");
        }
    }

    /**
     * <p><b>Закрытый конструктор</b>
     * <br>Невозможно создать объект данного класса
     */
    private TestCommandMainReservation() {}

    /**
     * <p><b>Метод увеличения или уменьшения числа в зависимости от операции</b>
     * <br> Метод взят из {@link mc.commands.CommandMain CommandMain} для тестирования и улучшения.
     * Впоследствии метод был аннотирован {@link Deprecated @Deprecated} и сохранён в качестве истории
     * @param x <i>параметр, кторой нужно увеличить/уменьшить</i>
     * @param op <i>Операция</i>
     * @return изменённый параметр x
     */
    @Deprecated
    private static int nextLogic(int x, String op) {

        //Финальный вариант
        if(op.equals(">=")) {
            return  x-1;
        } else if(op.equals("<=")) {
            return x+1;
        }
        return x;

        //Попытка
        /*
        if(x > 0){
            if(parX > 0) {
                if (x <= parX) return x + 1;
                else return x - 1;
            }
            else if(parX < 0) return x - 1;
            else return x - 1;
        } else if(x < 0) {
            if(parX > 0) {return x+1;}
            else if(parX < 0) {
                if (x <= parX) return x + 1;
                else return x - 1;
            }
            else {return x+1;}
        } else {
            if(parX > 0) {return x+1;}
            else if(parX < 0) {return x-1;}
            else {return x?;}
        }
        */

    }

    /**
     * <p><b>Метод увеличения проверки неравенства</b>
     * <br> Метод взят из {@link mc.commands.CommandMain}  CommandMain} для тестирования и улучшения. Впоследствии метод был аннотирован {@link Deprecated @Deprecated} в качестве истории
     * @param param <i>Измменяемое значение в цикле for</i>
     * @param op <i>Операция</i>
     * @param param2 <i>Неизменяемое значение в цикле for</i>
     * @return <b>true</b> если неравенство верно <br><b>false</b> если неравенство неверно
     */
    @Deprecated
    private static boolean logic(int param, String op, int param2) {
        if(op.equals(">=")) {
            if(param >= param2) return true;
            return false;
        } else if(op.equals("<=")) {
            if(param <= param2) return true;
            return false;
        }
        return false;
    }
}
