package com.example.daomy.uar2.model;

public class School {
    private int id;
    private String school_name;

    public School() {
    }

    public School(int id, String school_name) {
        this.id = id;
        this.school_name = school_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSchool_name() {
        return school_name;
    }

    public void setSchool_name(String school_name) {
        this.school_name = school_name;
    }
}
