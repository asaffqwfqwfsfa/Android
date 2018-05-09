package com.example.jdtest_gy.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jdtest_gy.R;
import com.example.jdtest_gy.bean.RegisterBean;
import com.example.jdtest_gy.utils.Api;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText et2_mobile,et2_pwd;
    private Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        et2_mobile = findViewById(R.id.et2_mobile);
        et2_pwd=findViewById(R.id.et2_pwd);
        register = findViewById(R.id.register);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.register:
                String mobile = et2_mobile.getText().toString().trim();
                String password = et2_pwd.getText().toString().trim();

                boolean b = isPhoneNumber(mobile);
                if (mobile.isEmpty() || password.isEmpty()) {
                    Toast.makeText(this, "用户名/密码不能为空", Toast.LENGTH_SHORT).show();
                } else if (!b) {
                    Toast.makeText(this, "手机号不合法", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 6) {
                    Toast.makeText(this, "密码不能少于六位数", Toast.LENGTH_SHORT).show();
                } else {
                    Register(mobile, password);
                }

                break;
        }
    }
    private boolean isPhoneNumber(String phoneStr) {
        String regex = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(phoneStr);
        return m.find();
    }
    private void Register(String mobile,String password){
        RequestParams params = new RequestParams(Api.REG_URL);
        params.addQueryStringParameter("mobile",mobile);
        params.addQueryStringParameter("password",password);

        x.http().get(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                RegisterBean bean = gson.fromJson(result, RegisterBean.class);
                Toast.makeText(RegisterActivity.this,bean.getMsg(), Toast.LENGTH_SHORT).show();
                if (bean.getCode().equals("0")){
                    finish();
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
