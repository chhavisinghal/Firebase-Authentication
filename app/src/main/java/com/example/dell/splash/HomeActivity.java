package com.example.dell.splash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class HomeActivity extends AppCompatActivity {

    Button signIn,signUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        signIn=(Button)findViewById(R.id.button);
        signUp=(Button)findViewById(R.id.button2);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                Intent h1=new Intent(HomeActivity.this,Login.class);
                startActivity(h1);
            }
        });

       signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Register",Toast.LENGTH_SHORT).show();
                Intent h2=new Intent(HomeActivity.this,SignUp.class);
                startActivity(h2);
            }
        });
    }
}
