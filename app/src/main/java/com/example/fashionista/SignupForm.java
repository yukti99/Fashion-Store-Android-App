package com.example.fashionista;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class SignupForm extends AppCompatActivity {
    private ConstraintLayout constraintLayout;
    private AnimationDrawable animationDrawable;
    // Variables for input fields
    private Button createAccountBtn;
    private EditText inFirstName,inLastName,inPhone,inPassword;
    private ProgressDialog loadingBar;
    DataBaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_form);
        /*
        //------------------------FOR BACKGROUND ANIMATION---------------------------------------
        // init constraintLayout
        constraintLayout = (ConstraintLayout) findViewById(R.id.constraintLayout);
        // initializing animation drawable by getting background from constraint layout
        animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        // setting enter fade animation duration to 5 seconds
        animationDrawable.setEnterFadeDuration(4000);
        // setting exit fade animation duration to 2 seconds
        animationDrawable.setExitFadeDuration(2000);
        //----------------------------------------------------------------------------------------
        */

        // for input fields editTexts
        createAccountBtn = (Button)findViewById(R.id.createAccount);
        inFirstName = (EditText)findViewById(R.id.firstName);
        inLastName = (EditText)findViewById(R.id.lastName);
        inPhone = (EditText)findViewById(R.id.regPhone);
        inPassword = (EditText)findViewById(R.id.regPassword);

        // calling constructor of DataBaseHelper class to create the database
        myDb = new DataBaseHelper(this);

        loadingBar = new ProgressDialog(this);
        // Creating user Account
        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateAccount();
            }
        });



    }
    private void CreateAccount(){
        String FName = inFirstName.getText().toString();
        String LName = inLastName.getText().toString();
        String phone = inPhone.getText().toString();
        //Toast.makeText(SignupForm.this,"Phone0 = "+phone,Toast.LENGTH_SHORT).show();
        String password = inPassword.getText().toString();

        if (TextUtils.isEmpty(FName)){
            Toast.makeText(this,"Please enter your First name...",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(LName)){
            Toast.makeText(this,"Please enter your Last name...",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phone)){
            Toast.makeText(this,"Please enter your valid Phone...",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter your valid Password...",Toast.LENGTH_SHORT).show();
        }
        else{
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait! while we are checking the credentials ");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            //Toast.makeText(SignupForm.this,"Phone1 = "+phone,Toast.LENGTH_SHORT).show();
            ValidatePhone(FName,LName,phone,password);
        }
    }
    private void ValidatePhone(String fName,String lName,String phone,String password) {

        if ((myDb.UserPhoneExists(phone))==false){
            if (myDb.insertDataUsers(fName,lName,phone,password)){
                Toast.makeText(SignupForm.this,"Congratulations! Your account has been created!",Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
               // Toast.makeText(SignupForm.this,"Welcome "+fName+" !!",Toast.LENGTH_SHORT).show();
                //loadingBar.dismiss();
                try{
                    Intent intent = new Intent(SignupForm.this, CustomerLogin.class);
                    startActivity(intent);
                }catch(Exception e ){
                    e.printStackTrace();
                }
            }
            else{
                Toast.makeText(SignupForm.this,"Sorry! Some error occurred.. Try again later!",Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }

        }
        else{
            Toast.makeText(SignupForm.this,"Entered Phone already exits!!",Toast.LENGTH_SHORT).show();
            loadingBar.dismiss();
            Toast.makeText(SignupForm.this,"Please try again with another phone!",Toast.LENGTH_SHORT).show();
            loadingBar.dismiss();
        }

    }
    public void openHomepage(){
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
    }
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
