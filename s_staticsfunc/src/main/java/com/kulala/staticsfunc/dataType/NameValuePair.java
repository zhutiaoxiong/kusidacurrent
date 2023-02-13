package com.kulala.staticsfunc.dataType;

public class NameValuePair {
    private String name;
    private int id;
    private String value;

    public NameValuePair(String name,String value){
        this.name = name;
        this.value = value;
    }
    public NameValuePair(int id,String value){
        this.id = id;
        this.value = value;
    }

    public String getName() {
        return name;
    }
    public int getID() {
        return id;
    }
    public String getValue() {
        return value;
    }
}
