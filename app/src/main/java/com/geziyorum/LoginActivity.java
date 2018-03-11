package com.geziyorum;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private Button btn_sign_in, btn_sign_up;
    private EditText edt_email,edt_password;
    private TextView txt_forgot_password;
    private String input_email,input_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        getComponent();
        mAuth = FirebaseAuth.getInstance();

        btn_sign_in.setOnClickListener(this);
        btn_sign_up.setOnClickListener(this);
        txt_forgot_password.setOnClickListener(this);

    }


    private void getComponent(){
        btn_sign_in = findViewById(R.id.login_btn_signin);
        btn_sign_up = findViewById(R.id.login_btn_signup);
        edt_email = findViewById(R.id.login_edt_email);
        edt_password = findViewById(R.id.login_edt_password);
        txt_forgot_password = findViewById(R.id.login_txt_forgotpass);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login_btn_signin:
               signIn();
                break;
            case R.id.login_btn_signup:
                Intent intent = new Intent(this,SignUpActivity.class);
                startActivity(intent);

                break;
            case R.id.login_txt_forgotpass:
                // Send Mail to reset password
                break;
        }
    }

    private void signIn(){

        input_email = String.valueOf(edt_email.getText());
        input_password = String.valueOf(edt_password.getText());
        if (TextUtils.isEmpty(input_email)) {
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(input_password)) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(input_email, input_password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("SUCCESS", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent=new Intent(LoginActivity.this,DashBoardActivity.class);
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("FAIL", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }


}
