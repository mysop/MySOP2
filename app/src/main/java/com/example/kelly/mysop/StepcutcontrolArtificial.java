package com.example.kelly.mysop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;

//p304
public class StepcutcontrolArtificial extends Activity implements GestureDetector.OnGestureListener {

    private GestureDetector detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stepcutcontrol_artificial);
        detector = new GestureDetector(this, this);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        // TODO Auto-generated method stub
        return false;
    }
    @Override
    public void onShowPress(MotionEvent e) {
        // TODO Auto-generated method stub

    }
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        // TODO Auto-generated method stub
        return false;
    }
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        // TODO Auto-generated method stub
        return false;
    }
    @Override
    public void onLongPress(MotionEvent e) {
        // TODO Auto-generated method stub

    }
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        return detector.onTouchEvent(event);//这里用的detector就是上面创建的对象
    }
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        // TODO Auto-generated method stub
        if ((e1.getX() - e2.getX()) > 50) {//说明是左滑
            Intent intent = new Intent();
            intent.setClass(this, Start.class);
            startActivity(intent);
            // 设置切换动画，从右边进入，左边退出
            overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            return true;
        } else
            return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_stepcutcontrol_artificial, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
