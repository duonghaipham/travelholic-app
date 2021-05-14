package com.example.travelholic.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.travelholic.CreateTourActivity;
import com.example.travelholic.R;
import com.example.travelholic.helper.Session;
import com.example.travelholic.helper.Tour;
import com.example.travelholic.helper.TourRecyclerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HistoryFragment extends Fragment {

    private Session session;
    private List<Tour> tours;
    private RecyclerView rvTours;
    private FloatingActionButton fabCreateTour;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tours, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvTours = view.findViewById(R.id.rv_tours);
        tours = new Vector<>();
        refreshData();
    }

    private void refreshData() {
        OkHttpClient client = new OkHttpClient();
        Session session = new Session(getActivity());
        String url = "http://10.0.2.2/travelholic-app/server/index.php?controller=Tour&action=load_my_tours&username=" + session.getUsername();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) { }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    JSONArray jsonArray = new JSONArray(response.body().string());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Tour tour = new Tour();
                        tour.setImage(jsonObject.getString("image"));
                        tour.setTourName(jsonObject.getString("tour_name"));
                        tour.setType(jsonObject.getString("type"));
                        tour.setDuring(jsonObject.getString("during"));
                        tour.setStatus(jsonObject.getString("status"));
                        tours.add(tour);
                    }
                    getActivity().runOnUiThread(() -> {
                        TourRecyclerViewAdapter adapter = new TourRecyclerViewAdapter(getActivity(), tours);
                        rvTours.setLayoutManager(new LinearLayoutManager(getActivity()));
                        rvTours.setAdapter(adapter);
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}