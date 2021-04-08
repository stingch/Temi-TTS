package com.example.temitts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.robotemi.sdk.listeners.OnRobotReadyListener;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;

public abstract class MainActivity extends AppCompatActivity implements OnRobotReadyListener, Robot.TtsListener{
    private EditText etSpeak;
    private Robot robot;

    /**
     * Places this application in the top bar for a quick access shortcut.
     */
    @Override
    public void onRobotReady(boolean isReady) {
        if (isReady) {
            try {
                final ActivityInfo activityInfo = getPackageManager().getActivityInfo(getComponentName(), PackageManager.GET_META_DATA);
                // Robot.getInstance().onStart() method may change the visibility of top bar.
                robot.onStart(activityInfo);
            } catch (PackageManager.NameNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etSpeak = findViewById(R.id.etSpeak);
        robot = Robot.getInstance(); // get an instance of the robot in order to begin using its features.
    }

    protected void onStart() {
        super.onStart();
        robot.addOnRobotReadyListener(this);
        robot.addTtsListener(this);
    }

    protected void onStop() {
        super.onStop();
        robot.getInstance().removeTtsListener(this);
        robot.removeOnRobotReadyListener(this);
    }

    public void speak(View view) {
        TtsRequest ttsRequest = TtsRequest.create(etSpeak.getText().toString().trim(), true);
        robot.speak(ttsRequest);
        //hideKeyboard();
    }


}