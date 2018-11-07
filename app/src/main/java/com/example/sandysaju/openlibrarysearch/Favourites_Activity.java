package com.example.sandysaju.openlibrarysearch;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class Favourites_Activity extends AppCompatActivity implements FavouriteListItemFragment.OnListFragmentInteractionListener {

    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites_);
        queue = Volley.newRequestQueue(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FavouriteListItemFragment favouriteListItemFragment = FavouriteListItemFragment.newInstance(2);
        fragmentTransaction.add(R.id.fragmentContainer, favouriteListItemFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onListFragmentInteraction(Favourite item) {
        final Favourite book = item;
        final String url = getString(R.string.api_1)+item.getIsbn()+getString(R.string.api_2);


        Tools.openInBrowser(url, item.getIsbn(), this, queue);


    }
}
