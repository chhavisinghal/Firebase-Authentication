package com.example.dell.splash;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    EditText email,pass;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Toast.makeText(getApplicationContext(),"Register here",Toast.LENGTH_SHORT).show();

        findViewById(R.id.button3).setOnClickListener(this);
        findViewById(R.id.button4).setOnClickListener(this);

        progressBar=(ProgressBar)findViewById(R.id.progressbar);

        email=(EditText)findViewById(R.id.editText);
        pass=(EditText)findViewById(R.id.editText2);

        //firebase
        mAuth = FirebaseAuth.getInstance();
    }

    private void registerUser(){
        String e=email.getText().toString().trim();
        String p=pass.getText().toString().trim();
        if(e.isEmpty())
        {
            email.setError("Email is required");
            email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(e).matches())
        {
            email.setError("Please enter a valid email");
            email.requestFocus();
            return;
        }
        if(p.isEmpty())
        {
            pass.setError("Email is required");
            pass.requestFocus();
            return;
        }
        if(p.length()<6)
        {
            pass.setError("Minimum length of password should be 6");
            pass.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(e,p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful())
                {
                    Toast.makeText(getApplicationContext(),"User Registered Successfully",Toast.LENGTH_SHORT).show();
                    finish();
                    Intent intent=new Intent(SignUp.this,ProfileActivity.class);
                    startActivity(intent);
                }
                else
                {
                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(getApplicationContext(),"You are already registered",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button3: registerUser();
                               break;
            case R.id.button4:
                               finish();
                               startActivity(new Intent(this,Login.class));
                               break;
        }

    }
}
