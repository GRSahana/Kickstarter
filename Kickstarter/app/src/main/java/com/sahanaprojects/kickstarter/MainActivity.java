package com.sahanaprojects.kickstarter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sahanaprojects.kickstarter.Adapter.RecyclerViewAdapter;
import com.sahanaprojects.kickstarter.Model.KickstarterDetails;
import com.sahanaprojects.kickstarter.service.APIservices;
import com.sahanaprojects.kickstarter.service.RetrofitSingleInstance;
import com.sahanaprojects.kickstarter.utils.DateTimeConverter;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerViewAdapter recyclerViewAdapter;
    List<KickstarterDetails> kickstarterDetailsList = new ArrayList<>();
    List<KickstarterDetails> kickstarterDetailsList2 = new ArrayList<>();
    ProgressBar progressBar;
    RecyclerView recyclerView;
    boolean isLoading = false;
    int ViewSize = 0, totalSize = 0;
    EditText search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerview);
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null){
            setSupportActionBar(toolbar);
        }
        progressBar = findViewById(R.id.progressbar);
        search = findViewById(R.id.search);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.hasFixedSize();

        fetchApiDetails();
    }

    private void fetchApiDetails() {
        // Create a very simple REST adapter which points the API endpoint.
        APIservices apIservices = RetrofitSingleInstance.getRetrofitInstance().create(APIservices.class);
        Call<List<KickstarterDetails>> call = apIservices.getData();
        // Fetch a list of the contents.
        call.enqueue(new Callback<List<KickstarterDetails>>() {
            @Override
            public void onResponse(Call<List<KickstarterDetails>> call, Response<List<KickstarterDetails>> response) {
                // The network call was a success and we got a response
                // TODO: use the list and display it
                progressBar.setVisibility(View.GONE);
                kickstarterDetailsList2 = response.body();
                totalSize = kickstarterDetailsList2.size();
                addList();
                initRecyclerView();
                initScrollListener();

                search.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                        // When user changed the Text
                        recyclerViewAdapter.getFilter().filter(cs);
                    }

                    @Override
                    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                                  int arg3) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void afterTextChanged(Editable arg0) {
                        // TODO Auto-generated method stub
                    }
                });

            }

            @Override
            public void onFailure(Call<List<KickstarterDetails>> call, Throwable t) {
                // the network call was a failure or the server send an error
                // TODO: handle error
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Internet Connection Error!!", Toast.LENGTH_SHORT).show();

            }
        });


    }

    //initial method to add the first 20 contents to the list
    private void addList(){
        ViewSize = 0;
        if(totalSize>=20) {
            for (int i = 0; i<20;i++){
                Log.d("data", "morethan20");
                ViewSize++;
                kickstarterDetailsList.add(kickstarterDetailsList2.get(i));
            }
        }else{
            for (int i = 0; i<=kickstarterDetailsList2.size();i++){
                Log.d("data", "moreless20");

                ViewSize++;
                kickstarterDetailsList.add(kickstarterDetailsList2.get(i));
            }
        }
    }

    //setting adapter to the recycler view
    private void initRecyclerView() {
        recyclerViewAdapter = new RecyclerViewAdapter(kickstarterDetailsList,MainActivity.this,kickstarterDetailsList2);
        recyclerView.setAdapter(recyclerViewAdapter);

    }

    //method used for infinite scrolling
    private void initScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == kickstarterDetailsList.size() - 1) {
                        //bottom of list!
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });


    }

    //method to load other 20 items every time in the list
    private void loadMore() {
        Log.d("Size", totalSize + " " + kickstarterDetailsList.size());
        //check if the number of items in the list is equal to the total number of responses received
        if(kickstarterDetailsList.size() < totalSize) {
            kickstarterDetailsList.add(null);
            recyclerView.post(new Runnable() {
                public void run() {
                    recyclerViewAdapter.notifyItemInserted(kickstarterDetailsList.size() - 1);
                }
            });
            //handler to show the loading process
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    kickstarterDetailsList.remove(kickstarterDetailsList.size() - 1);
                    int scrollPosition = kickstarterDetailsList.size();
                    recyclerViewAdapter.notifyItemRemoved(scrollPosition);
                    int nextLimit = ViewSize + 20;


                    Log.d("limit", totalSize + " " + nextLimit);

                    if (totalSize >= nextLimit) {
                        Log.d("data2", "20");
                        for (int i = ViewSize; i <= nextLimit; i++) {
                            Log.d("data2", ViewSize + "");
                            ViewSize++;
                            kickstarterDetailsList.add(kickstarterDetailsList2.get(i));
                        }
                    } else {
                        for (int i = ViewSize; i < kickstarterDetailsList2.size(); i++) {
                            Log.d("data22", ViewSize + "");
                            ViewSize++;
                            kickstarterDetailsList.add(kickstarterDetailsList2.get(i));
                        }
                    }

                    recyclerViewAdapter.notifyDataSetChanged();
                    isLoading = false;
                }
            }, 2000);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.sort) {
            //do nothing
            return true;
        }else if (id == R.id.filter) {
            //Filter by number of backers
            //Display alert dialog with radio buttons
            final CharSequence[] items = {"Above 10000", "Below 10000"};

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Number of Backer's");
            builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    Toast.makeText(getApplicationContext(), items[item], Toast.LENGTH_SHORT).show();
                    sortData();
                }
            });

            builder.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            }
                    });
            builder.setNegativeButton("No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
            return true;
        }else if (id == R.id.alpha) {
            //sort alphabetically
            isLoading = false;
            if(item.isChecked()) {
                item.setChecked(false);
                addList();
                initRecyclerView();
            }else {
                item.setChecked(true);
                //clear all the recyclerview
                kickstarterDetailsList.clear();
                recyclerViewAdapter.notifyDataSetChanged();
                Collections.sort(kickstarterDetailsList2, new Comparator<KickstarterDetails>() {
                    @Override
                    public int compare(KickstarterDetails item, KickstarterDetails t1) {
                        String s1 = item.getTitle();
                        String s2 = t1.getTitle();
                        return s1.compareToIgnoreCase(s2);

                    }

                });
                Log.d("Size", totalSize + " " + kickstarterDetailsList.size());
                addList();
                initRecyclerView();
            }
            return true;
        }else if (id == R.id.time) {
            //sort according to end time in ascending order
            isLoading = false;
            if(item.isChecked()) {
                item.setChecked(false);
                addList();
                initRecyclerView();
            }else {
                item.setChecked(true);
                //clear all the recyclerview
                kickstarterDetailsList.clear();
                recyclerViewAdapter.notifyDataSetChanged();
                //sort according to end time in ascending order method
                Collections.sort(kickstarterDetailsList2, new Comparator<KickstarterDetails>() {
                    @Override
                    public int compare(KickstarterDetails item, KickstarterDetails t1) {
                        Date s1 = DateTimeConverter.convertZoneDatetoDate(item.getEndTime());
                        Date s2 = DateTimeConverter.convertZoneDatetoDate(t1.getEndTime());

                        return s1.compareTo(s2);
                    }
                });
                Log.d("Size", totalSize + " " + kickstarterDetailsList.size());
                addList();
                initRecyclerView();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //Filter according to number of backers
    private void sortData() {
//        kickstarterDetailsList.clear();
//        recyclerViewAdapter.notifyDataSetChanged();
//        Collections.sort(kickstarterDetailsList2, new Comparator<KickstarterDetails>() {
//            @Override
//            public int compare(KickstarterDetails item, KickstarterDetails t1) {
//                int s1 = item.getAmtPledged();
//                if(s1>10000){
//                     return 1;
//                }else
//                    return 0;
//
//            }
//
//        });
//        Log.d("Size", totalSize + " " + kickstarterDetailsList.size());
//        addList();
//        initRecyclerView();
    }


}
