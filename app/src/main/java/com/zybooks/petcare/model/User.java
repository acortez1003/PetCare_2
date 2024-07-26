package com.zybooks.petcare.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = false)
    @NonNull
    @ColumnInfo(name = "micro_id")
    private String mMicroId;

    @NonNull
    @ColumnInfo(name = "name")
    private String mName;

    @NonNull
    @ColumnInfo(name = "gender")
    private String mGender;

    @NonNull
    @ColumnInfo(name = "email")
    private String mEmail;

    @NonNull
    @ColumnInfo(name = "access")
    private String mAccess;

    @NonNull
    @ColumnInfo(name = "breed")
    private String mBreed;

    @ColumnInfo(name = "neutered")
    private boolean mNeutered;

    public User(@NonNull String microId, @NonNull String name, @NonNull String gender, @NonNull String email,
                @NonNull String access, @NonNull String breed, boolean neutered) {
        this.mMicroId = microId;
        this.mName = name;
        this.mGender = gender;
        this.mEmail = email;
        this.mAccess = access;
        this.mBreed = breed;
        this.mNeutered = neutered;
    }

    @NonNull
    public String getMicroId() {
        return mMicroId;
    }
    public void setMicroId(@NonNull String microId) {
        this.mMicroId = microId;
    }

    public String getName() {
        return mName;
    }
    public void setName(@NonNull String name) {
        this.mName = name;
    }

    public String getGender() {
        return mGender;
    }
    public void setGender(@NonNull String gender) {
        this.mGender = gender;
    }

    @NonNull
    public String getEmail() {
        return mEmail;
    }
    public void setEmail(@NonNull String email) {
        this.mEmail = email;
    }

    @NonNull
    public String getAccess() {
        return mAccess;
    }
    public void setAccess(@NonNull String access) {
        this.mAccess = access;
    }

    @NonNull
    public String getBreed() {
        return mBreed;
    }
    public void setBreed(@NonNull String breed) {
        this.mBreed = breed;
    }

    public boolean isNeutered() {
        return mNeutered;
    }
    public void setNeutered(boolean neutered) {
        this.mNeutered = neutered;
    }
}
