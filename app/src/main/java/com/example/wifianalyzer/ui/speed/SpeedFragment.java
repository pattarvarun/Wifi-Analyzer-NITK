package com.example.wifianalyzer.ui.speed;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wifianalyzer.*;

import java.util.ArrayList;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;

public class SpeedFragment extends Fragment {

    private SpeedViewModel mViewModel;

    public static SpeedFragment newInstance() {
        return new SpeedFragment();
    }

    private FancyButton btn_start_speed;
    List<SpeedTest_> speedTest_list = new ArrayList<SpeedTest_>();
    RecyclerView recyclerView;
    SpeedTestAdapter speedTestAdapter;
    TextView namewifi, notest;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.speed_fragment, container, false);

        notest = root.findViewById(R.id.notest);
        WifiManager wmgr = (WifiManager) getContext().getApplicationContext().getSystemService(
                Context.WIFI_SERVICE);
        final String name = wmgr.getConnectionInfo().getSSID();
        namewifi = root.findViewById(R.id.wifi_name);
        recyclerView = root.findViewById(R.id.liste_tests);
        namewifi.setText(name);
        btn_start_speed = root.findViewById(R.id.btn_start_speed);
        btn_start_speed.setOnClickListener(v -> {
            Intent intent = new Intent().setClass(getContext(),
                    com.example.wifianalyzer.ui.MainActivity.class);
            intent.putExtra("name_wifi", name);
            startActivity(intent);

        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        SQLiteDatabase database = new SampleSQLiteDBHelper(getContext()).getReadableDatabase();
        DBManager dbManager = new DBManager(getContext());
        dbManager.open();
        database.execSQL(
                "CREATE TABLE IF NOT EXISTS " + SampleSQLiteDBHelper.PERSON_TABLE_NAME + "("
                        + SampleSQLiteDBHelper.PERSON_COLUMN_NAME + " VARCHAR,"
                        + SampleSQLiteDBHelper.PERSON_COLUMN_D + " VARCHAR,Upload VARCHAR,"
                        + SampleSQLiteDBHelper.PERSON_COLUMN_DATE + " VARCHAR);");

        Cursor cursor = database.rawQuery(
                "Select * from " + SampleSQLiteDBHelper.PERSON_TABLE_NAME + " ", null);

        Toast.makeText(getContext(), "The total cursor count is " + cursor.getCount(),
                Toast.LENGTH_LONG).show();
        speedTest_list = new ArrayList<SpeedTest_>();
        while (cursor.moveToNext()) {
            int index;

            index = cursor.getColumnIndexOrThrow(SampleSQLiteDBHelper.PERSON_COLUMN_NAME);
            String rname = cursor.getString(index);

            index = cursor.getColumnIndexOrThrow(SampleSQLiteDBHelper.PERSON_COLUMN_D);
            String d = cursor.getString(index);

            index = cursor.getColumnIndexOrThrow("Upload");
            String u = cursor.getString(index);
            index = cursor.getColumnIndexOrThrow(SampleSQLiteDBHelper.PERSON_COLUMN_DATE);
            String date = cursor.getString(index);
            speedTest_list.add(new SpeedTest_(d, u, date, rname));
            //... do something with data
        }
        speedTestAdapter = new SpeedTestAdapter(getContext(), speedTest_list);

        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(speedTestAdapter);
        if (cursor.getCount() != 0) {
            recyclerView.setVisibility(View.VISIBLE);
            notest.setVisibility(View.GONE);
        }
        speedTestAdapter.notifyDataSetChanged();
        return root;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SpeedViewModel.class);
        // TODO: Use the ViewModel
    }

}
