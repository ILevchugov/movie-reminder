package io.levchugov.petproject.handler.strategy;

public enum CallbackData {
    ADD_MOVIE("add_movie"),
    ROLL_MOVIE("roll_movie"),
    WATCHED_MOVIE("watched_movie"),
    CANCEL("cancel"),
    CONFIRM_MOVIE("confirm_movie"),
    SHOW_NEXT_MOVIE("show_next_movie"),
    DEFAULT_CALLBACK("default_callback");

    private final String explanation;

    CallbackData(String explanation) {
        this.explanation = explanation;
    }

    public String getExplanation() {
        return explanation;
    }
}
