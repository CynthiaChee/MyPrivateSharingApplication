package com.example.myprivatesharingapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //Initializing variables
    EditText userNameTextEdit, passwordTextEdit;
    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //FInd view by ID
        userNameTextEdit = findViewById(R.id.userNameEdit);
        passwordTextEdit = findViewById(R.id.passwordEdit);
        Button loginButton = findViewById(R.id.loginButton);
        Button signUpButton = findViewById(R.id.signupButton);

        db = new DatabaseHelper(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Get login details
                String username = userNameTextEdit.getText().toString();
                String password = passwordTextEdit.getText().toString();
                boolean user =  db.fetchUser(username,password);
                if (user)
                {
                    Toast.makeText(MainActivity.this, "Log in successful!", Toast.LENGTH_SHORT).show();
                    Intent loginIntent = new Intent(getApplicationContext(), HomeActivity.class);
                    loginIntent.putExtra("username",username);
                    startActivity(loginIntent);
                    finish();
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Error, please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //If sign up button clicked
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
