package com.cimb.cimb3dconquest;
import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;

import java.util.List;

import io.paperdb.Paper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Register_Pattern extends AppCompatActivity {

    String save_pattern_key = "pattern_code";
    String confirmation_pattern = "";
    PatternLockView mPatternLockView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register__pattern);
        Paper.init(this);
        mPatternLockView = (PatternLockView) findViewById(R.id.registerlockPattern);
        mPatternLockView.addPatternLockListener(new PatternLockViewListener() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {

            }

            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {
                confirmation_pattern = PatternLockUtils.patternToString(mPatternLockView, pattern);
            }

            @Override
            public void onCleared() {

            }
        });

        Button button = (Button) findViewById(R.id.buttonSetPattern);
        button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           Paper.book().write(save_pattern_key, confirmation_pattern);
           Toast.makeText(Register_Pattern.this, "Pattern has been saved", Toast.LENGTH_SHORT).show();


          }
        }
        );

    }


}
