package com.example.SkillSharingEI1027.modelo;

import java.util.ArrayList;
import java.util.List;

public class Chat {
    private String idChat;
    private String user1;
    private String user2;
    private List<Message> log;
    private boolean newMsgParaStudent1;
    private boolean newMsgParaStudent2;

    public Chat() {
        log=new ArrayList<>();
        newMsgParaStudent1=false;
        newMsgParaStudent2=false;
    }

    public boolean isNewMsgParaStudent1() {
        return newMsgParaStudent1;
    }

    public void setNewMsgParaStudent1(boolean newMsgParaStudent1) {
        this.newMsgParaStudent1 = newMsgParaStudent1;
    }

    public boolean isNewMsgParaStudent2() {
        return newMsgParaStudent2;
    }

    public void setNewMsgParaStudent2(boolean newMsgParaStudent2) {
        this.newMsgParaStudent2 = newMsgParaStudent2;
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
