package com.example.fashionista;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import io.paperdb.Paper;

public class HomePage extends AppCompatActivity {
    private ImageButton logoutbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        /*logoutbtn =  (ImageButton)findViewById(R.id.logoutButton);
        logoutbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // we destroy th record stored in paper for remember me when the user clicks the logout button
                Paper.book().destroy();
                Intent intent = new Intent(HomePage.this, MainActivity.class);
                startActivity(intent);
            }
        });*/
    }
}
