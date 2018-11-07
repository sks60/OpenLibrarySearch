package com.example.sandysaju.openlibrarysearch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONObject;

public class BookDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        Intent bookIntent = getIntent();

        TextView blurb = (TextView) findViewById(R.id.blurb);

        Bundle extra = getIntent().getExtras();

        try {
            JSONObject bookJSON = new JSONObject(extra.getString("stringReply"));
            //blurb.setText(bookJSON.getJSONObject("flapcopy").toString());
            Log.d(" ", "GET THE BLURB: " +bookJSON.getJSONObject("flapcopy").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //flapcopy has the blurb
        blurb.setMovementMethod(new ScrollingMovementMethod());

    }
}
