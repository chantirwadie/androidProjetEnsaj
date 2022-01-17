package com.ensaj.examanchantirwadie.ui.gallery;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ensaj.examanchantirwadie.R;
import com.ensaj.examanchantirwadie.adapter.AdapterOccupation;
import com.ensaj.examanchantirwadie.databinding.FragmentGalleryBinding;
import com.ensaj.examanchantirwadie.model.Chrono;
import com.ensaj.examanchantirwadie.model.Occupation;
import com.ensaj.examanchantirwadie.model.SalleO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    private FragmentGalleryBinding binding;
    private ListView listView;

    RequestQueue requestQueue;
    String insertUrl ="https://fierce-ridge-76224.herokuapp.com/occupations/";
    ArrayList<Occupation> occupations;
    private Button btn;
    private Button findAll;
    private EditText find;
    String currentDate;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        requestQueue = Volley.newRequestQueue(getActivity());
        occupations =new ArrayList<Occupation>();
        listView=root.findViewById(R.id.listOc);
        btn =root.findViewById(R.id.findBtn);
        find = root.findViewById(R.id.find);
        findAll = root.findViewById(R.id.getAll);

        currentDate = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());
        find.setText(currentDate);
        fetchDataOccupation(currentDate);
        findAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                occupations.clear();
                String a= "";
                fetchDataOccupation(a);
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                occupations.clear();
                String a =find.getText().toString();
                fetchDataOccupation(a);
            }
        });

        return root;
    }
    private void fetchDataOccupation(String a) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, insertUrl+a, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

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
                listView.setAdapter(new AdapterOccupation(getActivity(),occupations));
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAG", "onErrorResponse: "+error.networkResponse);

            }
        });
        requestQueue.add(jsonArrayRequest);
    }
    private void fetchDataOccupationByDate(String dateR) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        JSONObject json = new JSONObject();
        json.put("created_at",dateR);
        jsonArray.put(json);
        Log.d("TAG", "fetchDataOccupationByDate: "+jsonArray);
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.PUT, insertUrl, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d("TAG", "onResponse: "+response);
//                for (int i=0;i<response.length();i++){
//                    JSONObject jsonObject = null;
//                    try {
//                        jsonObject = response.getJSONObject(i);
//                        String id = jsonObject.getString("_id");
//                        String dateResrvation = jsonObject.getString("created_at");
//
//                        String idSalle = jsonObject.getJSONObject("salle").getString("_id");
//                        String nameSalle = jsonObject.getJSONObject("salle").getString("name");
//                        String typeSalle = jsonObject.getJSONObject("salle").getString("type");
//
//                        SalleO salleO = new SalleO(idSalle,nameSalle,typeSalle);
//                        String idChrono = jsonObject.getJSONObject("chrono").getString("_id");
//                        String dateDebut = jsonObject.getJSONObject("chrono").getString("dateDebut");
//                        String dateFin = jsonObject.getJSONObject("chrono").getString("dateFin");
//                        Chrono chrono =  new Chrono(idChrono,dateDebut,dateFin);
//                        Occupation occupation = new Occupation(id,salleO,chrono,dateResrvation);
//                        occupations.add(occupation);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//                listView.setAdapter(new AdapterOccupation(getActivity(),occupations));
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAG", "onErrorResponse: "+error.networkResponse);

            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}