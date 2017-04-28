package com.a8fdi12.birthdayhelper;

import android.net.Uri;

import java.util.ArrayList;

/**
 * Created by 8fdi12 on 23/3/17.
 */

public class Birthday {
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
        this.photo = photo;
        this.telefonos = telefonos;
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
}
