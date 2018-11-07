package com.example.sandysaju.openlibrarysearch;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class Tools {
    public static void openInBrowser(final String url, final String isbn, final Context context, final RequestQueue queue){
        AlertDialog.Builder builder;
// Add the buttons
        builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        builder.setTitle("Open in Browser");
        builder.setMessage("Would you like to open this book in the OpenLibrary Website?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                JsonObjectRequest reply = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    i.setData(Uri.parse(response.getJSONObject("ISBN:"+isbn).getString("info_url")));
                                    context.startActivity(i);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        //Toast.makeText(getApplicationContext(), error.toString(),Toast.LENGTH_LONG).show();
                    }
                });
                // Add the request to the RequestQueue.
                queue.add(reply);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        //AlertDialog dialog = builder.create();
        builder.show();
    }
}
