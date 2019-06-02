package com.android.example.attendencemanagemnetsystem.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class ClassModel implements Parcelable {
    private String classId;
    private String title;
    private String session;

    public ClassModel(String classId, String title, String session) {
        this.classId = classId;
        this.title = title;
        this.session = session;
    }

    protected ClassModel(Parcel in) {
        classId = in.readString();
        title = in.readString();
        session = in.readString();
    }

    public static final Creator<ClassModel> CREATOR = new Creator<ClassModel>() {
        @Override
        public ClassModel createFromParcel(Parcel in) {
            return new ClassModel(in);
        }

        @Override
        public ClassModel[] newArray(int size) {
            return new ClassModel[size];
        }
    };

    public String getClassId() {
        return classId;
    }

    public String getTitle() {
        return title;
    }

    public String getSession() {
        return session;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(classId);
        dest.writeString(title);
        dest.writeString(session);
    }
}
