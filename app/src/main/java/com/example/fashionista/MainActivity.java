package com.example.fashionista;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.drawable.AnimationDrawable;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.Button;

import com.example.fashionista.Admin.AdminLogin;


public class MainActivity extends AppCompatActivity {
     //  ***********************************DECLARATIONS********************************************
    //  FOR BACKGROUND
    private ConstraintLayout constraintLayout;
    private AnimationDrawable animationDrawable;
    // FOR BUTTONS
    private Button openCustomerLogin;
    private Button openAdminLogin;

    // *********************************************************************************************
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //---------------------------------BY DEFAULT-----------------------------------------------
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TO OPEN THE CUSTOMER LOGIN PAGE CLICKING CUSTOMER LOGIN BUTTON ON MAIN PAGE
        openCustomerLogin = (Button) findViewById(R.id.customerButton);
        openCustomerLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCustomerLoginForm();
            }
        });
        // TO OPEN THE ADMIN LOGIN PAGE CLICKING CUSTOMER LOGIN BUTTON ON MAIN PAGE
        openAdminLogin = (Button) findViewById(R.id.adminLogin);
        openAdminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAdminLoginForm();
            }
        });

    }
    //**********************************************************************************************

    public void openCustomerLoginForm(){
        Intent intent = new Intent(MainActivity.this, CustomerLogin.class);
        startActivity(intent);
    }
    // FUNCTION TO ADMIN LOGIN ACTIVITY USING INTENT
    public void openAdminLoginForm(){
        Intent intent = new Intent(MainActivity.this, AdminLogin.class);
        startActivity(intent);
    }

    // DEFAULT FUNCTIONS
    @Override
    protected void onResume() {
        super.onResume();
        if (animationDrawable != null && !animationDrawable.isRunning()) {
            // start the animation
            animationDrawable.start();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (animationDrawable != null && animationDrawable.isRunning()) {
            // stop the animation
            animationDrawable.stop();
        }
    }
}




