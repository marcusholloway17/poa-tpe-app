package com.example.acceuil_recp;
public enum MotifEnum {
    consultation("consultation"),
    admission("admission"),
    analyse("analyse"),
    examination("examination"),
    checkup("checkup"),
    autre("autre");

    private final String displayName;

    MotifEnum(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
