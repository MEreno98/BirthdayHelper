package com.a8fdi12.birthdayhelper;

import android.Manifest;
import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class ContactView extends AppCompatActivity {

    private Birthday oBirthday;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_view);

        Intent intent = getIntent();
        oBirthday = (Birthday) intent.getParcelableExtra("birthday");

        //Imagen
        ImageView ivItem = (ImageView) findViewById(R.id.iv_avatar);
        TextView tvItem = (TextView) findViewById(R.id.tv_nombre);

        if (oBirthday.getPhoto() != null){
            ivItem.setImageURI(oBirthday.getPhoto());
        }else{
            ivItem.setImageResource(R.mipmap.ic_default_contact_img);
        }

        //Nombre
        tvItem.setText(oBirthday.getNombre());

        //Telefonos
        Spinner sp_telefonos = (Spinner) findViewById(R.id.sp_telefonos);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, oBirthday.getTelefonos());
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_telefonos.setAdapter(spinnerArrayAdapter);
        
        for (int x = 0; x < oBirthday.getTelefonos().size(); x++){
            if (oBirthday.getTelefono(x) == oBirthday.getSelectTel()){
                sp_telefonos.setSelection(x);
            }
        }

        //Fecha de Nacimiento
        tvItem = (TextView) findViewById(R.id.tv_cumple);


        if (oBirthday.getFechaNacimiento() != null){
            tvItem.setText(oBirthday.getFechaNacimiento());
        }else {
            tvItem.setVisibility(View.GONE);
        }
    }

    public void checkEnviarMensaje(View v){
        CheckBox cb = (CheckBox) v;
        EditText mensaje = (EditText) findViewById(R.id.et_mensaje);

        if (cb.isChecked()){

            //Comprobar permisos
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {


                } else {
                    //Solicitar permisos
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.SEND_SMS},
                            MY_PERMISSIONS_REQUEST_SEND_SMS);
                }

            } else {
                mensaje.setText(oBirthday.getMensaje());
                mensaje.setEnabled(true);
            }

        }else{
            mensaje.setText("");
            mensaje.setEnabled(false);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    EditText mensaje = (EditText) findViewById(R.id.et_mensaje);
                    mensaje.setText(oBirthday.getMensaje());
                    mensaje.setEnabled(true);
                } else {

                }
                return;
            }

        }
    }
}
