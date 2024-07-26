package com.zybooks.petcare.repo;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.zybooks.petcare.model.User;

@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase{
    public abstract UserDao userDao();
}
