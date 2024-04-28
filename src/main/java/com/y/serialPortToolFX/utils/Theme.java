package com.y.serialPortToolFX.utils;

public enum Theme {
    CUPERTINO_DARK("cupertino-dark"),
    CUPERTINO_LIGHT("cupertino-light"),
    DRACULA("dracula"),
    NORD_DARK("nord-dark"),
    NORD_LIGHT("nord-light"),
    PRIMER_DARK("primer-dark"),
    PRIMER_LIGHT("primer-light");
    public static int index = 0;
    public final String css;

    Theme(String css) {
        this.css = "/assets/theme/" + css + ".css";
    }

    public static Theme getNext() {
        return Theme.values()[index++ % 7];
    }
}
