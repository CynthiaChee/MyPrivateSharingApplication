package com.example.myprivatesharingapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.FileNotFoundException;

public class SignupActivity extends AppCompatActivity {

    //Initializing variables
    DatabaseHelper db;
    Intent imageIntent;
    EditText fullnameInput,usernameInput, passwordInput, confirmPasswordInput, phonenumberInput;
    ImageButton addImageButton;
    String fullName, userName, password, confirmPassword, phoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Find view by ID
        fullnameInput = findViewById(R.id.signupFullNameEdit);
        usernameInput = findViewById(R.id.signupUserNameEdit);
        passwordInput = findViewById(R.id.signupPasswordEdit);
        confirmPasswordInput = findViewById(R.id.signupConfirmPasswordEdit);
        phonenumberInput = findViewById(R.id.signupPhoneEdit);
        addImageButton = findViewById(R.id.addPhotoField);
        Button createAccountButton = findViewById(R.id.signupCreateAccountButton);
        db = new DatabaseHelper(this);

        //If add image button clicked
        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                ActivityCompat.requestPermissions(SignupActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
            }
        });

        //If create account button clicked
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fullName = fullnameInput.getText().toString();
                userName = usernameInput.getText().toString();
                password = passwordInput.getText().toString();
                confirmPassword = confirmPasswordInput.getText().toString();
                phoneNumber = phonenumberInput.getText().toString();
                if (password.equals(confirmPassword))
                {
                    User user = new User(fullName, userName, password, phoneNumber);
                    long result = db.insertUser(user);
                    if (result > 0)
                    {
                        Toast.makeText(SignupActivity.this, "Sign up successful!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(SignupActivity.this, "Error, please try again.", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(SignupActivity.this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            Uri targetUri = data.getData();
            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                addImageButton.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                Toast.makeText(SignupActivity.this, "File not found", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                //Request permission
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivityForResult(imageIntent, 0);
                } else {
                    Toast.makeText(SignupActivity.this, "Permission is required.", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
