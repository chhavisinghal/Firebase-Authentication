package com.example.dell.splash;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    EditText email,pass;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toast.makeText(getApplicationContext(),"Login here",Toast.LENGTH_SHORT).show();

        findViewById(R.id.button6).setOnClickListener(this);
        findViewById(R.id.button5).setOnClickListener(this);

        email=(EditText)findViewById(R.id.editText3);
        pass=(EditText)findViewById(R.id.editText4);

        progressBar=(ProgressBar)findViewById(R.id.progressbar);

        mAuth=FirebaseAuth.getInstance();

    }

    private void userLogin(){

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
        mAuth.signInWithEmailAndPassword(e,p).addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
              //  Toast.makeText(getApplicationContext(),"Wait login in process",Toast.LENGTH_LONG).show();
                    if(task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(),"Login successful",Toast.LENGTH_LONG).show();
                        finish();
                        Intent intent=new Intent(Login.this,ProfileActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                    }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()!=null)
        {
            finish();
            startActivity(new Intent(this,ProfileActivity.class));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button6:
                               finish();
                               startActivity(new Intent(this,SignUp.class));
                               break;

            case R.id.button5: userLogin();
                               break;
        }

    }
}
