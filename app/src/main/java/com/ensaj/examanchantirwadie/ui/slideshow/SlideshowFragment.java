package com.ensaj.examanchantirwadie.ui.slideshow;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ensaj.examanchantirwadie.MainActivity;
import com.ensaj.examanchantirwadie.MainActivity2;
import com.ensaj.examanchantirwadie.MainActivity3;
import com.ensaj.examanchantirwadie.R;
import com.ensaj.examanchantirwadie.databinding.FragmentSlideshowBinding;
import com.ensaj.examanchantirwadie.model.Bloc;
import com.ensaj.examanchantirwadie.model.Chrono;
import com.ensaj.examanchantirwadie.model.Occupation;
import com.ensaj.examanchantirwadie.model.Salle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    private FragmentSlideshowBinding binding;
    RequestQueue requestQueue;
    String urlChrono = "https://fierce-ridge-76224.herokuapp.com/chronos/";
    String urlSalle = "https://fierce-ridge-76224.herokuapp.com/salles/";
    String insertUrl ="https://fierce-ridge-76224.herokuapp.com/occupations/";
    ArrayList<Chrono> chronos;
    ArrayList<String> chronosList;
    ArrayList<String> chronosIDList;
    ArrayList<String> salleIDList;

    ArrayList<String> sallesList;
    private Spinner sp;
    private Spinner sp2;
    private Button btn;

    String date;
    String idChrono;
    String idSalle;

    private static final String TAG = "MainActivity";

    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        requestQueue = Volley.newRequestQueue(getActivity());
        chronosList = new ArrayList<String>();
        chronosIDList = new ArrayList<String>();
        salleIDList = new ArrayList<String>();

        sallesList= new ArrayList<String>();
        chronos =new ArrayList<Chrono>();

        mDisplayDate =  root.findViewById(R.id.tvDate);
        sp = root.findViewById(R.id.spChrono);
        sp2 = root.findViewById(R.id.spSalle);
        btn = root.findViewById(R.id.rev);

        fetchDataChronos();
        fetchDataSalles();

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        getActivity(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d("TAG", "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                date = "0"+month + "/0" + day + "/" + year;
                mDisplayDate.setText(date);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int j=sp.getSelectedItemPosition();
                        int i=sp2.getSelectedItemPosition();
                        idChrono = chronosIDList.get(j);
                        idSalle = salleIDList.get(i);

                        Log.d("TAG", "onClick: "+idChrono+" "+idSalle+" "+date);
                        try {
                            envoiDonner(idSalle,idChrono,date);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });
            }
        };




        return root;
    }
    private void fetchDataChronos() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlChrono, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("TAG", "onResponse: "+response);
                for (int i=0;i<response.length();i++){
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = response.getJSONObject(i);
                        String id = jsonObject.getString("_id");
                        String dateDebut = jsonObject.getString("dateDebut");
                        String dateFin = jsonObject.getString("dateFin");

                        Chrono chrono = new Chrono(id,dateDebut,dateFin);
                        chronos.add(chrono);
                        chronosIDList.add(chrono.getId());
                        chronosList.add(chrono.getDateDebut()+"-"+chrono.getDateFin());

                        sp.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, chronosList));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAG", "onErrorResponse: "+error.networkResponse);

            }
        });
        requestQueue.add(jsonArrayRequest);
    }
    private void fetchDataSalles() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlSalle, null, new Response.Listener<JSONArray>() {
            @SuppressLint("WrongConstant")
            @Override
            public void onResponse(JSONArray response) {
                Log.d("TAG", "onResponse: "+response);
                for (int i=0;i<response.length();i++){
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = response.getJSONObject(i);
                        String id = jsonObject.getString("_id");
                        String name = jsonObject.getString("name");
                        String type = jsonObject.getString("type");
                        String idBloc = jsonObject.getJSONObject("bloc").getString("_id");
                        String nameBloc = jsonObject.getJSONObject("bloc").getString("name");
                        Bloc b = new Bloc(idBloc,nameBloc);
                        Salle salle = new Salle(id,name,type,b);
                        sallesList.add(salle.getName());
                        salleIDList.add(salle.getId());
                        sp2.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, sallesList));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }



            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAG", "onErrorResponse: "+error.networkResponse);

            }
        });
        requestQueue.add(jsonArrayRequest);
    }
    private void envoiDonner(String idSalle,String idChrono,String date) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("salle", idSalle);
        json.put("chrono", idChrono);
        json.put("created_at", date);
        Log.d("rrrr", "envoiDonner: "+json);
        JsonObjectRequest req = new JsonObjectRequest(insertUrl, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v("Response:%n %s", response.toString(4));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Occupation est bien Ajouter", Toast.LENGTH_SHORT).show();

                VolleyLog.e("Error: ", error.getMessage());
            }
        });
        requestQueue.add(req);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}