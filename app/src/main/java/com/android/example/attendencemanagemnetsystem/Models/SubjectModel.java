package com.android.example.attendencemanagemnetsystem.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class SubjectModel implements Parcelable {
    private String name, id;

    public SubjectModel(String name, String id) {
        this.name = name;
        this.id = id;
    }

    protected SubjectModel(Parcel in) {
        name = in.readString();
        id = in.readString();
    }

    public static final Creator<SubjectModel> CREATOR = new Creator<SubjectModel>() {
        @Override
        public SubjectModel createFromParcel(Parcel in) {
            return new SubjectModel(in);
        }

        @Override
        public SubjectModel[] newArray(int size) {
            return new SubjectModel[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(id);
    }
}
