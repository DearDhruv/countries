package com.deardhruv.countries.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.deardhruv.countries.R;
import com.deardhruv.countries.model.entity.Country;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CountryListAdapter extends RecyclerView.Adapter<CountryListAdapter.CountryViewHolder> {
    private List<Country> countryList;

    public class CountryViewHolder extends RecyclerView.ViewHolder {
        public TextView txtCountry, txtCurrency, txtLanguage;
        public View viewBackground, viewForeground;

        public CountryViewHolder(View view) {
            super(view);
            txtCountry = view.findViewById(R.id.txt_country);
            txtCurrency = view.findViewById(R.id.txt_currency);
            txtLanguage = view.findViewById(R.id.txt_language);
            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);
        }
    }

    public CountryListAdapter(List<Country> countries) {
        this.countryList = countries;
    }

    @Override
    public CountryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_country, parent, false);

        return new CountryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CountryViewHolder holder, final int position) {
        final Country item = countryList.get(position);
        holder.txtCountry.setText(item.getCountry());
        holder.txtCurrency.setText(item.getCurrency());
        holder.txtLanguage.setText(item.getLanguage());
    }

    @Override
    public int getItemCount() {
        return countryList.size();
    }

    public void removeItem(int position) {
        countryList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(Country item, int position) {
        countryList.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }
}
