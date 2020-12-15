package com.freedomsoft.securemessenger.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.freedomsoft.securemessenger.LiveData;
import com.freedomsoft.securemessenger.R;
import com.freedomsoft.securemessenger.Typefaces;
import com.freedomsoft.securemessenger.databinding.ActivityStartPageBinding;
import com.freedomsoft.securemessenger.room.models.User;
import com.freedomsoft.securemessenger.room.services.UserService;


public class StartPageActivity extends AppCompatActivity {
    private ActivityStartPageBinding b;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        User user = UserService.getSelected();
        if (user != null) {
            LiveData.getInstance().userMutableLiveData.setValue(user);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        getWindow().getDecorView().setBackgroundColor(Color.WHITE);
        b = DataBindingUtil.setContentView(this, R.layout.activity_start_page);
        b.existingUserButton.setTypeface(Typefaces.medium);
        b.orTitle.setTypeface(Typefaces.medium);
    }

    public void addNewUser(View view) {
        if (UserService.count() == 0) {
            User user = new User();
            user.setSelected(true);
            UserService.save(user);
            LiveData.getInstance().userMutableLiveData.setValue(user);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
