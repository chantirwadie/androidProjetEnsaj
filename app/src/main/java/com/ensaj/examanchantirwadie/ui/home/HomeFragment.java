package com.ensaj.examanchantirwadie.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.charts.Pie;
import com.anychart.core.cartesian.series.Bar;
import com.ensaj.examanchantirwadie.MainActivity2;
import com.ensaj.examanchantirwadie.R;
import com.ensaj.examanchantirwadie.databinding.FragmentHomeBinding;
import com.ensaj.examanchantirwadie.model.Chrono;
import com.ensaj.examanchantirwadie.model.Occupation;
import com.ensaj.examanchantirwadie.model.SalleO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private Button btn;
    View root;
    ArrayList<String> fullArray;
    ArrayList<String> dates;
    ArrayList<Integer> counts;
    RequestQueue requestQueue;
    String url = "https://fierce-ridge-76224.herokuapp.com/occupations/count/c";
    Cartesian pie;
    AnyChartView anyChartView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        requestQueue = Volley.newRequestQueue(getActivity());
        pie= AnyChart.bar();
        anyChartView = (AnyChartView) root.findViewById(R.id.any_chart_view);


        fetchCount();

        btn =root.findViewById(R.id.scan);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MainActivity2.class);
                startActivity(intent);

            }
        });

        return root;
    }
    private void fetchCount() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("TAG", "onResponse: "+response);
                dates = new ArrayList<String>();
                counts =new ArrayList<Integer>();
                for (int i=0;i<response.length();i++){
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = response.getJSONObject(i);
                        String id = jsonObject.getString("_id");
                        int count = jsonObject.getInt("count");
                        dates.add(id);
                        counts.add(count);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                List<DataEntry> data = new ArrayList<>();
                for (int i =0;i<dates.size();i++ ){

                    data.add(new ValueDataEntry(dates.get(i), counts.get(i)));
                }

                pie.data(data);

                anyChartView.setChart(pie);
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