package com.ryd.cardview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.shadow.ShadowViewDelegate;
import com.ryd.reflectupdatecardviewshadow.CardViewUtils;
import com.ryd.shadowdrawable.ShadowDrawable;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CardViewUtils.init(); // 或者放在Application的onCreate方法中

        setContentView(R.layout.activity_main);

        CardView cardView = findViewById(R.id.cvx);
        // 改变CarView的阴影颜色
        CardViewUtils.setCardShadowColor(cardView, Color.parseColor("#FFBB86FC"), Color.parseColor("#FF6200EE"));

        View view = findViewById(R.id.shadow_drawable);
        ShadowDrawable.setShadowDrawable(view,Color.parseColor("#ff33b5e5"),10,Color.parseColor("#ffff4444"),40,0,0);
    }
}