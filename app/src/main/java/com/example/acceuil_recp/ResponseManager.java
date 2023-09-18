package com.example.acceuil_recp;

public class ResponseManager {
    private static ResponseManager instance;
    private String response;
    private boolean referenceFound = false; // Ajout de la variable referenceFound

    private ResponseManager() {
        // Constructeur privé pour empêcher l'instanciation directe
    }

    public static synchronized ResponseManager getInstance() {
        if (instance == null) {
            instance = new ResponseManager();
        }
        return instance;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public boolean isReferenceFound() {
        return referenceFound;
    }

    public void setReferenceFound(boolean referenceFound) {
        this.referenceFound = referenceFound;
    }
}
