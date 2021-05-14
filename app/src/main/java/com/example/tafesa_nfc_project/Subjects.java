package com.example.tafesa_nfc_project;

public class Subjects {
    private String subjectName, date, hours, description;
    private boolean expandable;

    public boolean isExpandable() {
        return expandable;
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }

    public Subjects(String subjectName, String date, String hours, String description) {
        this.subjectName = subjectName;
        this.date = date;
        this.hours = hours;
        this.description = description;
        this.expandable = false;

    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Subjects{" +
                "subjectName='" + subjectName + '\'' +
                ", date='" + date + '\'' +
                ", hours='" + hours + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

}

