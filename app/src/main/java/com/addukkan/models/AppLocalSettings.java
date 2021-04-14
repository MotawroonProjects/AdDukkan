package com.addukkan.models;

import java.io.Serializable;

public class AppLocalSettings implements Serializable {
    private boolean isLanguageSelected = false;

    public boolean isLanguageSelected() {
        return isLanguageSelected;
    }

    public void setLanguageSelected(boolean languageSelected) {
        isLanguageSelected = languageSelected;
    }
}
