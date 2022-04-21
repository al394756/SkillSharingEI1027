package com.example.SkillSharingEI1027.modelo;

public class Sorter {

    private boolean fromTopToBot;
    private String type;
    private String texto=null;

    public boolean isFromTopToBot() {
        return fromTopToBot;
    }

    public String getType() {
        return type;
    }

    public void setFromTopToBot(boolean fromTopToBot) {
        this.fromTopToBot = fromTopToBot;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.fromTopToBot = texto.equals("TopToBot");

    }

    public Sorter() {
        this.fromTopToBot = false;
        this.type="idStudent";
    }

    @Override
    public String toString() {
        return "Sorter{" +
                "fromTopToBot=" + fromTopToBot +
                ", type='" + type + '\'' +
                ", texto='" + texto + '\'' +
                '}';
    }
}

