package com.example.fashionista;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.drawable.AnimationDrawable;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import io.paperdb.Paper;
import android.database.Cursor;
import com.example.fashionista.Prevalent.Prevalent;
import static com.example.fashionista.Prevalent.Prevalent.UserPasswordKey;
import static com.example.fashionista.Prevalent.Prevalent.UserPhoneKey;


public class CustomerLogin extends AppCompatActivity {

    //  ***********************************DECLARATIONS********************************************
    // CUSTOMER CAN ACCESS ONLY CUSTOMER DATABASE
    // FOR USERS DATABASE
    DataBaseHelper myDb;

    //  FOR BACKGROUND
    private ConstraintLayout constraintLayout;
    private AnimationDrawable animationDrawable;

    // FOR BUTTONS
    private Button opensignupForm ;
    private Button LoginBtn;
    Button btnviewAllUsers;

    // FOR EDIT TEXTS
    private EditText loginPhone,loginPassword;
    private TextView forgetPasswordLink;

    // FOR PROGRESS DIALOG BAR
    private ProgressDialog loadingBar;

    // FOR CHECKBOX
    private CheckBox chRememberMe;

    // *********************************************************************************************
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //---------------------------------BY DEFAULT-----------------------------------------------
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_login);
        //getSupportActionBar().hide();
        // ------------------------FOR BACKGROUND ANIMATION---------------------------------------
        // init constraintLayout
        constraintLayout = (ConstraintLayout) findViewById(R.id.constraintLayout);
        // initializing animation drawable by getting background from constraint layout
        animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        // setting enter fade animation duration to 5 seconds
        animationDrawable.setEnterFadeDuration(5000);
        // setting exit fade animation duration to 2 seconds
        animationDrawable.setExitFadeDuration(3000);
        //----------------------------------------------------------------------------------------
        // CONSTRUCTOR TO CREATE USER DATABASE USING USER DATABASE HELPER CLASS
        myDb = new DataBaseHelper(this);

        // TO OPEN THE SIGN UP PAGE ON CLICKING SIGN UP BUTTON ON CUSTOMER LOGIN PAGE
        opensignupForm =  (Button)findViewById(R.id.customerButton);
        opensignupForm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                openSignupForm();
            }
        });
        // CUSTOMER LOGIN ACTIVITY
        loadingBar    = new ProgressDialog(this);
        LoginBtn      = (Button)findViewById(R.id.loginButton);
        loginPhone    = (EditText)findViewById(R.id.phoneLogin);
        loginPassword = (EditText)findViewById(R.id.passLogin);
        forgetPasswordLink = (TextView)findViewById(R.id.forgotPassword);
        LoginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                CustomerLogin();
            }
        });
        // --------------BUTTONS INITIALISED TO PRINT ALL USER & ADMIN DATABASE DATA----------------
        btnviewAllUsers  = (Button)findViewById(R.id.viewAllUsers);
        viewAllUsers();
        //------------------------------------------------------------------------------------------

        //******************************REMEMBER ME FUNCTION OF THE APP*****************************
        chRememberMe = (CheckBox)findViewById(R.id.RememberCheckbox);
        Paper.init(this);
        String UserPhoneKey = Paper.book().read(Prevalent.UserPhoneKey);
        String UserPasswordKey = Paper.book().read(Prevalent.UserPasswordKey);
        if (UserPhoneKey != null && UserPasswordKey != null){
            if (!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordKey)){
                // CALLING ALLOW ACCESS FUNCTION WHEN REMEMBER ME CHECKBOX IS TICKED I.E DIRECT LOGGING IN
                AllowDirectAccess(UserPhoneKey,UserPasswordKey);
                loadingBar.setTitle("Already Logged in!");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
                loadingBar.dismiss();
                }
        }
        forgetPasswordLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(CustomerLogin.this, ResetPasswordActivity.class);
                intent.putExtra("check", "login");
                startActivity(intent);
            }
        });
    }
    //****************************METHODS FOR THIS CLASS********************************************
    private void CustomerLogin(){
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
            AllowAccessToUserAccount(phone, password);
        }
    }
    //-------------------------------------------------------------------------------------------------------------------------------
    // FOR DIRECT LOGIN WHEN REMEMBER ME WAS CHECKED ON LAST LOGIN
    private void AllowDirectAccess(final String phone, final String password) {
        if (!(myDb.UserPhoneExists(phone))){
            //Toast.makeText(MainActivity.this,"Database = "+parentDbName,Toast.LENGTH_SHORT).show();
            Toast.makeText(CustomerLogin.this,"Account with this phone number does not exist!",Toast.LENGTH_SHORT).show();
            loadingBar.dismiss();
            Toast.makeText(CustomerLogin.this,"Please create a new account..",Toast.LENGTH_SHORT).show();
            loadingBar.dismiss();
        }
        else{
            if (!(myDb.UserPasswordCorrect(phone,password))){
                loadingBar.dismiss();
                Toast.makeText(CustomerLogin.this,"Sorry! Incorrect Password!",Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(CustomerLogin.this,"Welcome "+myDb.getUserFName(phone).toString()+" !",Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
                String fn = myDb.getUserFName(phone);
                System.out.println(fn+" ");
                String ln = myDb.getUserLName(phone);
                System.out.println(ln);
                Customers c = new Customers(fn,ln,phone,password,null,null,null,null);
                try{
                    Intent intent = new Intent(CustomerLogin.this, HomeActivity.class);
                    Prevalent.currentOnlineUser =c;
                    startActivity(intent);
                }catch(Exception e ){
                    e.printStackTrace();
                }
            }
        }

    }
    // FOR NORMAL LOGIN USING CREDENTIALS WHEN REMEMBER ME WASN'T CHECKED AT THE TIME OF LAST LOGIN
    private void AllowAccessToUserAccount(final String phone, final String password) {
        if (chRememberMe.isChecked()){
            Paper.book().write(UserPhoneKey,phone);
            Paper.book().write(UserPasswordKey ,password);
        }
        Boolean phoneExists = myDb.UserPhoneExists(phone);
        System.out.println("Phone exists  = "+phoneExists);
        if (phoneExists){
            Boolean passcorrect = myDb.UserPasswordCorrect(phone, password);
            System.out.println("Password correct = "+ passcorrect);
            if (passcorrect){
                if (myDb.getUserFName(phone)!=null)
                    Toast.makeText(CustomerLogin.this,"Welcome "+myDb.getUserFName(phone).toString()+" !",Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    String fn = myDb.getUserFName(phone);
                    System.out.println(fn+" ");
                    String ln = myDb.getUserLName(phone);
                    System.out.println(ln);
                    Customers c = new Customers(fn,ln,phone,password,null,null,null,null);
                try{

                    Intent intent = new Intent(CustomerLogin.this, HomeActivity.class);
                    Prevalent.currentOnlineUser = c;
                    startActivity(intent);
                }catch(Exception e ){
                    e.printStackTrace();
                }
            }
            else{
                Toast.makeText(CustomerLogin.this,"Sorry! Incorrect Password!",Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }
        else{
            //Toast.makeText(MainActivity.this,"Database = "+parentDbName,Toast.LENGTH_SHORT).show();
            Toast.makeText(CustomerLogin.this,"Account with this phone number does not exist!",Toast.LENGTH_SHORT).show();
            loadingBar.dismiss();
            Toast.makeText(CustomerLogin.this,"Please create a new account..",Toast.LENGTH_SHORT).show();
            loadingBar.dismiss();
        }
    }

    // FUNCTIONS TO PRINT ALL THE DATABASE INFO INSERTED TILL NOW
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
                            // indices of the columns are given here as argument
                            if (res.getString(2) == null)
                                break;
                            buffer.append("First Name : "+ res.getString(0)+"\n");
                            buffer.append("Last Name  : "+ res.getString(1)+"\n");
                            buffer.append("Phone      : "+ res.getString(2)+"\n");
                            buffer.append("Password   : "+ res.getString(3)+"\n");
                            //if (res.getString(4) == null)
                            String addr = res.getString(4);
                            if (addr==null || addr.equals("null"))
                                buffer.append("Address  : Not Updated\n");
                            else
                                buffer.append("Address  : "+ res.getString(4)+"\n");
                            String s1 =  res.getString(6);
                            String s2 =  res.getString(7);
                            if (s1!=null && s2!=null){
                                buffer.append("Security Answer1 : " + s1 + "\n");
                                buffer.append("Security Answer2 : " + s2 + "\n");
                            }
                            buffer.append("\n\n");
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
    // FUNCTION TO OPEN SIGN UP ACTIVITY USING INTENT
    public void openSignupForm(){
        Intent intent = new Intent(this, SignupForm.class);
        startActivity(intent);
    }
    public void openHomepage(){
        Intent intent = new Intent(this, HomePage.class);
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




