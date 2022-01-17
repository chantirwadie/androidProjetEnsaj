package com.ensaj.examanchantirwadie.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ensaj.examanchantirwadie.R;
import com.ensaj.examanchantirwadie.model.Occupation;

import java.util.ArrayList;

public class AdapterOccupation extends BaseAdapter {
    private LayoutInflater li;
    ArrayList<Occupation> occupations;

    public AdapterOccupation(Activity activity, ArrayList<Occupation> occupations) {
        this.occupations =occupations;
        li = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
    }

    @Override
    public int getCount() {
        return  occupations.size();
    }

    @Override
    public Object getItem(int i) {
        return occupations.get(i);
    }

    @Override
    public long getItemId(int i) {
        return  i+1;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null){
            view = li.inflate(R.layout.item_occupation,null);
        }
        TextView nom  = view.findViewById(R.id.nomSalle);
        TextView creneaux = view.findViewById(R.id.creneau);
        TextView dateR = view.findViewById(R.id.dateR);
        nom.setText(occupations.get(i).getSalle().getName());
        creneaux.setText(occupations.get(i).getChrono().getDateDebut()+"-"+occupations.get(i).getChrono().getDateFin());
        dateR.setText(occupations.get(i).getDateResrvation());
        notifyDataSetChanged();
        return view;
    }
}
