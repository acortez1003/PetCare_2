package com.zybooks.petcare.repo;

import androidx.room.*;
import com.zybooks.petcare.model.User;
import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM users WHERE micro_id = :id")
    User getUser(String id);

    @Query("SELECT * FROM users ORDER BY micro_id")
    List<User> getUsers();

    @Insert
    void addUser(User user);

    @Query("SELECT COUNT(*) FROM users WHERE micro_id = :id")
    int checkIfUserExists(String id);
}
