package com.deardhruv.countries;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.deardhruv.countries.adapter.CountryListAdapter;
import com.deardhruv.countries.adapter.RecyclerItemTouchHelper;
import com.deardhruv.countries.model.CountriesResponse;
import com.deardhruv.countries.model.entity.Country;
import com.deardhruv.countries.repository.FetchCountriesTask;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements FetchCountriesTask.OnLoadListener,
        RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    TextView textView;
    ConstraintLayout constraintLayout;
    private RecyclerView recyclerView;
    private List<Country> countryList;
    private CountryListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.txt_loading);
        constraintLayout = findViewById(R.id.constraint_layout);
        recyclerView = findViewById(R.id.recycler_countries);
        textView.setText(getString(R.string.str_countries_are_loading));

        FetchCountriesTask fetchCountriesTask = new FetchCountriesTask(this, this);
        fetchCountriesTask.execute();

        countryList = new ArrayList<>();
        mAdapter = new CountryListAdapter(countryList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);


        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback =
                new RecyclerItemTouchHelper(ItemTouchHelper.LEFT, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

    }

    @Override
    public void onLoadComplete(CountriesResponse response) {
        Log.e("CountriesResponse", "CountriesResponse - " + response.toString());

        recyclerView.setVisibility(View.VISIBLE);
        textView.setVisibility(View.GONE);

        textView.setText("");

        countryList.clear();
        countryList.addAll(response.getCountries());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onError() {
        Log.e("onError", "onError: ");
        recyclerView.setVisibility(View.GONE);
        textView.setVisibility(View.VISIBLE);

        textView.setText("Something went wrong! Please try again.");
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof CountryListAdapter.CountryViewHolder) {
            // get the removed item name to display it in snack bar
            String name = countryList.get(viewHolder.getAdapterPosition()).getCountry();

            // backup of removed item for undo purpose
            final Country deletedItem = countryList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            mAdapter.removeItem(viewHolder.getAdapterPosition());

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(constraintLayout, name + " removed from list!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", view -> {

                // undo is selected, restore the deleted item
                mAdapter.restoreItem(deletedItem, deletedIndex);
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }
}
