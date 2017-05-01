package com.a8fdi12.birthdayhelper;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.Manifest;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase db;
    private List<Birthday> birthdayList = new ArrayList<Birthday>();
    private ListView listView;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    //private static final int CONTACT_VIEW = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Crear base de datos
        db = openOrCreateDatabase("BirthdayHelper", Context.MODE_PRIVATE, null);
        //db.execSQL("CREATE TABLE IF NOT EXISTS Birthdays(ID integer, TipoNotif char(1), Mensaje VARCHAR(160), Telefono VARCHAR(15), FechaNacimiento VARCHAR(15), Nombre VARCHAR(128));");
        db.execSQL("CREATE TABLE IF NOT EXISTS Birthdays(ID integer, TipoNotif char(1), Mensaje VARCHAR(160), Telefono VARCHAR(15))");

        listView = (ListView) findViewById(R.id.list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Birthday oBirthday = birthdayList.get(position);

                Intent intent = new Intent(parent.getContext(),ContactView.class);
                intent.putExtra("birthday",oBirthday);
                startActivity(intent);
            }
        });

        //Comprobar permisos
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_CONTACTS)) {
                mostrarContactos();

            } else {
                //Solicitar permisos
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            }

        } else {
            obtenerContactos();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        birthdayList.clear();
        obtenerContactos();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    obtenerContactos();
                } else {

                    mostrarContactos();
                }
                return;
            }

        }
    }

    private void obtenerContactos() {
        String proyeccion[] = {ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.HAS_PHONE_NUMBER, ContactsContract.Contacts.PHOTO_ID};
        Cursor mCursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, proyeccion, "", null, "");

        if (mCursor.getCount() > 0) {
            while (mCursor.moveToNext()) {
                Birthday oBirthday = new Birthday();

                oBirthday.setId(mCursor.getInt(mCursor.getColumnIndex(ContactsContract.Contacts._ID)));
                oBirthday.setNombre(mCursor.getString(mCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));

                //Obtener la photo
                String photoId = mCursor.getString(mCursor.getColumnIndex(ContactsContract.Contacts.PHOTO_ID));

                if (photoId != null) {
                    oBirthday.setPhoto(Uri.withAppendedPath(ContactsContract.Data.CONTENT_URI, photoId));
                } else {
                    oBirthday.setPhoto(null);
                }

                //Comprobar si tiene telefono
                if (mCursor.getInt(mCursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) == 1) {
                    //Obtener los telefonos
                    Cursor phoneCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER}, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "= ?", new String[]{mCursor.getString(mCursor.getColumnIndex(ContactsContract.Contacts._ID))}, "");

                    if (phoneCursor.getCount() > 0) {

                        while (phoneCursor.moveToNext()) {

                            oBirthday.addTelefono(phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                        }
                    }

                    phoneCursor.close();

                }

                //Obtener el cumpleaños
                Cursor birthdayCursor = getContentResolver().query(ContactsContract.Data.CONTENT_URI, new String[]{ContactsContract.CommonDataKinds.Event.START_DATE}, ContactsContract.Data.CONTACT_ID + "= ? AND " + ContactsContract.Data.MIMETYPE + "= ? AND " +
                        ContactsContract.CommonDataKinds.Event.TYPE + "=" +
                        ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY, new String[]{mCursor.getString(mCursor.getColumnIndex(ContactsContract.Contacts._ID)), ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE}, null);

                if (birthdayCursor.getCount() > 0) {

                    while (birthdayCursor.moveToNext()) {
                        oBirthday.setFechaNacimiento(birthdayCursor.getString(birthdayCursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE)));
                    }

                    birthdayCursor.close();

                }

                Cursor cdb = db.rawQuery("SELECT TipoNotif, Mensaje FROM Birthdays WHERE ID = ?", new String[]{Integer.toString(oBirthday.getId())});

                if (cdb.getCount() > 0) {
                    while (cdb.moveToNext()) {
                        oBirthday.setTipoNotif(cdb.getString(0).charAt(0));
                        oBirthday.setMensaje(cdb.getString(1));
                    }
                }

                cdb.close();

                birthdayList.add(oBirthday);
            }
        }

        mCursor.close();
        mostrarContactos();
    }

    private void mostrarContactos() {
        // Sets the data behind this ListView
        listView = (ListView) findViewById(R.id.list);
        this.listView.setAdapter(new ItemAdapter(this, birthdayList));
        listView.setEmptyView(findViewById(R.id.emptyView));
    }
}
