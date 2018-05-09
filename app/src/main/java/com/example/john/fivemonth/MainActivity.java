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

public class MainActivity extends AppCompatActivity {

    private EditText et_name,et_pwd;
    private Button login,register;
    private String loginPath="http://120.27.23.105/user/login";
//    private String registerPath="http://120.27.23.105/user/reg";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_name = findViewById(R.id.et_name);
        et_pwd=findViewById(R.id.et_pwd);
        login = findViewById(R.id.bt_login);
        register=findViewById(R.id.bt_register);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = et_name.getText().toString();
                String pwd = et_pwd.getText().toString();
                boolean phoneNum = isPhoneNum(name);
                if (name.isEmpty() || pwd.isEmpty()) {
                    Toast.makeText(MainActivity.this, "用户名/密码不能为空", Toast.LENGTH_SHORT).show();
                } else if (!phoneNum) {
                    Toast.makeText(MainActivity.this, "手机号不合法", Toast.LENGTH_SHORT).show();
                } else if (pwd.length() < 6) {
                    Toast.makeText(MainActivity.this, "密码不能少于六位数", Toast.LENGTH_SHORT).show();
                } else {

                    Login(name, pwd);
                }
        }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
Intent intent=new Intent(MainActivity.this,Main2Activity.class);
startActivity(intent);
            }
        });
    }
    private boolean isPhoneNum(String name){
        String num = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
        Pattern pattern = Pattern.compile(num);
        Matcher matcher = pattern.matcher(name);
        return matcher.find();
    }
    private void Login(String mobile,String pwd){
        RequestParams params = new RequestParams(loginPath);
        params.addQueryStringParameter("mobile",mobile);
        params.addQueryStringParameter("password",pwd);
        x.http().get(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson=new Gson();
                UserBean bean = gson.fromJson(result, UserBean.class);
                Toast.makeText(MainActivity.this,bean.getMsg(),Toast.LENGTH_SHORT).show();
                if(bean.getData().equals("0")){
                    Intent intent=new Intent(MainActivity.this,Main3Activity.class);
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
