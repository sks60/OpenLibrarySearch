package com.example.sandysaju.openlibrarysearch;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public MyDatabase db;
    public List<History> bookHistoryList = new ArrayList<>();
    public String[] historyListToArray;
    public AutoCompleteTextView textView;
    public ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText search = (EditText) findViewById(R.id.authorId);

        if(Intent.ACTION_SEND.equals(getIntent().getAction()) && getIntent().getType() != null){
            if ("text/plain".equals(getIntent().getType())) {
                String sharedText = getIntent().getStringExtra(Intent.EXTRA_TEXT);
                if (sharedText != null) {
                    // Update UI to reflect text being shared
                    search.setText(sharedText);
                }
            }
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        textView = (AutoCompleteTextView)
                findViewById(R.id.authorId);

        db = MyDatabase.getDatabase(getApplicationContext());

        db.history_dao().getHistoryList().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable List<String> histories) {
                //bookHistoryList = histories;
                historyListToArray = new String[histories.size()];
                adapter = new ArrayAdapter<String>(getApplicationContext(),
                        android.R.layout.simple_dropdown_item_1line, historyListToArray);
                for (int i = 0; i < histories.size(); i++) {
                    historyListToArray[i] = histories.get(i);
                }
                textView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });




        final Button searchButton = (Button) findViewById(R.id.search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RequestQueue queue = Volley.newRequestQueue(v.getContext());
                String finalString = search.getText().toString().trim();
                String url ="http://openlibrary.org/search.json?q="+finalString.replaceAll(" ","+");


                //save search to database
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        History history = new History(search.getText().toString());
                        db.history_dao().insert(history);
                        Log.d(" ", "history: " + history.getSearch());
                    }
                }) .start();



                // Request a string response from the provided URL.
                JsonObjectRequest reply = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // Display the first 500 characters of the response string.
                                Intent intent = new Intent(getApplicationContext(), ListBooks.class);
                                intent.putExtra("jsonReply", response.toString());
                                startActivity(intent);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("", error.toString());
                    }
                });
                // Add the request to the RequestQueue.
                queue.add(reply);

            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.favouriteDrawer) {
            // Handle the camera action
            Intent faveIntent = new Intent(getApplicationContext(), Favourites_Activity.class);
            startActivity(faveIntent);
        } else if (id == R.id.searchDrawer) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
