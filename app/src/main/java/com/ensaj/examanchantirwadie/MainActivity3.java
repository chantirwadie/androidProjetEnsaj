package com.ensaj.examanchantirwadie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.ensaj.examanchantirwadie.model.Bloc;
import com.ensaj.examanchantirwadie.model.Chrono;
import com.ensaj.examanchantirwadie.model.Occupation;
import com.ensaj.examanchantirwadie.model.Salle;
import com.ensaj.examanchantirwadie.model.SalleO;
import com.google.zxing.Result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity3 extends AppCompatActivity {
    private CodeScanner mCodeScanner;
    private TextView salleName;
    private TextView salleType;
    private TextView salleBloc;
    private TextView textType;
    private TextView textBloc;
    private Button btn;
    boolean a=false;
    boolean sT =false;
    boolean cR=false;

    RequestQueue requestQueue;
    String insertUrl ="https://fierce-ridge-76224.herokuapp.com/occupations/";
    String urlChrono = "https://fierce-ridge-76224.herokuapp.com/chronos/";
    String urlSalle = "https://fierce-ridge-76224.herokuapp.com/salles/";
    String idSalle;
    ArrayList<Chrono> chronos;
    List<Salle> salles;
    ArrayList<SalleO> salleOS;
    ArrayList<Occupation> occupations;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        salleName = findViewById(R.id.salleName);
        salleType = findViewById(R.id.type);
        salleBloc = findViewById(R.id.bloc);
        textBloc = findViewById(R.id.typeBloc);
        textType = findViewById(R.id.typeSalle);
        btn = findViewById(R.id.send);

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        occupations =new ArrayList<Occupation>();
        chronos = new ArrayList<Chrono>();
        salles =new ArrayList<Salle>();
        fetchDataChronos();
        fetchDataSalles();
        fetchDataOccupation();
        Log.d("TAG", "onCreate: "+salles);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @SuppressLint("WrongConstant")
                    @Override
                    public void run() {
                        idSalle=result.getText();
                        btn.setVisibility(1);
                        fetchDataSalles();
                        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
                        for (Chrono c:chronos){
                            String id = c.getId();
                            String startTime = c.getDateDebut();
                            String endTime = c.getDateFin();
                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                            Date d1 = null;
                            Date d2 = null;
                            try {
                                d1 = sdf.parse(startTime);
                                d2 = sdf.parse(endTime);

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            int value =currentTime.compareTo(startTime);
                            int value2 =currentTime.compareTo(endTime);
                            Log.d("TAG", "run: "+value+" "+value2);
                            if (value>=0){
                                if (value2<=0){
                                    btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Log.d("TAG", "onActivityResult: "+occupations);
                                            if(occupations.isEmpty()){
                                                try {
                                                    envoiDonner(idSalle,id);
                                                    refrsh();

                                                    Toast.makeText(getApplicationContext(), "la salle est Bien Occupeé !!", Toast.LENGTH_SHORT).show();

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }else {
                                                Log.d("TAG", "onActivityResult: not empty ");
                                                for (Occupation o:occupations){
                                                    String currentDate = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(new Date());
                                                    Log.d("TAG", "onActivityResult: "+currentDate+" "+o.getDateResrvation());
                                                    if(currentDate.compareTo(o.getDateResrvation()) == 0) {
                                                        Log.d("TAG", "onActivityResult: "+currentDate+" "+o.getDateResrvation());
                                                        a=true;
                                                    } else{
                                                        a=false;
                                                    }

                                                    if (idSalle.equals(o.getSalle().getId())){
                                                        sT=true;
                                                    }else {
                                                        sT=false;
                                                    }
                                                    if (id.equals(o.getChrono().getId())){
                                                        cR=true;
                                                    }else{
                                                        cR=false;
                                                    }
                                                }
                                                Log.d("TAG", "onActivityResult: "+a+" "+sT+" "+cR);
                                                if (a==true && sT==true && cR==true){
                                                    refrsh();
                                                    Toast.makeText(getApplicationContext(), "la salle est Deja Occuper", Toast.LENGTH_SHORT).show();

                                                }else{
                                                    AlertDialog.Builder alertDialogBuilder= new AlertDialog.Builder(getApplicationContext());
                                                    final AlertDialog.Builder alertDialogBuilder1 = new AlertDialog.Builder(getApplicationContext());

                                                    alertDialogBuilder.setNeutralButton("Supprimer", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {




                                                        }
                                                    });

                                                    alertDialogBuilder.setNegativeButton("Modifier", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {


                                                        }
                                                    });

                                                    AlertDialog alertDialog1 = alertDialogBuilder.create();
                                                    alertDialog1.show();
                                                }
                                                }


                                            }

                                    });

                                }
                            }
                        }


                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refrsh();
            }
        });
    }
    private void refrsh(){
        mCodeScanner.startPreview();
        fetchDataChronos();
        fetchDataSalles();
        fetchDataOccupation();
    }
    private void fetchDataSalles() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlSalle, null, new Response.Listener<JSONArray>() {
            @SuppressLint("WrongConstant")
            @Override
            public void onResponse(JSONArray response) {
                salles.clear();
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
                        salles.add(salle);
                        Log.d("TAG", "onResponse: "+idSalle);
                        for (Salle s:salles){
                            if (s.getId().equals(idSalle)){
                                textType.setVisibility(1);
                                textBloc.setVisibility(1);
                                salleName.setText("La Salle " + s.getName());
                                salleType.setVisibility(1);
                                salleType.setText(s.getType());
                                salleBloc.setVisibility(1);
                                salleBloc.setText(s.getBloc().getName());
                            }
                        }
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
    private void fetchDataOccupation() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, insertUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                salles.clear();
                Log.d("TAG", "onResponse: "+response);
                for (int i=0;i<response.length();i++){
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = response.getJSONObject(i);
                        String id = jsonObject.getString("_id");
                        String dateResrvation = jsonObject.getString("created_at");

                        String idSalle = jsonObject.getJSONObject("salle").getString("_id");
                        String nameSalle = jsonObject.getJSONObject("salle").getString("name");
                        String typeSalle = jsonObject.getJSONObject("salle").getString("type");

                        SalleO salleO = new SalleO(idSalle,nameSalle,typeSalle);
                        String idChrono = jsonObject.getJSONObject("chrono").getString("_id");
                        String dateDebut = jsonObject.getJSONObject("chrono").getString("dateDebut");
                        String dateFin = jsonObject.getJSONObject("chrono").getString("dateFin");
                        Chrono chrono =  new Chrono(idChrono,dateDebut,dateFin);
                        Occupation occupation = new Occupation(id,salleO,chrono,dateResrvation);
                        occupations.add(occupation);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.d("azz", "onResponse: "+occupations);



            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAG", "onErrorResponse: "+error.networkResponse);

            }
        });
        requestQueue.add(jsonArrayRequest);
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
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
                Log.d("azer", "onResponse: "+chronos);




            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAG", "onErrorResponse: "+error.networkResponse);

            }
        });
        requestQueue.add(jsonArrayRequest);
    }
    private void envoiDonner(String idSalle,String idChrono) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("salle", idSalle);
        json.put("chrono", idChrono);
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
                Toast.makeText(getApplicationContext(), "Occupation est bien Ajouter", Toast.LENGTH_SHORT).show();

                VolleyLog.e("Error: ", error.getMessage());
            }
        });
        requestQueue.add(req);
    }
    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
}