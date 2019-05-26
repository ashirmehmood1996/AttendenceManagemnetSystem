package com.android.example.attendencemanagemnetsystem.Models;

public class StudentModel {
    private String studentId;
    private String name;
    private String rollNumber;
    //// TODO: 5/26/2019 add more fields later


    public StudentModel(String studentId, String name, String rollNumber) {
        this.studentId = studentId;
        this.name = name;
        this.rollNumber = rollNumber;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    public String getRollNumber() {
        return rollNumber;
    }
}
