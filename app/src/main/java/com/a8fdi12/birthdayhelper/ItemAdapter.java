package com.a8fdi12.birthdayhelper;

/**
 * Created by 8fdi12 on 27/4/17.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends BaseAdapter {

    private Context context;
    private List<Birthday> items;

    public ItemAdapter(Context context, List<Birthday> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return this.items.size();
    }

    @Override
    public Object getItem(int position) {
        return this.items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        if (convertView == null) {
            // Create a new view into the list.
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            rowView = inflater.inflate(R.layout.contacts_list_item, parent, false);
        }

        // Set data into the view.
        ImageView ivItem = (ImageView) rowView.findViewById(R.id.iv_avatar);
        TextView tvItem;

        //Obtener el contacto
        Birthday item = this.items.get(position);

        //Nombre
        tvItem = (TextView) rowView.findViewById(R.id.tv_name);
        tvItem.setText(item.getNombre());

        //Imagen
        if (item.getPhoto() != null){
            ivItem.setImageURI(item.getPhoto());
        }else{
            ivItem.setImageResource(R.mipmap.ic_default_contact_img);
        }

        //Cumpleaños
        tvItem = (TextView) rowView.findViewById(R.id.tv_cumple);
        if(item.getFechaNacimiento() != null ){
            tvItem.setText(item.getFechaNacimiento());
        }else{
            tvItem.setVisibility(View.GONE);
        }

        //Telefonos
        ArrayList<String> telefonos;
        telefonos = item.getTelefonos();
        String sTelefonos = "";

        for (Integer x = 0; x < telefonos.size(); x++){
            if(x < 1){
                sTelefonos = telefonos.get(x);
            }else{
                sTelefonos += " | " + telefonos.get(x);
            }
        }

        tvItem = (TextView) rowView.findViewById(R.id.tv_numbers);
        tvItem.setText(sTelefonos);

        //Aviso
        tvItem = (TextView) rowView.findViewById(R.id.tv_aviso);

        if (item.getTipoNotif() == 'S'){
            tvItem.setText(context.getString(R.string.s_sms));
        }else if(item.getTipoNotif() == 'N'){
            tvItem.setText(context.getString(R.string.s_noti));
        }else{
            tvItem.setText(context.getString(R.string.s_aviso));
        }

        return rowView;
    }

}