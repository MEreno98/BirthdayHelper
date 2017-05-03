package com.a8fdi12.birthdayhelper;

import android.Manifest;
import android.app.ActionBar;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
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
import android.widget.Toast;

public class ContactView extends AppCompatActivity {

    SQLiteDatabase db;
    private Birthday oBirthday;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_view);

        Intent intent = getIntent();
        oBirthday = (Birthday) intent.getParcelableExtra("birthday");

        db = openOrCreateDatabase("BirthdayHelper", Context.MODE_PRIVATE, null);

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
        System.out.println(oBirthday.getTelefonos());
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, oBirthday.getTelefonos());
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_telefonos.setAdapter(spinnerArrayAdapter);

        /*for (int x = 0; x < oBirthday.getTelefonos().size(); x++){
            if (oBirthday.getTelefono(x) == oBirthday.getSelectTel()){
                System.out.println(oBirthday.getSelectTel());
                sp_telefonos.setSelection(x);
            }
        }*/

        //Fecha de Nacimiento
        tvItem = (TextView) findViewById(R.id.tv_cumple);


        if (oBirthday.getFechaNacimiento() != null){
            tvItem.setText(oBirthday.getFechaNacimiento());
        }else {
            tvItem.setVisibility(View.GONE);
        }

        //Tipo Noti y Mensaje
        CheckBox cb = (CheckBox) findViewById(R.id.cb_esms);
        tvItem = (EditText) findViewById(R.id.et_mensaje);

        if(oBirthday.getTipoNotif() == 'S'){
            cb.setChecked(true);
            tvItem.setEnabled(true);
            tvItem.setText(oBirthday.getMensaje());
        }else{
            cb.setChecked(false);
            tvItem.setEnabled(false);
            tvItem.setText("");
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

    public void onGuardar(View v){
        Cursor c = db.rawQuery("Select * FROM Birthdays WHERE ID =?",new String[]{Integer.toString(oBirthday.getId())});
        long resultado;

        if(c.getCount() > 0){
            ContentValues nuevoRegistro = new ContentValues();

            CheckBox cb = (CheckBox) findViewById(R.id.cb_esms);

            if(cb.isChecked()){
                EditText et_mensaje = (EditText) findViewById(R.id.et_mensaje);
                nuevoRegistro.put("TipoNotif","S");
                nuevoRegistro.put("Mensaje",et_mensaje.getText().toString());
            }else{
                nuevoRegistro.put("TipoNotif","N");
            }

            Spinner sp_telefono = (Spinner) findViewById(R.id.sp_telefonos);
            nuevoRegistro.put("Telefono",sp_telefono.getSelectedItem().toString());

           resultado = db.update("Birthdays", nuevoRegistro, "ID =?",new String[]{Integer.toString(oBirthday.getId())});
        }else{
            ContentValues nuevoRegistro = new ContentValues();
            nuevoRegistro.put("ID",oBirthday.getId());

            CheckBox cb = (CheckBox) findViewById(R.id.cb_esms);

            if(cb.isChecked()){
                EditText et_mensaje = (EditText) findViewById(R.id.et_mensaje);
                nuevoRegistro.put("TipoNotif","S");
                nuevoRegistro.put("Mensaje",et_mensaje.getText().toString());
            }else{
                nuevoRegistro.put("TipoNotif","N");
            }

            Spinner sp_telefono = (Spinner) findViewById(R.id.sp_telefonos);
            nuevoRegistro.put("Telefono",sp_telefono.getSelectedItem().toString());

            resultado = db.insert("Birthdays", null, nuevoRegistro);
        }

        if (resultado != -1){
            Toast.makeText(this,getString(R.string.success_contacto),Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this,getString(R.string.error_contacto),Toast.LENGTH_LONG).show();
        }

        c.close();
    }

    public void onVerContacto(View v){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("_ID:"+oBirthday.getId()));
        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(oBirthday.getId()));
        intent.setData(uri);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
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
