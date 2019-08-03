package com.codixlab.floatingwidget;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

public class FloatingService extends Service {


    private WindowManager windowManager;
    private View floatinglayout;

    public FloatingService() {
    }


    @Override
    public void onCreate() {
        super.onCreate();

        floatinglayout = LayoutInflater.from(this).inflate(R.layout.layout_floating_widget, null);

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 100;

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManager.addView(floatinglayout, params);


        final View collapseView = floatinglayout.findViewById(R.id.collapse_layout);
        final View expandedView = floatinglayout.findViewById(R.id.expanded_layout);
        ImageButton closeButton = floatinglayout.findViewById(R.id.closeBtn);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stopSelf();
            }
        });

        Button openApp = floatinglayout.findViewById(R.id.openApp);
        openApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(FloatingService.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                stopSelf();
            }
        });


        floatinglayout.findViewById(R.id.rootLayout).setOnTouchListener(new View.OnTouchListener() {

            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;


            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:

                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;

                    case MotionEvent.ACTION_UP:

                        int xDiff = (int) (event.getRawX() - initialX);
                        int yDiff = (int) (event.getRawY() - initialY);

                        if(xDiff < 10 && yDiff < 10){
                            if(isViewCollapsed()){
                                collapseView.setVisibility(View.GONE);
                                expandedView.setVisibility(View.VISIBLE);

                            }
                        }
                        return true;

                    case MotionEvent.ACTION_MOVE:

                        params.x =  initialX + (int)(event.getRawX()  - initialTouchX);
                        params.y =  initialY + (int)(event.getRawY()  - initialTouchY);

                        windowManager.updateViewLayout(floatinglayout,params);

                        return true;
                }

                return false;
            }
        });
    }

    private boolean isViewCollapsed() {

        return floatinglayout == null || floatinglayout.findViewById(R.id.collapse_layout).getVisibility() == View.VISIBLE;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (floatinglayout != null)
            windowManager.removeViewImmediate(floatinglayout);
    }
}
