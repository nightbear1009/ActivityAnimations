package com.fanpage.tedliang.activityanimation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MyActivity extends Activity implements View.OnClickListener {
    public static final String PACKAGE_NAME = "com.example.MyActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        Button btn = (Button)findViewById(R.id.btn);
        btn.setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(MyActivity.this,NextActivity.class);
        int[] screenLocation = new int[2];
        view.getLocationOnScreen(screenLocation);
        intent.putExtra(PACKAGE_NAME + ".left", screenLocation[0]).
                putExtra(PACKAGE_NAME + ".top", screenLocation[1]).
                putExtra(PACKAGE_NAME + ".width", view.getWidth()).
                putExtra(PACKAGE_NAME + ".height", view.getHeight());
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}
