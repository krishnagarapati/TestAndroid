package com.example.krishna.testclassandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.Menu;

import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    public static final int DETECT_NONE = 0;
    public static final int DETECT_SNORE = 1;
    public static int selectedDetection = DETECT_NONE;

    private DetectorThread detectorThread;
    private RecorderThread recorderThread;
    private DrawThread drawThread;

    public static int snoreValue = 0;

    private View mainView;
    private Button mSleepRecordBtn, mAlarmBtn, mRecordBtn, mTestBtn;
    private TextView txtAbs;

    private Toast mToast;

    private Handler rhandler = new Handler();
    private Handler showhandler = null;
    private Handler alarmhandler = null;

    private Intent intent;
    private PendingIntent pendingIntent;
    private AlarmManager am;

    private SurfaceView sfv;
    private Paint mPaint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Snoring Detector");

        mSleepRecordBtn = (Button) this.findViewById(R.id.btnSleepRecord);

        mTestBtn = (Button) findViewById(R.id.btnAlarmTest);
        txtAbs = (TextView) findViewById(R.id.txtaverageAbsValue);
        sfv = (SurfaceView) this.findViewById(R.id.SurfaceView);

        intent = new Intent(MainActivity.this, AlarmReceiverActivity.class);
        pendingIntent = PendingIntent.getActivity(MainActivity.this, 2, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        am = (AlarmManager) getSystemService(ALARM_SERVICE);

        mPaint = new Paint();
        mPaint.setColor(Color.RED);

        showhandler = new Handler() {
            public void handleMessage(Message msg) {
                txtAbs.setText(msg.obj.toString());
            }
        };


        alarmhandler = new Handler() {
            public void handleMessage(Message msg) {
                int interval = 1;
                int i = msg.arg1;
                setLevel(i);
                AlarmStaticVariables.level = AlarmStaticVariables.level1;
                am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                        + (interval * 1000), pendingIntent);
            }
        };


        mSleepRecordBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                selectedDetection = DETECT_SNORE;

                recorderThread = new RecorderThread(showhandler);
                recorderThread.startRecording();
                detectorThread = new DetectorThread(recorderThread,
                        alarmhandler);
                detectorThread.start();
                drawThread = new DrawThread(sfv.getHeight() / 2, sfv, mPaint);
                drawThread.start();


                mToast = Toast.makeText(getApplicationContext(),
                        "Recording & Detecting start", Toast.LENGTH_LONG);
                mToast.show();

            }
        });



        mTestBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                int level = 1;
                mToast = Toast.makeText(getApplicationContext(),
                        "Alarm Started", Toast.LENGTH_LONG);
                mToast.show();
                setLevel(level);
                startOneShoot();
            }
        });

    }


    public void startOneShoot() {
        int i = 5;
        am.set(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + (i * 1000), pendingIntent);
    }

    public void setLevel(int l) {
        switch (l) {
            case 0:
                AlarmStaticVariables.level = AlarmStaticVariables.level0;
                break;
            case 1:
                AlarmStaticVariables.level = AlarmStaticVariables.level1;
                break;
           case 2:
                AlarmStaticVariables.level = AlarmStaticVariables.level2;
                break;
            case 3:
                AlarmStaticVariables.level = AlarmStaticVariables.level3;
                break;
            default:
                AlarmStaticVariables.level = AlarmStaticVariables.level1;
                break;
        }
    }

    private void goHomeView() {
        setContentView(mainView);
        if (recorderThread != null) {
            recorderThread.stopRecording();
            recorderThread = null;
        }
        if (detectorThread != null) {
            detectorThread.stopDetection();
            detectorThread = null;
        }
        selectedDetection = DETECT_NONE;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 0, 0, "Quit demo");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                am.cancel(pendingIntent);
                finish();
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            goHomeView();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void onDestroy() {
        super.onDestroy();
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
