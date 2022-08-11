package mc.game.events;

/**
 * <b>Интерфейс, содержащий поведение событий</b>
 *
 * @author <b>pleer__gr45</b>
 */
public interface BowWarsEvent {

    /**
     * <b>Метод срабатывания события</b>
     * <br>Метод будет выполнен, когда произойдёт событие, реализующее данный интерфейс
     * <br>Используется в методе {@link EventLoader#update(int)}
     * @see EventLoader.Events Events
     */
    void whenTriggered();
}
