package gui;

/**
 * InputEventListener is a functional interface used for performing actions when an InputEvent occurs,
 * while keeping all parts of the Gui decoupled.
 */
public interface InputEventListener {

    /**
     * Performs a desired action when called with an InputEvent.
     *
     * @param event input event
     */
    void onInputEvent(InputEvent event);
}
