package com.geziyorum;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText edt_name,edt_surname,edt_email,edt_password;
    private Button btn_signup;
    private String inputEmail,inputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getComponent();
        mAuth = FirebaseAuth.getInstance();
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });
    }
    private void signUp(){
    inputEmail = edt_email.getText().toString();
    inputPassword = edt_password.getText().toString();
        mAuth.createUserWithEmailAndPassword(inputEmail, inputPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("SUCCESS", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("FAILED", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("name", edt_name.getText().toString());
        editor.putString("surname", edt_surname.getText().toString());
        editor.putString("email", edt_email.getText().toString());
        editor.apply();
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }
    private void getComponent(){
        edt_name = findViewById(R.id.signup_edt_name);
        edt_surname = findViewById(R.id.signup_edt_surname);
        edt_email = findViewById(R.id.signup_edt_email);
        edt_password = findViewById(R.id.signup_edt_password);
        btn_signup = findViewById(R.id.signup_btn_signup);
    }
}
