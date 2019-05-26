package com.android.example.attendencemanagemnetsystem.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class TeacherModel implements Parcelable {
    private String teacher_id;
    private String teacherName;
    private boolean isSelected = false;

    public TeacherModel(String teacher_id, String teacherName) {
        this.teacher_id = teacher_id;
        this.teacherName = teacherName;
    }

    protected TeacherModel(Parcel in) {
        teacher_id = in.readString();
        teacherName = in.readString();
        isSelected = in.readByte() != 0;
    }

    public String getTeacher_id() {
        return teacher_id;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    public String getTeacherName() {
        return teacherName;
    }


    public static final Creator<TeacherModel> CREATOR = new Creator<TeacherModel>() {
        @Override
        public TeacherModel createFromParcel(Parcel in) {
            return new TeacherModel(in);
        }

        @Override
        public TeacherModel[] newArray(int size) {
            return new TeacherModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(teacher_id);
        dest.writeString(teacherName);
        dest.writeByte((byte) (isSelected ? 1 : 0));
    }
}
