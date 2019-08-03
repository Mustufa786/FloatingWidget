package com.codixlab.floatingwidget;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    public static final int CODE_DRAW_OVER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)){
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent,CODE_DRAW_OVER);

        }else{
            initView();
        }
    }

    private void initView() {


        Button open= findViewById(R.id.openWidget);
        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startService(new Intent(MainActivity.this,FloatingService.class));
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        if(requestCode == CODE_DRAW_OVER){
            if(resultCode == RESULT_OK){
                initView();
            }else{
                Toast.makeText(this, "Please allow overlay permission", Toast.LENGTH_SHORT).show();
                finish();
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }



    }
}
