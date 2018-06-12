package com.example.whiteroom.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.whiteroom.R;
import com.example.whiteroom.application.myApplication;

import cn.bmob.v3.Bmob;

public class MainMenu extends AppCompatActivity {

    //创建菜单列表数组
    String classes[] = {"福味轩","学一小白房","艺园小白房", "主食售卖（煎饼）"};
    private ListView listView;
    private ListAdapter listAdapter;
    private myApplication application;
    FloatingActionButton mButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Bmob.initialize(this, "1d9071643a06f98aab7eddb3dffe97f9");
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,classes));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent startA = new Intent(MainMenu.this, MainActivity.class); //class名的方式指定Activity启动
                startActivity(startA);
            }
        });
        mButton = (FloatingActionButton) findViewById((R.id.floatingActionButton3));
        mButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent startA;
                application = (myApplication) getApplication();
                if (application.getlogined()) {
                    startA = new Intent(MainMenu.this, SelfCenter.class);
                }
                else {
                    startA = new Intent(MainMenu.this, LoginActivity.class);
                }
                startActivity(startA);
            }
        });
    }
}
