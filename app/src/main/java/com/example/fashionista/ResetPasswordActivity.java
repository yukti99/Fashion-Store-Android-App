package com.example.fashionista;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fashionista.Prevalent.Prevalent;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class ResetPasswordActivity extends AppCompatActivity{
    private String check = "";
    private TextView pageTitle,titleQuestions;
    private EditText phoneNumber,ques1,ques2;
    private Button verifyButton;
    DataBaseHelper userDb;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        check = getIntent().getStringExtra("check");
        titleQuestions = findViewById(R.id.title_questions);
        pageTitle = findViewById(R.id.page_title);
        phoneNumber = findViewById(R.id.find_phone_number);
        ques1 = findViewById(R.id.question_1);
        ques2 = findViewById(R.id.question_2);
        verifyButton = findViewById(R.id.verify_btn);
        userDb = new DataBaseHelper(this);


    }
    @Override
    protected void onStart(){
        super.onStart();
        phoneNumber.setVisibility(View.GONE);
        if (check.equals("settings")){
            displayPreviousAnswers();
            pageTitle.setText("Set Questions");
            titleQuestions.setText("Please set following Security Answers");
            verifyButton.setText("Set");
            verifyButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){

                    setSecurityAnswers();
                }
            });
        }
        else if (check.equals("login")){
            phoneNumber.setVisibility(View.VISIBLE);
            verifyButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    verifyUser();
                }
            });
        }
    }
    private void verifyUser() {
        final String phone = phoneNumber.getText().toString();
        if (phone.equals("")){
            Toast.makeText(ResetPasswordActivity.this, "Please enter your account phone number!!", Toast.LENGTH_SHORT).show();
        }else {
            if (userDb.UserPhoneExists(phone) == true) {
                phoneNumber.setTextColor(Color.GREEN);
                String ans1 = userDb.getSecurity1(phone).toLowerCase();
                String ans2 = userDb.getSecurity2(phone).toLowerCase();
                if (ans1.equals("") || ans2.equals("")) {
                    Toast.makeText(ResetPasswordActivity.this, "You have NOT set the Security Keys for your account!!", Toast.LENGTH_SHORT).show();
                    Toast.makeText(ResetPasswordActivity.this, "Please make a new account!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                String q1 = ques1.getText().toString().toLowerCase();
                String q2 = ques2.getText().toString().toLowerCase();
                if (q1.equals("")){
                    Toast.makeText(ResetPasswordActivity.this, "You must answer Question-1!!", Toast.LENGTH_SHORT).show();
                }
                else if (q2.equals("")){
                    Toast.makeText(ResetPasswordActivity.this, "You must answer Question-2!!", Toast.LENGTH_SHORT).show();
                }else {
                    if (q1.equals(ans1)) {
                        ques1.setTextColor(Color.GREEN);
                        if (q2.equals(ans2)) {
                            ques2.setTextColor(Color.GREEN);
                            Toast.makeText(ResetPasswordActivity.this, "Correct!!", Toast.LENGTH_SHORT).show();

                            changeUserPassword(phone);


                        } else {
                            ques2.setTextColor(Color.RED);
                            Toast.makeText(ResetPasswordActivity.this, "Wrong Security Key!!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        ques1.setTextColor(Color.RED);
                        Toast.makeText(ResetPasswordActivity.this, "Wrong Security Key!!", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                phoneNumber.setTextColor(Color.RED);
                Toast.makeText(ResetPasswordActivity.this, "The Entered phone number is Incorrect!!", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void changeUserPassword(final String phone) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ResetPasswordActivity.this);
        builder.setTitle("New Password");
        final EditText newPassword = new EditText(ResetPasswordActivity.this);
        newPassword.setHint("Write New Password here..");
        builder.setView(newPassword);
        builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!newPassword.getText().toString().equals("")) {
                    if (userDb.changePassword(phone, newPassword.getText().toString())) {
                        Toast.makeText(ResetPasswordActivity.this, "Password Changed Successfully!!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ResetPasswordActivity.this, CustomerLogin.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(ResetPasswordActivity.this, "Error!!Please try again Later!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(ResetPasswordActivity.this, "New password Cannot be Empty!", Toast.LENGTH_SHORT).show();

                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
    private void setSecurityAnswers(){
        String ans1 = ques1.getText().toString().toLowerCase();
        String ans2 = ques2.getText().toString().toLowerCase();
        if (ques1.equals("")||ques2.equals("")){
            Toast.makeText(ResetPasswordActivity.this,"Please answer both the Security Questions!\n",Toast.LENGTH_SHORT);
        }else{
            //DatabaseReference ref = FireBaseDatabase.getInstance().getReference().child("Users");
            String phone = Prevalent.currentOnlineUser.getPhone();
            // Entering the security questions' answers into to database
            if (userDb.enterSecurityQues(phone,ans1,ans2)){
                Toast.makeText(ResetPasswordActivity.this,"Security Questions set Successfully!\n",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(ResetPasswordActivity.this,"Error! Please Try again Later!\n",Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent(ResetPasswordActivity.this, SettingsActivity.class);
            startActivity(intent);
        }
    }
    private void displayPreviousAnswers(){
        String phone = Prevalent.currentOnlineUser.getPhone();
        String ans1 = userDb.getSecurity1(phone);
        String ans2 = userDb.getSecurity2(phone);
        System.out.println("ans1 = "+ans1);
        System.out.println("ans2 = "+ans2);
        if (ans1.equals("null") && ans2.equals("null")){
            System.out.println("The questions' answers are not in database!\n");
        }else{
            ques1.setText(ans1);
            ques2.setText(ans2);
        }
    }
}