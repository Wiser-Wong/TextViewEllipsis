package com.wiser.textviewellipsis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView rlv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rlv = findViewById(R.id.rlv);
        rlv.setLayoutManager(new LinearLayoutManager(this));
        rlv.setAdapter(new MainAdapter(this,getData()));
    }

    private List<String> getData(){
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            if (i % 2 == 0)
            list.add("我带省略号我带省略号我带省略号我带省略号我带省略号我带省略号");
            else list.add("我不带省略号");
        }
        return list;
    }
}
