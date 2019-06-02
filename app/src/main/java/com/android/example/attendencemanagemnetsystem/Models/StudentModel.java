package com.android.example.attendencemanagemnetsystem.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class StudentModel implements Parcelable {
    private String studentId;
    private String name;
    private String rollNumber;
    private boolean isSelected=false;




    public StudentModel(String studentId, String name, String rollNumber) {
        this.studentId = studentId;
        this.name = name;
        this.rollNumber = rollNumber;
    }

    protected StudentModel(Parcel in) {
        studentId = in.readString();
        name = in.readString();
        rollNumber = in.readString();
        isSelected = in.readByte() != 0;
    }

    public static final Creator<StudentModel> CREATOR = new Creator<StudentModel>() {
        @Override
        public StudentModel createFromParcel(Parcel in) {
            return new StudentModel(in);
        }

        @Override
        public StudentModel[] newArray(int size) {
            return new StudentModel[size];
        }
    };

    public String getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(studentId);
        dest.writeString(name);
        dest.writeString(rollNumber);
        dest.writeByte((byte) (isSelected ? 1 : 0));
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
