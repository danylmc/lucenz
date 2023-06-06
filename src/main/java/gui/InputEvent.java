package gui;

/**
 * InputEvent describes any type of user input within the Gui that requires immediate
 * interaction with the domain.
 */
public enum InputEvent {
    /**
     * GO button was pressed.
     */
    GO_PRESSED,
    /**
     * Clear button was pressed.
     */
    CLEAR_PRESSED,
    /**
     * Load button was pressed.
     */
    LOAD_PRESSED,
    /**
     * Save button was pressed.
     */
    SAVE_PRESSED,
    /**
     * GraphType was changed.
     */
    GRAPH_TYPE_CHANGED,
    /**
     * The currently selected model has been changed.
     */
    MODEL_CHANGED,
    /**
     * The name the user has given the substrate changed.
     */
    SUBSTRATE_NAME_CHANGED,
    /**
     * The clicked points on the graph have changed.
     */
    CLICKED_POINTS_CHANGED,
    /**
     * Transpose button was pressed.
     */
    TRANSPOSE_PRESSED,
    /**
     * The version has been changed to LUCENZ 2 (200 level).
     */
    LUCENZ_2_SELECTED,
    /**
     * The version has been changed to LUCENZ 3 (300 level).
     */
    LUCENZ_3_SELECTED,
}
