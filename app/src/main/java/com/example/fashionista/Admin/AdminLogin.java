package com.example.fashionista.Admin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fashionista.DataBaseHelper;
import com.example.fashionista.DataBaseHelperAdmins;
import com.example.fashionista.R;


public class AdminLogin extends AppCompatActivity {

    //  ***********************************DECLARATIONS********************************************
    //  ADMINS CAN ACCESS BOTH CUSTOMER AND ADMIN DATABASES
    // FOR USERS DATABASE
    DataBaseHelper myDb;
    // FOR ADMINS DATABASE
    DataBaseHelperAdmins adminDb;

    //  FOR BACKGROUND
    private ConstraintLayout constraintLayout;
    private AnimationDrawable animationDrawable;

    // FOR BUTTONS
    private Button LoginBtn;
    Button btnviewAllUsers;
    Button btnviewAllAdmins;
    private Button openCustomerLogin;
    private Button openAdminLogin;

    // FOR EDIT TEXTS
    private EditText loginPhone,loginPassword;

    // FOR PROGRESS DIALOG BAR
    private ProgressDialog loadingBar;


    // *********************************************************************************************
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //---------------------------------BY DEFAULT-----------------------------------------------
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        // CONSTRUCTOR TO CREATE USER DATABASE USING USER DATABASE HELPER CLASS
        myDb = new DataBaseHelper(this);
        adminDb = new DataBaseHelperAdmins(this);

        // TO INITIALISE THE ADMIN DB WITH AN ADMIN
        adminDb.insertDataAdmins("Yukti","Khurana","8851237085","Magic1");
        adminDb.insertDataAdmins("Shruti","Singh","9718527839","Magic2");


        // LOGIN ACTIVITY FROM MAIN PAGE
        loadingBar    = new ProgressDialog(this);
        LoginBtn      = (Button)findViewById(R.id.loginButton);
        loginPhone    = (EditText)findViewById(R.id.phoneLogin);
        loginPassword = (EditText)findViewById(R.id.passLogin);
        LoginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                AdminLogin();
            }
        });
        // --------------BUTTONS INITIALISED TO PRINT ALL USER & ADMIN DATABASE DATA----------------
        //btnviewAllUsers  = (Button)findViewById(R.id.viewAllUsers);
        btnviewAllAdmins = (Button)findViewById(R.id.viewAllAdmins);
        //viewAllUsers();
        viewAllAdmins();
        //------------------------------------------------------------------------------------------
    }
    private void AdminLogin(){
        String phone    = loginPhone.getText().toString();
        String password = loginPassword.getText().toString();
        // --------------ALERTS USER IF ANY OF THE FIELDS ARE EMPTY---------------------------------
        if (TextUtils.isEmpty(phone)){
            Toast.makeText(this,"Please enter your Phone...",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter your Password...",Toast.LENGTH_SHORT).show();
        }
        else{
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please wait! while we are checking the credentials ");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            //TO ALLOW ACCESS TO SIGNED UP USER
            AllowAdminAccess(phone,password);
        }
        //------------------------------------------------------------------------------------------
    }
    // FOR ADMIN LOGIN
    private void AllowAdminAccess(String phone,String password) {
        Boolean phoneExists = adminDb.AdminPhoneExists(phone);
        if (phoneExists){
            Boolean passCorrect = adminDb.AdminPasswordCorrect(phone, password);
            System.out.println("Password correct = "+ passCorrect);
            if (passCorrect){
                Toast.makeText(AdminLogin.this,"Welcome "+adminDb.getAdminFName(phone).toString()+" !",Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
                try{
                    Intent intent = new Intent(AdminLogin.this,AdminCategoryActivity.class);
                    startActivity(intent);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            else{
                Toast.makeText(AdminLogin.this,"Sorry! Incorrect Password!",Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }
        else{
            //Toast.makeText(MainActivity.this,"Database = "+parentDbName,Toast.LENGTH_SHORT).show();
            Toast.makeText(AdminLogin.this,"Account with this phone number does not exist!",Toast.LENGTH_SHORT).show();
            loadingBar.dismiss();
            Toast.makeText(AdminLogin.this,"Please create a new account..",Toast.LENGTH_SHORT).show();
            loadingBar.dismiss();
        }
    }

    public void viewAllUsers() {
        btnviewAllUsers.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //used the instance of DatabaseHelper
                        Cursor res = myDb.getAllDataUsers();
                        if(res.getCount() == 0) {
                            // show error message  if no data to display!
                            showMessage("Error","Nothing found");
                            // function returned
                            return;
                        }
                        StringBuffer buffer = new StringBuffer();
                        // moves the cursor to the next result
                        while (res.moveToNext()) {
                            // indices of the columns are given her as argument
                            buffer.append("First Name : "+ res.getString(0)+"\n");
                            buffer.append("Last Name  : "+ res.getString(1)+"\n");
                            buffer.append("Phone      : "+ res.getString(2)+"\n");
                            buffer.append("Password   : "+ res.getString(3)+"\n\n");
                        }
                        // Show all data :  this function is declared downwards
                        showMessage("Data",buffer.toString());
                    }
                }
        );
    }
    public void viewAllAdmins() {
        btnviewAllAdmins.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //used the instance of DatabaseHelper
                        Cursor res = adminDb.getAllDataAdmins();
                        if(res.getCount() == 0) {
                            // show error message  if no data to display!
                            showMessage("Error","Nothing found");
                            // function returned
                            return;
                        }
                        StringBuffer buffer = new StringBuffer();
                        // moves the cursor to the next result
                        while (res.moveToNext()) {
                            // indices of the columns are given her as argument
                            buffer.append("First Name : "+ res.getString(0)+"\n");
                            buffer.append("Last Name  : "+ res.getString(1)+"\n");
                            buffer.append("Phone      : "+ res.getString(2)+"\n");
                            buffer.append("Password   : "+ res.getString(3)+"\n\n");
                        }
                        // Show all data :  this function is declared downwards
                        showMessage("Data",buffer.toString());
                    }
                }
        );
    }
    public void showMessage(String title,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // so that we can cancel it after use
        builder.setCancelable(true);
        // The heading
        builder.setTitle(title);
        // heading and massage as argument
        builder.setMessage(Message);
        // shows the dialog
        builder.show();
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




