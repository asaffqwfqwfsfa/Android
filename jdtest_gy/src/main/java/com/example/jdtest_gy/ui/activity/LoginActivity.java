package com.example.jdtest_gy.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jdtest_gy.R;
import com.example.jdtest_gy.bean.UserBean;
import com.example.jdtest_gy.utils.Api;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText et_mobile,et_pwd;
    private Button bt_login,bt_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        et_mobile = findViewById(R.id.et_mobile);
        et_pwd=findViewById(R.id.et_pwd);
        bt_login = findViewById(R.id.bt_login);
        bt_register=findViewById(R.id.bt_register);
        bt_login.setOnClickListener(this);
        bt_register.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            default:
                break;
            case R.id.bt_login:
                String mobile = et_mobile.getText().toString();
                String password = et_pwd.getText().toString();

                boolean b = isPhoneNumber(mobile);

                if (mobile.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "用户名/密码不能为空", Toast.LENGTH_SHORT).show();
                } else if (!b) {
                    Toast.makeText(LoginActivity.this, "手机号不合法", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 6) {
                    Toast.makeText(LoginActivity.this, "密码不能少于六位数", Toast.LENGTH_SHORT).show();
                } else {

                    Login(mobile, password);
                }
                break;
            case R.id.bt_register:
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }
    private boolean isPhoneNumber(String phoneStr) {
        String regex = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(phoneStr);
        return m.find();
    }
    private void Login(String mobile, String password) {

        RequestParams params = new RequestParams(Api.LOGIN_URL);
        params.addQueryStringParameter("mobile", mobile);
        params.addQueryStringParameter("password", password);
        x.http().get(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                UserBean bean = gson.fromJson(result, UserBean.class);
                Toast.makeText(LoginActivity.this, bean.getMsg(), Toast.LENGTH_SHORT).show();
                if (bean.getCode().equals("0")) {
                    Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);
                    startActivity(intent);
                }
            }


            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

            @Override
            public boolean onCache(String result) {
                return false;
            }
        });

    }
}
