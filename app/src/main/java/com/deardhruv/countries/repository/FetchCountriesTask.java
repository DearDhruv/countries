package com.deardhruv.countries.repository;

import android.content.Context;
import android.os.AsyncTask;

import com.deardhruv.countries.model.CountriesResponse;
import com.deardhruv.countries.model.entity.Country;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class FetchCountriesTask extends AsyncTask<Void, Void, CountriesResponse> {

    private final Context mContext;
    private final OnLoadListener mListener;

    public FetchCountriesTask(Context context, OnLoadListener listener) {
        mContext = context;
        mListener = listener;
    }

    @Override
    protected CountriesResponse doInBackground(Void... params) {
        BufferedReader reader = null;

        try {

            URL myUrl = new URL("https://restcountries.eu/rest/v2/all");

            HttpURLConnection conn = (HttpURLConnection) myUrl
                    .openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.connect();

            int statusCode = conn.getResponseCode();
            if (statusCode != 200) {
                return null;
            }

            InputStream inputStream = conn.getInputStream();
            StringBuffer buffer = new StringBuffer();

            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }

            String response = buffer.toString();

            return deserializeServiceResponse(response);

        } catch (IOException e) {

            e.printStackTrace();
            return null;

        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onPostExecute(CountriesResponse countriesResponse) {
        super.onPostExecute(countriesResponse);

        if (countriesResponse != null) {
            mListener.onLoadComplete(countriesResponse);
        }
    }

    private CountriesResponse deserializeServiceResponse(String response) {

        CountriesResponse countriesResponse = null;

        try {

            Object json = new JSONTokener(response).nextValue();
            JSONArray dataJson;

            if (json instanceof JSONArray)
                dataJson = (JSONArray) json;
            else
                return null;

            ArrayList<Country> countries = new ArrayList<>();

            for (int i = 0; i < dataJson.length(); i++) {
                Country country = new Country();

                JSONObject countryJson = dataJson.getJSONObject(i);
                country.setCountry((countryJson.optString(("name"), "None")));

                JSONArray jsonArray = countryJson.optJSONArray("currencies");
                if (jsonArray != null && jsonArray.optJSONObject(0) != null) {
                    country.setCurrency(jsonArray.optJSONObject(0).optString("name", "None"));
                }
                jsonArray = countryJson.optJSONArray("languages");

                if (jsonArray != null && jsonArray.optJSONObject(0) != null) {
                    country.setLanguage(jsonArray.optJSONObject(0).optString("name", "None"));
                }

                countries.add(country);
            }

            countriesResponse = new CountriesResponse();
            countriesResponse.setCountries(countries);
        } catch (JSONException e) {
            e.printStackTrace();
            mListener.onError();
        }

        return countriesResponse;
    }

    public interface OnLoadListener {

        void onLoadComplete(CountriesResponse response);

        void onError();

    }
}

