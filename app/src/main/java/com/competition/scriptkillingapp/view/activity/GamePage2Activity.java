package com.competition.scriptkillingapp.view.activity;

        import androidx.appcompat.app.AppCompatActivity;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;

        import android.content.Intent;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.Spinner;
        import android.widget.Toast;

        import com.competition.scriptkillingapp.R;
        import com.competition.scriptkillingapp.adapter.GamePage2Adapter;
        import com.competition.scriptkillingapp.adapter.GameRoomCharacterAdapter;
        import com.competition.scriptkillingapp.model.Script;

        import java.util.ArrayList;


// An highlighted block
public class GamePage2Activity extends AppCompatActivity {

    //声明数组
    private String[] episodeArray = {"第一篇-序章", "第二篇-回忆1", "第一轮搜证", "第三篇-回忆2", "第二轮搜证", "第四篇-现实","第一轮技能卡","第五篇-结尾"};

    private RecyclerView scriptsRecView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamepage2);

        //数组适配器
        ArrayAdapter<String> germsAdapt = new ArrayAdapter<String>(this,
                R.layout.item_select, episodeArray);
        germsAdapt.setDropDownViewResource(R.layout.item_dropdown);

        Spinner sp = findViewById(R.id.gamepage2_dropdown);
        sp.setPrompt("请选择篇章");
        sp.setAdapter(germsAdapt);
        sp.setSelection(0);
        sp.setOnItemSelectedListener(new MySelectedListener());



        scriptsRecView = findViewById(R.id.gamepage2_scriptsRecView);

        ArrayList<Script> scripts = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            scripts.add(new Script("这里是正文" ));
        }

        GamePage2Adapter adapter = new GamePage2Adapter(this);
        adapter.setScripts(scripts);

        scriptsRecView.setAdapter(adapter);
        scriptsRecView.setLayoutManager(new LinearLayoutManager(this));


        initView();
    }

    private Button btn;

    private void initView() {
        btn = (Button) findViewById(R.id.belowline1_button6);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GamePage2Activity.this,GamePage3Activity.class);
                startActivity(intent);
            }
        });
    }

    public class MySelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(GamePage2Activity.this,"您选择的是"+episodeArray[position],Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }
}
