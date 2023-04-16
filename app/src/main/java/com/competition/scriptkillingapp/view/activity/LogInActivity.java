package com.competition.scriptkillingapp.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.competition.scriptkillingapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogInActivity extends AppCompatActivity {

    private static final String TAG = "LogInCheck";

    //声明控件
    private Button mBtnLogin, mBtnRegister;
    private EditText mEdtTextEmail, mEdtTextPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //设置顶部框透明
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        //找到控件
        mBtnLogin = findViewById(R.id.btnLogin);
        mEdtTextEmail = findViewById(R.id.login_edtTextEmail);
        mEdtTextPassword = findViewById(R.id.login_edtTextPassword);
        mBtnRegister = findViewById(R.id.btnRegister);
        mAuth = FirebaseAuth.getInstance();

        // Initialize Firebase Auth
        setListener();
    }

    private void setListener() {
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //需要获取输入的用户名和密码
                String email = mEdtTextEmail.getText().toString();
                String password = mEdtTextPassword.getText().toString();


                //弹出的内容设置
                String ok = "登录成功！";
                String fail = "邮箱或者密码有误，请重新输入！";
                String warning = "邮箱或者密码不能为空！";

                if (!email.equals("") && !password.equals("")) {
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(LogInActivity.this, ok, Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "登录成功\nUID: " + mAuth.getCurrentUser().getUid());
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(LogInActivity.this, fail, Toast.LENGTH_SHORT).show();
                                Log.d(TAG, fail);
                            }
                        }
                    });
                } else {
                    // 邮箱或密码为空，登录失败
                    Toast.makeText(getApplicationContext(), warning, Toast.LENGTH_SHORT).show();
                }
            }
        });
        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}