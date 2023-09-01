package com.example.acceuil_recp;

import java.util.List;


public class QueryObject<T> {
    private String url;
    private String xApiKey;
    private List<T> include;
    private List<T> where;
    private List<T> order;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getXApiKey() {
        return xApiKey;
    }

    public void setXApiKey(String xApiKey) {
        this.xApiKey = xApiKey;
    }

    public List<T> getInclude() {
        return include;
    }

    public void setInclude(List<T> include) {
        this.include = include;
    }

    public List<T> getWhere() {
        return where;
    }

    public void setWhere(List<T> where) {
        this.where = where;
    }

    public List<T> getOrder() {
        return order;
    }

    public void setOrder(List<T> order) {
        this.order = order;
    }
}


