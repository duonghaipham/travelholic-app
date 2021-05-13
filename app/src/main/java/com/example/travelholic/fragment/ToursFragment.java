package com.example.travelholic.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.travelholic.CreateTourActivity;
import com.example.travelholic.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

public class ToursFragment extends Fragment {

    private FloatingActionButton fabCreateTour;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tours, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fabCreateTour = view.findViewById(R.id.fab_create_tour);

        fabCreateTour.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CreateTourActivity.class);
            startActivity(intent);
        });
    }
}