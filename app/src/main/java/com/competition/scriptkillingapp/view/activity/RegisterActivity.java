package com.competition.scriptkillingapp.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    final String URL = "https://scriptkillingapp-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private String mUserName, mEmail, mPassword;
    private EditText mEdtTextUserName, mEdtTextEmail, mEdtTextPassword;
    private Button mBtnRegister;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEdtTextUserName = findViewById(R.id.register_edtTextUserName);
        mEdtTextEmail = findViewById(R.id.register_edtTextEmail);
        mEdtTextPassword = findViewById(R.id.register_edtTextPassword);
        mBtnRegister = findViewById(R.id.register_btnLogin);

        mAuth = FirebaseAuth.getInstance();

        initWindow();
        initListener();
    }

    private void initWindow() {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    private void initListener() {
        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserName = mEdtTextUserName.getText().toString();
                mEmail = mEdtTextEmail.getText().toString();
                mPassword = mEdtTextPassword.getText().toString();

                if (!mUserName.equals("") && !mEmail.equals("") && !mPassword.equals("")) {
                    mAuth.createUserWithEmailAndPassword(mEmail, mPassword)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser User = mAuth.getCurrentUser();
                                        String Uid = User.getUid();

                                        mRef = FirebaseDatabase.getInstance(URL)
                                                .getReference("Users").child(Uid);

                                        HashMap<String, String> hashMap = new HashMap<>();
                                        hashMap.put("id", Uid);
                                        hashMap.put("username", mUserName);
                                        hashMap.put("imageURL", "default");

                                        mRef.setValue(hashMap)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT).show();
                                                            Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                    }
                                                });
                                    } else {
                                        Toast.makeText(getApplicationContext(), "注册失败", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                } else {
                    Toast.makeText(getApplicationContext(), "请填入完整信息", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
