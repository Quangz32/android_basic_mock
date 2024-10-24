package com.example.ojtbadamockproject.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.ojtbadamockproject.R;

public class SettingsFragment extends Fragment {

    View categoryView;  //wrapper view for category
    TextView tvCategory;
    View ratingView;    //wrapper view for rating
    TextView tvRating;
    View releaseView;   //wrapper view for release
    TextView tvReleaseYear;
    View sortView;      //wrapper view for sort
    TextView tvSort;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor preferenceEditor;


    public SettingsFragment() {
        // Required empty public constructor
    }


    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences =
                getActivity().getSharedPreferences("settings", Context.MODE_PRIVATE);
        preferenceEditor = sharedPreferences.edit();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        categoryView = view.findViewById(R.id.category_view);
        tvCategory = view.findViewById(R.id.category);

        ratingView = view.findViewById(R.id.rating_view);
        tvRating = view.findViewById(R.id.rating);

        releaseView = view.findViewById(R.id.release_view);
        tvReleaseYear = view.findViewById(R.id.releaseYear);

        sortView = view.findViewById(R.id.sort_view);
        tvSort = view.findViewById(R.id.sort);

        tvCategory.setText(sharedPreferences.getString("category", ""));
        tvRating.setText(String.valueOf(sharedPreferences.getInt("rating", 0)));
        tvReleaseYear.setText(sharedPreferences.getString("releaseYear", ""));
        tvSort.setText(sharedPreferences.getString("sort", ""));

        categoryView.setOnClickListener(v -> {
            showCategoryDialog();
        });

        ratingView.setOnClickListener(v -> {
            showRatingDialog();
        });

        releaseView.setOnClickListener(v -> {
            showReleaseYearDialog();
        });

        sortView.setOnClickListener(v -> {
            showSortDialog();
        });


        return view;
    }

    private void showCategoryDialog() {
        final String[] categories = new String[]{"Popular Movies", "Top Rated Movies", "Upcoming Movies", "Now Playing Movies"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose a Category")
                .setItems(categories, (dialog, which) -> {
                    String selectedCategory = categories[which];
                    tvCategory.setText(selectedCategory);
                    preferenceEditor.putString("category", selectedCategory);
                    preferenceEditor.apply();
                })
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void showRatingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select Rating");

        SeekBar seekBar = new SeekBar(getContext());
        seekBar.setMax(10);
        seekBar.setProgress(sharedPreferences.getInt("rating", 0));

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvRating.setText(String.valueOf(progress));
                preferenceEditor.putInt("rating", progress);
                preferenceEditor.apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        builder.setView(seekBar);
        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("OK", null);
        builder.show();
    }

    private void showReleaseYearDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Enter Release Year");

        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setText(sharedPreferences.getString("releaseYear", ""));
        input.setHint("Enter release year");

        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            String year = input.getText().toString();
            Toast.makeText(requireContext(), year, Toast.LENGTH_SHORT).show();
            tvReleaseYear.setText(year);
            preferenceEditor.putString("releaseYear", year);
            preferenceEditor.apply();
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void showSortDialog() {
        final String[] sortOptions = new String[]{"Release Date", "Rating"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose a Category")
                .setItems(sortOptions, (dialog, which) -> {
                    String selected = sortOptions[which];
                    tvSort.setText(selected);
                    preferenceEditor.putString("sort", selected);
                    preferenceEditor.apply();
                })
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

}