package com.example.ivan.examapp;

public class Ticket {
    private String name;
    private int id;
    private int size;
    private int questnum;

    public Ticket(String name, int id, int size) {
        this.name = name;
        this.id = id;
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public String getName() {
        return name;
    }

    public int getQuestnum() {
        return questnum;
    }

    public int getId() {
        return id;
    }
}
