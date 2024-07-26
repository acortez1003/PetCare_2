package com.zybooks.petcare.repo;

import android.content.Context;
import androidx.room.Room;
import com.zybooks.petcare.model.User;
import java.util.List;

public class UserRepository {
    private static UserRepository mUserRepo;
    private final UserDao mUserDao;

    public static UserRepository getInstance(Context context) {
        if(mUserRepo == null) {
            mUserRepo = new UserRepository(context);
        }
        return mUserRepo;
    }

    private UserRepository(Context context) {
        AppDatabase database = Room.databaseBuilder(context, AppDatabase.class, "user.db")
                .allowMainThreadQueries()
                .build();
        mUserDao = database.userDao();
    }

    public boolean addUser(User user) {
        if(mUserDao.checkIfUserExists(user.getMicroId()) > 0) {
            return false;
        } else {
            mUserDao.addUser(user);
            return true;
        }
    }

    public User getUser(String microId) {
        return mUserDao.getUser(microId);
    }

    public List<User> getUsers() {
        return mUserDao.getUsers();
    }
}
