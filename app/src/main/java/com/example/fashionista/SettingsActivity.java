package com.example.fashionista;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fashionista.Prevalent.Prevalent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;


import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import de.hdodenhof.circleimageview.CircleImageView;

// THIS CLASS IS FOR THE MENU OPTIONS OF HOME ACTIVITY
public class SettingsActivity extends AppCompatActivity{

    final int REQUEST_CODE_GALLERY = 999;
    private CircleImageView profileImageView;
    // FOR THE USER INFO FIELDS TO UPDATE IN SETTINGS
    private EditText firstNameEditText,lastNameEditText, userPhoneEditText, addressEditText;
    // SETTING BUTTONS
    private TextView profileChangeTextBtn,  closeTextBtn, saveTextButton;
    private Button securityQuestionBtn;
    // TO GET THE PICTURE STORED IN FIREBASE STORAGE  ALREADY
    private Uri uri;
    private ImageView profilePic;
    private  Bitmap bitmap;
    // FOR USERS DATABASE
    DataBaseHelper userDb;
    private Uri imageUri;
    private String myUrl = "";
    private StorageTask uploadTask;
    private StorageReference storageProfilePictureRef;
    private String checker = "";


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        // VARIABLE INITIALISATION
        storageProfilePictureRef = FirebaseStorage.getInstance().getReference().child("Profile pictures");
        userPhoneEditText = (EditText) findViewById(R.id.settings_phone_number);
        firstNameEditText = (EditText) findViewById(R.id.settings_first_name);
        lastNameEditText = (EditText) findViewById(R.id.settings_last_name);
        addressEditText = (EditText) findViewById(R.id.settings_address);
        profileChangeTextBtn = (TextView)findViewById(R.id.profile_image_change_btn);
        profileImageView = (CircleImageView) findViewById(R.id.settings_profile_image);
        securityQuestionBtn = findViewById(R.id.security_questions_btn);

        // for two top buttons - CLOSE AND UPDATE
        closeTextBtn = (TextView) findViewById(R.id.close_settings_btn);
        saveTextButton = (TextView) findViewById(R.id.update_account_settings_btn);

        // CONSTRUCTOR TO CREATE USER DATABASE USING USER DATABASE HELPER CLASS
        userDb = new DataBaseHelper(this);

        // FUNCTION TO DISPLAY ALL THE PROFILE INFORMATION
        userInfoDisplay(userPhoneEditText,firstNameEditText,lastNameEditText, addressEditText);

        // CLOSE BUTTON
        closeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                // TO END THE CURRENT TASK AND GO BACK TO THE PREVIOUS SCREEN
                finish();
            }
        });
        //  BUTTON TO CHANGE THE PROFILE PICTURE
        profileChangeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                checker = "clicked";
                CropImage.activity(imageUri)
                        .setAspectRatio(1, 1)
                        .start(SettingsActivity.this);

            }
        });
        saveTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                if (checker.equals("clicked")){
                    userInfoSaved();
                }
                else{
                    updateUserInfo();
                }
            }
        });
        securityQuestionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(SettingsActivity.this, ResetPasswordActivity.class);
                intent.putExtra("check", "settings");
                startActivity(intent);
            }
        });
    }
    private void updateUserInfo(){
        String oldphone = Prevalent.currentOnlineUser.getPhone();
        String phone = userPhoneEditText.getText().toString();
        String fname = firstNameEditText.getText().toString();
        String lname = lastNameEditText.getText().toString();
        String address = addressEditText.getText().toString();
        userDb.updateDataUsers(oldphone, phone, fname, lname, address);
        startActivity(new Intent(SettingsActivity.this, HomeActivity.class));
        Toast.makeText(SettingsActivity.this, "Profile Info updated successfully.", Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE  &&  resultCode==RESULT_OK  &&  data!=null){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            profileImageView.setImageURI(imageUri);
        }
        else{
            Toast.makeText(this, "Error, Try Again.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SettingsActivity.this, SettingsActivity.class));
            finish();
        }
    }
    private void userInfoSaved(){
        if (TextUtils.isEmpty(firstNameEditText.getText().toString())){
            Toast.makeText(this, "First Name is mandatory!", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(lastNameEditText.getText().toString())){
            Toast.makeText(this, "Last Name is address!", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(addressEditText.getText().toString())){
            Toast.makeText(this, "Please enter your address!", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(userPhoneEditText.getText().toString())){
            Toast.makeText(this, "Phone Number is mandatory!", Toast.LENGTH_SHORT).show();
        }
        else if(checker.equals("clicked")){
            uploadImage();
        }
    }
    private void uploadImage(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Please wait, while we are updating your account information");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        if (imageUri != null){
            final StorageReference fileRef = storageProfilePictureRef.child(Prevalent.currentOnlineUser.getPhone() + ".jpg");
            uploadTask = fileRef.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception{
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task){
                    if (task.isSuccessful()){
                        Uri downloadUrl = task.getResult();
                        myUrl = downloadUrl.toString();
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap. put("image", myUrl);
                        ref.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(userMap);
                        Prevalent.currentOnlineUser.setProfilePic("true");
                        progressDialog.dismiss();
                        startActivity(new Intent(SettingsActivity.this, HomeActivity.class));
                        Toast.makeText(SettingsActivity.this, "Profile Info updated successfully!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(SettingsActivity.this, "Error.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else
        {
            Toast.makeText(this, "Image is not selected.", Toast.LENGTH_SHORT).show();
        }
    }
    private void userInfoDisplay(final EditText userPhoneEditText,final EditText firstNameEditText,final EditText lastNameEditText, final EditText addressEditText){
        // RETRIEVING USER DATA FROM SQLITE DATABASE
        String phone = Prevalent.currentOnlineUser.getPhone();
        String lname = Prevalent.currentOnlineUser.getLName();
        String fname = Prevalent.currentOnlineUser.getFName();
        String address = userDb.getUserAddress(phone);
        if (address != null && !address.equals("null"))
            addressEditText.setText(address);

        userPhoneEditText.setText(phone);
        firstNameEditText.setText(fname);
        lastNameEditText.setText(lname);
        System.out.println("Address = "+address);
        // TO DISPLAY PROFILE PIC - PROFILE PIC OF USER STORED IN FIREBASE FOR EASE
        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone());
        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                if (dataSnapshot.exists()){
                    if (dataSnapshot.child("image").exists()){
                        String image = dataSnapshot.child("image").getValue().toString();
                        Picasso.get().load(image).into(profileImageView);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

}
