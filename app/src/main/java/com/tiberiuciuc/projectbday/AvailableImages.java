package com.tiberiuciuc.projectbday;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class AvailableImages extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerAdaptor adaptor;
    private int[] list = {
            R.drawable.cipa1, R.drawable.c2, R.drawable.c3, R.drawable.c4, R.drawable.c5,
            R.drawable.c6, R.drawable.c7, R.drawable.c8, R.drawable.c9, R.drawable.c10,
            R.drawable.c11
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_images);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        //list implementation

        adaptor = new RecyclerAdaptor(list, this);
//        adaptor.setHasStableIds(true);
        recyclerView.setAdapter(adaptor);
//        recyclerView.setNestedScrollingEnabled(false);
    }
}
