package com.chat.cociocompany.firebasechattest;

public class ChatDataProvider {
    public String message;
    public String name;

    public ChatDataProvider(String message, String name){
        super();
        this.setMessage(message);
        this.setName(name);
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage(){
        return message;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
