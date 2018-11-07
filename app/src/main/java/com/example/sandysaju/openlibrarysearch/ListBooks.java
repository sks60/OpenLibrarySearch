package com.example.sandysaju.openlibrarysearch;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.sandysaju.openlibrarysearch.api.APIBooks;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ListBooks extends FragmentActivity implements BookFragment.OnListFragmentInteractionListener {

    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        final MyDatabase bd = MyDatabase.getDatabase(getApplicationContext());


        queue = Volley.newRequestQueue(this);
        APIBooks.ITEMS.clear();
        if(intent.hasExtra("jsonReply")){
            try {
                Log.d(" ", "onCreate: " + intent.getStringExtra("jsonReply"));
                JSONObject jsonObject = new JSONObject(intent.getStringExtra("jsonReply"));
                JSONArray titles = jsonObject.getJSONArray("docs");
                for (int i = 0; i < titles.length(); i++) {
                    final JSONObject temp = titles.getJSONObject(i);

                    String coverID = "";
                    Boolean ignore = false;
                    String url, isbn, bookTitle = "";
                    String year = "0000";

                    if(temp.has("cover_i")){
                        coverID = temp.getString("cover_i");
                    }else if(temp.has("isbn")){
                        coverID = temp.getJSONArray("isbn").getString(0);
                    }else{
                        ignore = true;
                    }

                    if(!ignore){
                        try {
                            url = "http://covers.openlibrary.org/b/id/" + coverID + "-M.jpg";
                            isbn = temp.getJSONArray("isbn").getString(0);
                            bookTitle = temp.getString("title");
                            if (temp.has("first_publish_year"))
                                year = temp.getString("first_publish_year");
                            APIBooks.addItem(new APIBooks.APIBook(isbn, url, bookTitle, year));
                        }catch (Exception e){

                        }
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        BookFragment bookFragment = BookFragment.newInstance(2);
        fragmentTransaction.add(R.id.fragmentContainer, bookFragment);
        fragmentTransaction.commit();

        setContentView(R.layout.activity_list_books);
    }

    @Override
    public void onListFragmentInteraction(APIBooks.APIBook item) {
        final APIBooks.APIBook book = item;
        //Log.d("x", "onListFragmentInteraction: " + item.details);
        //Toast.makeText(getApplicationContext(), book.details,Toast.LENGTH_SHORT).show();
        String url = getString(R.string.api_1_1)+item.id+getString(R.string.api_1_2);


        Tools.openInBrowser(url, item.id, this, queue);

    }
}
