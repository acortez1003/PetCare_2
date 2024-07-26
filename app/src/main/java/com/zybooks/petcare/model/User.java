package com.zybooks.petcare.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "micro_id")
    private String mMicroId;

    @NonNull
    @ColumnInfo(name = "name")
    private String mName;

    @NonNull
    @ColumnInfo(name = "gender")
    private int mGender;

    @NonNull
    @ColumnInfo(name = "email")
    private String mEmail;

    @NonNull
    @ColumnInfo(name = "access")
    private String mAccess;

    @NonNull
    @ColumnInfo(name = "breed")
    private String mBreed;

    @NonNull
    @ColumnInfo(name = "neutered")
    private boolean mNeutered;

    public User(@NonNull String mMicroId, @NonNull String mName, int mGender, @NonNull String mEmail,
                @NonNull String mAccess, @NonNull String mBreed, boolean mNeutered) {
        this.mMicroId = mMicroId;
        this.mName = mName;
        this.mGender = mGender;
        this.mEmail = mEmail;
        this.mAccess = mAccess;
        this.mBreed = mBreed;
        this.mNeutered = mNeutered;
    }

    public

}
