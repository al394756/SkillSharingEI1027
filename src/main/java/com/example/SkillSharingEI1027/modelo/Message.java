package com.example.SkillSharingEI1027.modelo;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

public class Message {
    private String idChat;
    private String student;
    private int number;
    private String content;
    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE)
    private LocalDate date;

    public Message() {
    }

    public String getIdChat() {
        return idChat;
    }

    public String getStudent() {
        return student;
    }

    public int getNumber() {
        return number;
    }

    public String getContent() {
        return content;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setIdChat(String idChat) {
        this.idChat = idChat;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "Message{" +
                "idChat='" + idChat + '\'' +
                ", student='" + student + '\'' +
                ", number=" + number +
                ", content='" + content + '\'' +
                ", date=" + date +
                '}';
    }
}
