package com.tiberiuciuc.projectbday;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

public class AvailableImages extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerAdaptor adaptor;
    private int[] list = {
            R.drawable.cipa1, R.drawable.c2, R.drawable.c3, R.drawable.c4, R.drawable.c5,
            R.drawable.c6, R.drawable.c7, R.drawable.c8, R.drawable.c9, R.drawable.c10,
            R.drawable.c11, R.drawable.c12, R.drawable.c13, R.drawable.c14, R.drawable.c15,
            R.drawable.c16, R.drawable.c17, R.drawable.c18, R.drawable.c19, R.drawable.c20,
            R.drawable.c21, R.drawable.c22, R.drawable.c23, R.drawable.c24, R.drawable.c25,
            R.drawable.c26, R.drawable.c27, R.drawable.c28, R.drawable.c29, R.drawable.c30,
            R.drawable.c31, R.drawable.c32, R.drawable.c33, R.drawable.c34, R.drawable.c35,
            R.drawable.c36, R.drawable.c37, R.drawable.c38, R.drawable.c39, R.drawable.c40,
            R.drawable.c41, R.drawable.c42, R.drawable.c43, R.drawable.c44, R.drawable.c45,
            R.drawable.c46, R.drawable.c47, R.drawable.c48, R.drawable.c49, R.drawable.c50,
            R.drawable.c51, R.drawable.c52, R.drawable.c53, R.drawable.c54, R.drawable.c55
    };

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_images);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        toolbar = findViewById(R.id.toolbar_available_images);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

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
