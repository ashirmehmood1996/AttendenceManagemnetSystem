package com.android.example.attendencemanagemnetsystem.Models;

public class ClassModel {
    private String classId;
    private String title;
    private String session;

    public ClassModel(String classId, String title, String session) {
        this.classId = classId;
        this.title = title;
        this.session = session;
    }

    public String getClassId() {
        return classId;
    }

    public String getTitle() {
        return title;
    }

    public String getSession() {
        return session;
    }
}
