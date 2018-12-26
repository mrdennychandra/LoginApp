package id.go.bpkp.loginapp.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import id.go.bpkp.loginapp.FormKaryawanActivity;
import id.go.bpkp.loginapp.HomeActivity;
import id.go.bpkp.loginapp.R;
import id.go.bpkp.loginapp.adapter.KaryawanAdapter;
import id.go.bpkp.loginapp.http.ApiInterface;
import id.go.bpkp.loginapp.http.RestClient;
import id.go.bpkp.loginapp.model.Karyawan;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private RecyclerView list;
    private List<Karyawan> karyawans;
    private KaryawanAdapter adapter;
    private ApiInterface api;
    SwipeRefreshLayout swipeLayout;
    SharedPreferences settings;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),FormKaryawanActivity.class);
                startActivity(intent);
            }
        });

        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler()
                        .postDelayed(new Runnable() {
                            @Override public void run() {
                                getData();
                            }
                        }, 500);
            }
        });

        karyawans = new ArrayList<>();
        adapter = new KaryawanAdapter(getActivity(),karyawans);
        api = RestClient.getClient().create(ApiInterface.class);
        list = (RecyclerView) view.findViewById(R.id.list);
        list.setAdapter(adapter);
        swipeLayout.setRefreshing(true);
        getData();
        return view;
    }

    private void getData(){
        Call<List<Karyawan>> call = api.getKaryawan();
        call.enqueue(new Callback<List<Karyawan>>() {
            @Override
            public void onResponse(Call<List<Karyawan>> call, Response<List<Karyawan>>
                    response) {
                karyawans = response.body();
                if(karyawans != null) {
                    adapter = new KaryawanAdapter(getActivity(),karyawans);
                    list.setAdapter(adapter);
                    swipeLayout.setRefreshing(false);
                }else{
                    Toast.makeText(getActivity(),response.message(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Karyawan>> call, Throwable t) {
                Log.e("Retrofit Get", t.toString());
                Toast.makeText(getActivity(),t.toString(),Toast.LENGTH_LONG).show();
            }
        });
    }
}
