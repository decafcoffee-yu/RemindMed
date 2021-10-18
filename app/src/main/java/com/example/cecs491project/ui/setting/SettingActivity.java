package com.example.cecs491project.ui.setting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.cecs491project.MainActivity;
import com.example.cecs491project.R;
import com.example.cecs491project.ui.login.RegisterActivity;

import java.util.Set;

import io.perfmark.Link;

//TODO: Theme Setting
//TODO: Alarm Music Setting
//TODO: Vibration Setting
//TODO: Notification Setting
//TODO: Upgrade to registered account

//OPTIONAL:
//DISABLE ACCOUNT
//ABOUT USER PAGE
//COPYRIGHT PAGE
public class SettingActivity extends AppCompatActivity {

    Button link;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        link = findViewById(R.id.link_account);

        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent(SettingActivity.this, LinkAccountActivity.class));
                finish();
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar_setting);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("");

    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();

        return true;
    }


}