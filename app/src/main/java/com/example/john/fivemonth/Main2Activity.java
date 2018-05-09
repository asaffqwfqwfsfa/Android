package com.example.john.fivemonth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main2Activity extends AppCompatActivity {

    private EditText register_name,register_pwd;
    private Button register;
  private String registerPath="http://120.27.23.105/user/reg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        register_name = findViewById(R.id.register_name);
        register_pwd=findViewById(R.id.register_pwd);
        register = findViewById(R.id.bt2_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = register_name.getText().toString();
                String pwd = register_pwd.getText().toString();
                boolean phoneNum = isPhoneNum(name);
                if (name.isEmpty() || pwd.isEmpty()) {
                    Toast.makeText(Main2Activity.this, "用户名/密码不能为空", Toast.LENGTH_SHORT).show();
                } else if (!phoneNum) {
                    Toast.makeText(Main2Activity.this, "手机号不合法", Toast.LENGTH_SHORT).show();
                } else if (pwd.length() < 6) {
                    Toast.makeText(Main2Activity.this, "密码不能少于六位数", Toast.LENGTH_SHORT).show();
                } else {
                    Register(name, pwd);
                }


            }
        });
    }
    private boolean isPhoneNum(String name){
        String num = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
        Pattern pattern = Pattern.compile(num);
        Matcher matcher = pattern.matcher(name);
        return matcher.find();
    }
    private void Register(String mobile,String pwd){
        RequestParams params = new RequestParams(registerPath);
        params.addQueryStringParameter("mobile",mobile);
        params.addQueryStringParameter("password",pwd);
        x.http().get(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson=new Gson();
                RegisterBean bean = gson.fromJson(result, RegisterBean.class);
                if (bean.getCode().equals("0")){
                    Intent intent=new Intent(Main2Activity.this,MainActivity.class);
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
