package com.cimb.cimb3dconquest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PasswordActivity extends AppCompatActivity {

    private TextView Password;
    private Button Login;
//    LoginActivity loginActivity = new LoginActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        Password = (EditText) findViewById(R.id.txtPW);
        Login = (Button)findViewById(R.id.btnLogin);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(Password.getText().toString());
            }
        });
    }

    private void  validate(String Password){
        if(LoginActivity.user.getUsername().equals(LoginActivity.tempName) && Password.equals(LoginActivity.tempmPass) ){
//            Intent intent = new Intent( PasswordActivity.this, Register_Face.class);
            Intent intent = new Intent( PasswordActivity.this, SelectionActivity.class);
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

