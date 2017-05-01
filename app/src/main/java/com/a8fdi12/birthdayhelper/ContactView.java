package com.a8fdi12.birthdayhelper;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class ContactView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_view);

        Intent intent = getIntent();
        Birthday oBirthday = (Birthday) intent.getParcelableExtra("birthday");

        //Imagen
        ImageView ivItem = (ImageView) findViewById(R.id.iv_avatar);

        if (oBirthday.getPhoto() != null){
            ivItem.setImageURI(oBirthday.getPhoto());
        }else{
            ivItem.setImageResource(R.mipmap.ic_default_contact_img);
        }
    }
}
