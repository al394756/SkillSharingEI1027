package com.example.SkillSharingEI1027.modelo;

import java.util.ArrayList;
import java.util.List;

public class Chat {
    private String idChat;
    private String user1;
    private String user2;
    private List<Message> log;
    private boolean newMsg;

    public Chat() {
        log=new ArrayList<>();
        newMsg = true;
    }

    public boolean isNewMsg() {
        return newMsg;
    }

    public void setNewMsg(boolean newMsg) {
        this.newMsg = newMsg;
    }

    public String getIdChat() {
        return idChat;
    }

    public String getUser1() {
        return user1;
    }

    public String getUser2() {
        return user2;
    }

    public List<Message> getLog() {
        return log;
    }

    public void setIdChat(String idChat) {
        this.idChat = idChat;
    }

    public void setUser1(String user1) {
        this.user1 = user1;
    }

    public void setUser2(String user2) {
        this.user2 = user2;
    }

    public void setLog(List<Message> log) {
        this.log = log;
    }

}
