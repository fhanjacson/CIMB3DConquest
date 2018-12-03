package com.cimb.cimb3dconquest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SelectionActivity extends AppCompatActivity {

    private Button register, login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        register = (Button)findViewById(R.id.btnRegister);
        register.setOnClickListener(new Button.OnClickListener()
        {
            public void onClick(View arg0) {
                gotoRegister();
            }
        });
        login = (Button)findViewById(R.id.btnLogin2);
        login.setOnClickListener(new Button.OnClickListener()
        {
            public void onClick(View arg0) {
                gotoLogin();
            }
        });

    }

    void gotoRegister(){
        Intent intent = new Intent( SelectionActivity.this, Register_Face.class);
        startActivity(intent);
    }

    void gotoLogin(){
        Intent intent = new Intent( SelectionActivity.this, Login2Auth.class);
        startActivity(intent);
    }




}
