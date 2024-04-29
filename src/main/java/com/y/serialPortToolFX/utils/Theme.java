package com.y.serialPortToolFX.utils;

public enum Theme {

    CUPERTINO_LIGHT("cupertino-light"), CUPERTINO_DARK("cupertino-dark"),
    //    DRACULA("dracula"),NORD_DARK("nord-dark"),NORD_LIGHT("nord-light"),
    //PRIMER_LIGHT("primer-light"), PRIMER_DARK("primer-dark")
    ;
    public static int index = 0;
    public final String css;

    Theme(String css) {
        this.css = "/assets/theme/" + css + ".css";
    }

    private static final int valuesSize = values().length;

    public static String next() {
        return Theme.values()[index++ % valuesSize].css;
    }
}
