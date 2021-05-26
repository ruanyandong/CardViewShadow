package com.ryd.cardview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.graphics.Color;
import android.os.Bundle;

import com.ryd.reflectupdatecardviewshadow.CardViewUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CardViewUtils.init(); // 或者放在Application的onCreate方法中

        setContentView(R.layout.activity_main);

        CardView cardView = findViewById(R.id.cvx);
        // 改变CarView的阴影颜色
        CardViewUtils.setCardShadowColor(cardView, Color.parseColor("#FFBB86FC"), Color.parseColor("#FF6200EE"));

    }
}