package com.slq.myapp.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.slq.myapp.R;
import com.slq.myapp.api.ApiConfig;
import com.slq.myapp.api.Api;
import com.slq.myapp.util.MyappStringUtils;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class RegisterActivity extends BaseActivity {
    @BindView(R.id.register_form_button)
    Button registerBtn;
    @BindView(R.id.et_register_account)
    EditText etAccount;
    @BindView(R.id.et_register_pw)
    EditText etPwd;
    String TAG = "tttt";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.i1:
                showToast("todo i1");
                break;
            case R.id.i2:
                showToast("what i2");
                break;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.register_form_button)
    void register() {
        String account = etAccount.getText().toString().trim();
        String pwd = etPwd.getText().toString().trim();
        register(account, pwd);
    }

    private void register(String account, String pwd) {
        if (MyappStringUtils.isEmpty(account)) {
            showToast("请输入账号");
        } else if (MyappStringUtils.isEmpty(pwd)) {
            showToast("请输入密码");
        } else {
            HashMap map = new HashMap();
            map.put("mobile", account);
            map.put("password", pwd);
            String url = ApiConfig.REGISTER;
            Callback cb = new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(TAG, "register onFailure: " + e.getMessage());
                    showToastAsny(e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String s = response.body().string();
                    Log.e(TAG, "register onResponse: " + s);
                    showToastAsny(s);
                }
            };
            Api.postRequest(url, map, cb);
        }
    }
}