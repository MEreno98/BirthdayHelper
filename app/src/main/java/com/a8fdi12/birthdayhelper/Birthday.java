package com.a8fdi12.birthdayhelper;

import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.renderscript.Sampler;

import java.io.Serializable;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;

/**
 * Created by 8fdi12 on 23/3/17.
 */

public class Birthday implements Parcelable{
    private int id;
    private char tipoNotif;
    private String mensaje;
    private String selectTel;
    private ArrayList<String> telefonos;
    private String fechaNacimiento;
    private String nombre;
    private Uri photo;

    public Birthday() {
        this.telefonos = new ArrayList<String>();
    }

    public Birthday(int id, char tipoNotif, String mensaje, String selectTel, String fechaNacimiento, String nombre, Uri photo, ArrayList<String> telefonos) {
        this.id = id;
        this.tipoNotif = tipoNotif;
        this.mensaje = mensaje;
        this.selectTel = selectTel;
        this.fechaNacimiento = fechaNacimiento;
        this.nombre = nombre;
        this.telefonos = telefonos;
        this.photo = photo;
    }

    public Birthday(Parcel in){
        this.id = in.readInt();
        this.tipoNotif = in.readString().charAt(0);
        this.mensaje = in.readString();
        this.selectTel = in.readString();
        this.fechaNacimiento = in.readString();
        this.nombre = in.readString();
        this.telefonos = in.createStringArrayList();

        String path = in.readString();
        if (path != null){
            this.photo = Uri.parse(path);
        }else {
            this.photo = null;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public char getTipoNotif() {
        return tipoNotif;
    }

    public void setTipoNotif(char tipoNotif) {
        this.tipoNotif = tipoNotif;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getSelectTel() {
        return selectTel;
    }

    public void setTelefono(String selectTel) {
        this.selectTel = selectTel;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Uri getPhoto() {
        return photo;
    }

    public void setPhoto(Uri photo) {
        this.photo = photo;
    }

    public String getTelefono(int index){
        return telefonos.get(index);
    }

    public ArrayList<String> getTelefonos(){
        return telefonos;
    }

    public void addTelefono(String telefono){
        telefonos.add(telefono);
    }

    public static final Parcelable.Creator<Birthday> CREATOR = new Parcelable.Creator<Birthday>(){
        @Override
        public Birthday createFromParcel(Parcel source) {
            return new Birthday(source);
        }

        @Override
        public Birthday[] newArray(int size) {
            return new Birthday[size];
        }
    };

    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(Character.toString(tipoNotif));
        dest.writeString(mensaje);
        dest.writeString(selectTel);
        dest.writeString(fechaNacimiento);
        dest.writeString(nombre);
        dest.writeStringList(telefonos);

        if (photo != null){
            dest.writeString(photo.toString());
        }else{
            dest.writeString(null);
        }

    }
}
