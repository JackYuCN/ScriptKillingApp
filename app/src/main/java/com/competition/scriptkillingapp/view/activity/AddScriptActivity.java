package com.competition.scriptkillingapp.view.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.competition.scriptkillingapp.R;
import com.competition.scriptkillingapp.model.RoomSetting;
import com.competition.scriptkillingapp.model.Script;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class AddScriptActivity extends AppCompatActivity {
    final String TAG_CALENDAR = "CalendarCheck";
    final String TAG_ROLL = "RollCheck";
    final String TAG_USER = "UserCheck";
    final String URL = "https://scriptkillingapp-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private TextView mTxtTimeHint;
    private EditText mEdtTextPassword;
    private Button mBtnConfirm;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_script);

        mTxtTimeHint = findViewById(R.id.add_script_txtTimeHint);
        mEdtTextPassword = findViewById(R.id.add_script_edtTextPassword);
        mBtnConfirm = findViewById(R.id.add_script_btnConfirm);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        assert mCurrentUser != null;
        mDatabaseRef = FirebaseDatabase.getInstance(URL).getReference();
        assert mDatabaseRef != null;

        initWindow();
        initListener();
    }

    private void initListener() {
        initBottomSheetListener();
        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBtnConfirm.getText().toString().equals("")) {
                    Toast.makeText(AddScriptActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                } else if (mTxtTimeHint.getText().toString().equals("")) {
                    Toast.makeText(AddScriptActivity.this, "请选择时间", Toast.LENGTH_SHORT).show();
                } else {

                    // 这里直接给出剧本定义，实际实现中应该从数据库读出
                    Script script = new Script("测试剧本", 3, 3);

                    RoomSetting roomSetting = new RoomSetting(
                            mEdtTextPassword.getText().toString(),
                            mTxtTimeHint.getText().toString(),
                            mCurrentUser.getUid()
                    );
                    DatabaseReference ref;
                    if (mTxtTimeHint.getText().toString().trim().equals("即开房")) {
                        ref = mDatabaseRef.child("rooms").child("right_now").push();
                        ref.child("settings").setValue(roomSetting);
                        ref.child("script").setValue(script);
                    } else {
                        ref = mDatabaseRef.child("rooms").child("booking").push();
                        ref.child("settings").setValue(roomSetting);
                        ref.child("script").setValue(script);
                    }
                    Toast.makeText(AddScriptActivity.this, "成功预约", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    private void initWindow() {
        //设置顶部框透明
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    private void initBottomSheetListener() {
        mTxtTimeHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        AddScriptActivity.this, R.style.BottomSheetDialogTheme
                );
                View bottomSheetView = LayoutInflater.from(getApplicationContext())
                        .inflate(
                                R.layout.layout_bottom_time_picker,
                                findViewById(R.id.bottomSheetContainer)
                        );
                bottomSheetDialog.setContentView(bottomSheetView);


                TextView txtConfirm = bottomSheetDialog.findViewById(R.id.txtConfirm);
                TextView txtBack = bottomSheetDialog.findViewById(R.id.txtBack);
                NumberPicker pTime = bottomSheetDialog.findViewById(R.id.pickerTime);
                NumberPicker pType = bottomSheetDialog.findViewById(R.id.pickerType);
                NumberPicker pDate = bottomSheetDialog.findViewById(R.id.pickerDate);

                // 设置为不可编辑状态
                pTime.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
                pType.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
                pDate.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
                // 取消循环滚动
                pTime.setWrapSelectorWheel(false);
                pType.setWrapSelectorWheel(false);
                pDate.setWrapSelectorWheel(false);

                // 准备各种NumberPicker
                Calendar calendar = Calendar.getInstance();
                int month = calendar.get(Calendar.MONTH) + 1;
                int date = calendar.get(Calendar.DATE);
                int max_date = calendar.getActualMaximum(Calendar.DATE);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                Log.d(TAG_CALENDAR, "month: " + month + " date: " + date +
                        " max date: " + max_date + " current hour: " + hour);
                String[] DateArray = new String[7];
                for (int i = 0; i < DateArray.length; i++) {
                    DateArray[i] = month + "." + date;
                    date = (date + 1) % max_date;
                    if (date == 0) {
                        month = (month + 1) % 12;
                    }
                }
                String[] NullArray = {"您已选择即开"};
                String[] TypeArray = {"即开", "上午场", "下午场", "晚场", "修仙场"};
                String[] NullTimeArray = {"当前时段不可选"};
                int morningStart = 6;
                int afternoonStart = 12;
                int nightStart = 18;
                int midnightStart = 0;
                String[] TimeMorningArray = new String[12];
                String[] TimeAfternoonArray = new String[12];
                String[] TimeNightArray = new String[12];
                String[] TimeMidNightArray = new String[12];
                for (int i = 0; i < TimeMidNightArray.length; i++) {
                    String min;
                    if (i % 2 == 0)
                        min = "00";
                    else
                        min = "30";
                    TimeMorningArray[i] = (morningStart + i / 2) + ":" + min;
                    TimeAfternoonArray[i] = (afternoonStart + i / 2) + ":" + min;
                    TimeNightArray[i] = (nightStart + i / 2) + ":" + min;
                    TimeMidNightArray[i] = (midnightStart + i / 2) + ":" + min;
                }

                // date初始值为今天，type初始值为上午场，time初始值为相应时间
                pDate.setMinValue(0);
                pDate.setMaxValue(DateArray.length - 1);
                pDate.setDisplayedValues(DateArray);
                pType.setMinValue(0);
                pType.setMaxValue(TypeArray.length - 1);
                pType.setValue(1);
                pType.setDisplayedValues(TypeArray);
                pTime.setMinValue(0);
                if (hour >= 6) {
                    pTime.setMaxValue(0);
                    pTime.setDisplayedValues(NullTimeArray);
                } else {
                    pTime.setMaxValue(TimeMorningArray.length - 1);
                    pTime.setDisplayedValues(TimeMorningArray);
                }

                txtBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.cancel();
                    }
                });
                txtConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (pType.getValue() == 0) { // 即开
                            ViewGroup.LayoutParams layoutParams = mTxtTimeHint.getLayoutParams();
                            mTxtTimeHint.setText("          即开房");
                        } else if (pTime.getDisplayedValues()[0].equals(NullTimeArray[0])) {
                            Toast.makeText(AddScriptActivity.this, "请选择正确的时间！", Toast.LENGTH_SHORT).show();
                        } else {
                            mTxtTimeHint.setText(
                                    pDate.getDisplayedValues()[pDate.getValue()]
                                            + " " + pType.getDisplayedValues()[pType.getValue()]
                                            + " " + pTime.getDisplayedValues()[pTime.getValue()]
                            );
                        }

                        bottomSheetDialog.cancel();
                    }
                });

                pType.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        switch (newVal) {
                            case 0:
                                // order matters here!
                                pTime.setMaxValue(0);
                                pTime.setDisplayedValues(NullArray);
                                pDate.setMaxValue(0);
                                pDate.setDisplayedValues(NullArray);
                                break;
                            case 1:
                                pDate.setDisplayedValues(DateArray);
                                pDate.setMaxValue(DateArray.length - 1);
                                if (pDate.getValue() != 0 || hour < 6) {
                                    pTime.setDisplayedValues(TimeMorningArray);
                                    pTime.setMaxValue(TimeMorningArray.length - 1);
                                } else {
                                    pTime.setMaxValue(0);
                                    pTime.setDisplayedValues(NullTimeArray);
                                }
                                break;
                            case 2:
                                pDate.setDisplayedValues(DateArray);
                                pDate.setMaxValue(DateArray.length - 1);
                                if (pDate.getValue() != 0 || hour < 12) {
                                    pTime.setDisplayedValues(TimeAfternoonArray);
                                    pTime.setMaxValue(TimeAfternoonArray.length - 1);
                                } else {
                                    pTime.setMaxValue(0);
                                    pTime.setDisplayedValues(NullTimeArray);
                                }
                                break;
                            case 3:
                                pDate.setDisplayedValues(DateArray);
                                pDate.setMaxValue(DateArray.length - 1);
                                if (pDate.getValue() != 0 || hour < 18) {
                                    pTime.setDisplayedValues(TimeNightArray);
                                    pTime.setMaxValue(TimeNightArray.length - 1);
                                } else {
                                    pTime.setMaxValue(0);
                                    pTime.setDisplayedValues(NullTimeArray);
                                }
                                break;
                            case 4:
                                pDate.setDisplayedValues(DateArray);
                                pDate.setMaxValue(DateArray.length - 1);
                                if (pDate.getValue() != 0) {
                                    pTime.setDisplayedValues(TimeMidNightArray);
                                    pTime.setMaxValue(TimeMidNightArray.length - 1);
                                } else {
                                    pTime.setMaxValue(0);
                                    pTime.setDisplayedValues(NullTimeArray);
                                }
                                break;
                            default: // never arrive here!
                                assert false;
                                break;
                        }
                    }
                });
                pDate.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        if (newVal == 0 || oldVal == 0) {
                            switch (pType.getValue()) {
                                case 0: // 即开
                                    break;
                                case 1: // 上午场
                                    if (oldVal == 0 || hour < 6) {
                                        pTime.setDisplayedValues(TimeMorningArray);
                                        pTime.setMaxValue(TimeMorningArray.length - 1);
                                    } else {
                                        pTime.setMaxValue(0);
                                        pTime.setDisplayedValues(NullTimeArray);
                                    }
                                    break;
                                case 2: // 下午场
                                    if (oldVal == 0 || hour < 12) {
                                        pTime.setDisplayedValues(TimeAfternoonArray);
                                        pTime.setMaxValue(TimeAfternoonArray.length - 1);
                                    } else {
                                        pTime.setMaxValue(0);
                                        pTime.setDisplayedValues(NullTimeArray);
                                    }
                                    break;
                                case 3: // 晚场
                                    if (oldVal == 0 || hour < 18) {
                                        pTime.setDisplayedValues(TimeNightArray);
                                        pTime.setMaxValue(TimeNightArray.length - 1);
                                    } else {
                                        pTime.setMaxValue(0);
                                        pTime.setDisplayedValues(NullTimeArray);
                                    }
                                    break;
                                case 4: // 修仙场
                                    if (oldVal == 0) {
                                        pTime.setDisplayedValues(TimeMidNightArray);
                                        pTime.setMaxValue(TimeMidNightArray.length - 1);
                                    } else {
                                        pTime.setMaxValue(0);
                                        pTime.setDisplayedValues(NullTimeArray);
                                    }
                                    break;
                                default: // never arrive here!
                                    assert false;
                                    break;
                            }
                        }
                    }
                });

                bottomSheetDialog.show();
            }
        });
    }
}
