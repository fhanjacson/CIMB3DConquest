package com.cimb.cimb3dconquest;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText Name;
    private Button Login;

    public static User user = new User();
    public static String tempName = "cimb";
    public static String tempmPass = "123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Name = (EditText) findViewById(R.id.txtUserID);
        Login = (Button) findViewById(R.id.btnNext);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(Name.getText().toString());
            }
        });
    }

    private void  validate(String UserID){
        if((UserID.equals(tempName))){
            user.setUsername(tempName);
            Intent intent = new Intent( LoginActivity.this, PasswordActivity.class);
            startActivity(intent);

        } else {

            Context context = getApplicationContext();
            CharSequence text = "Invalid Username or Password!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

        }
    }
    }


