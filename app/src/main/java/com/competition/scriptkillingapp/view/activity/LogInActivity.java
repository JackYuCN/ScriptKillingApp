package com.competition.scriptkillingapp.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.competition.scriptkillingapp.R;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener{

    //声明控件
    private Button mBtnLogin;
    private EditText mEtUser;
    private EditText mEtPassword;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //找到控件
        mBtnLogin = findViewById(R.id.login_btn2);
        mEtUser = findViewById(R.id.login_et1);
        mEtPassword = findViewById(R.id.login_et2);


        mBtnLogin.setOnClickListener(this::onClick);
    }



    public void onClick(View v){
        //需要获取输入的用户名和密码
        String username = mEtUser.getText().toString();
        String password = mEtPassword.getText().toString();
        //弹出的内容设置
        String ok = "登录成功！";
        String fail = "密码或者账号有误，请重新登录！";
        Intent intent = null;

        //假设正确的账号和密码分别是abcdef，1234456
        if(username.equals("a") && password.equals("1")){
            Toast.makeText(getApplicationContext(),ok,Toast.LENGTH_SHORT).show();

            //如果正确的话进行跳转
            intent = new Intent(  LogInActivity.this, MainActivity.class);
            startActivity(intent);
        }else{
            Toast.makeText(getApplicationContext(),fail,Toast.LENGTH_SHORT).show();
        }
    }


}