package com.slq.myapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.slq.myapp.R;
import com.slq.myapp.api.ApiConfig;
import com.slq.myapp.entity.LoginResponse;
import com.slq.myapp.api.Api;
import com.slq.myapp.util.MyappStringUtils;

import java.io.IOException;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lombok.SneakyThrows;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginActivity extends BaseActivity {
    @BindView(R.id.login_form_button)
    Button loginBtn;
    @BindView(R.id.et_login_account)
    EditText etAccount;
    @BindView(R.id.et_login_pw)
    EditText etPwd;
    String TAG = "tttt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        String d;
        if (savedInstanceState != null) {
            d = savedInstanceState.getString("d");
            Log.e(TAG, "onCreate: "+d );
        }
        int a=1;
        getTaskId();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("d","ddd");
    }

    @OnClick( R.id.login_form_button)
    void login() {
        String account = etAccount.getText().toString().trim();
        String pwd = etPwd.getText().toString().trim();
        login(account, pwd);
        //loginSuccess();
    }

    private void loginSuccess(){
        navigateTo(HomeActivity.class);
    }
    private void login(String account, String pwd) {
        if (MyappStringUtils.isEmpty(account)) {
            showToast("请输入账号");
        } else if (MyappStringUtils.isEmpty(pwd)) {
            showToast("请输入密码");
        } else {
            HashMap map = new HashMap();
            map.put("mobile", account);
            map.put("password", pwd);
            String url = ApiConfig.LOGIN;
            Callback cb = new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(TAG, "login onFailure: " + e.getMessage());
                    showToastAsny(e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String s = response.body().string();
                    //showToastAsny(s);
                    Gson gson=new Gson();
                    LoginResponse loginResponse = gson.fromJson(s, LoginResponse.class);
                    if(0==loginResponse.getCode()){
                        saveString2Sp("token",loginResponse.getToken());
                        runOnUiThread(new Runnable() {
                            @SneakyThrows
                            @Override
                            public void run() {
                                Thread.sleep(1000);
                                //int flag1= 1|4;       //5
                                //int flag2= 1|3;       //3
                                int flag= Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK;
                                navigateToWithFlag(HomeActivity.class,flag);
                            }
                        });
                        showToastAsny("登录成功");
                    }else{
                        showToastAsny("登录失败");
                    }
                }
            };
            Api.postRequest(url,map,cb);
            }
        }
    }