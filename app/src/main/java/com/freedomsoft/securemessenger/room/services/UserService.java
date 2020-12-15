package com.freedomsoft.securemessenger.room.services;

import androidx.annotation.Nullable;
import androidx.room.Transaction;

import com.freedomsoft.securemessenger.App;
import com.freedomsoft.securemessenger.room.dao.UserRepo;
import com.freedomsoft.securemessenger.room.models.User;

public class UserService {
    private static final UserRepo userRepo = App.appDatabase.userRepo();

    public static void save(User user) {
        userRepo.save(user);
    }


    @Transaction
    public static void setSelected(User user) {
        userRepo.removeSelection();
        userRepo.setSelected(user.getId());
    }

    @Nullable
    public static User getSelected() {
        return userRepo.getSelected();
    }
    public static long count(){
        return userRepo.count();
    }
}
