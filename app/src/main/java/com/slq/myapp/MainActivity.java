package com.slq.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.slq.myapp.activity.BaseActivity;
import com.slq.myapp.activity.HomeActivity;
import com.slq.myapp.activity.LoginActivity;
import com.slq.myapp.activity.RegisterActivity;

import android.content.Intent;
import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.btn_register)
    Button btnRegister;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        String token = getStringFromSp("token");
        if(token!=null){
            navigateTo(HomeActivity.class);
            finish();
        }
    }

    @OnClick({R.id.btn_login,R.id.btn_register})
    void finishA(View view) {
        int id = view.getId();
        switch (id){
            case R.id.btn_login:
                navigateTo(LoginActivity.class);
                break;
            case R.id.btn_register:
                navigateTo(RegisterActivity.class);
                break;
        }
    }
}