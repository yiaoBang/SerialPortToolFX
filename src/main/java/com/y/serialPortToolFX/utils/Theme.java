package com.y.serialPortToolFX.utils;

import lombok.Getter;

@Getter
public enum Theme {
    CUPERTINO_DARK("cupertino-dark"),
    CUPERTINO_LIGHT("cupertino-light"),
    DRACULA("dracula"),
    NORD_DARK("nord-dark"),
    NORD_LIGHT("nord-light"),
    PRIMER_DARK("primer-dark"),
    PRIMER_LIGHT("primer-light");

    private final String css;

    Theme(String css) {
        this.css = "/assets/theme/" + css + ".css";
    }
}
