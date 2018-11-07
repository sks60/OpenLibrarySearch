package com.example.sandysaju.openlibrarysearch;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sandysaju.openlibrarysearch.BookFragment.OnListFragmentInteractionListener;
import com.example.sandysaju.openlibrarysearch.api.APIBooks;
import com.example.sandysaju.openlibrarysearch.api.APIBooks.APIBook;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link APIBook} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyBookRecyclerViewAdapter extends RecyclerView.Adapter<MyBookRecyclerViewAdapter.ViewHolder> {

    private final List<APIBook> mValues;
    private final OnListFragmentInteractionListener mListener;
    public MyDatabase db;


    public MyBookRecyclerViewAdapter(List<APIBooks.APIBook> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_book, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        db = MyDatabase.getDatabase(holder.mView.getContext());

        Picasso.with(holder.mView.getContext()).load(mValues.get(position).details).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                if(width == 1){
                    holder.mBookCover.setImageResource(R.drawable.cover_not_found);
                }else {
                    holder.mBookCover.setImageBitmap(bitmap);

                }
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                holder.mBookCover.setImageResource(R.drawable.cover_not_found);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                holder.mBookCover.setImageResource(R.drawable.cover_not_found);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                final int favCount = db.favourites_dao().countFav(holder.mItem.id);
                holder.mIsFavorite = favCount >=1;
                ((Activity)holder.mView.getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(holder.mIsFavorite){
                            holder.mFavourite.setImageResource(R.drawable.favourite);
                        }else{
                            holder.mFavourite.setImageResource(R.drawable.not_favourite);
                        }
                    }
                });

                    holder.mFavourite.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View v) {
                            if(holder.mIsFavorite){
                                new Thread(new Runnable() {
                                @Override
                                    public void run() {
                                       db.favourites_dao().deleteFav(holder.mItem.id);
                                    }
                                }) .start();

                                Toast.makeText(v.getContext(), "Deleted from favourites", Toast.LENGTH_SHORT).show();
                                holder.mFavourite.setImageResource(R.drawable.not_favourite);
                                holder.mIsFavorite = false;

                        }else {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Drawable drawable = holder.mBookCover.getDrawable();
                                    Bitmap image = ((BitmapDrawable) drawable).getBitmap();
                                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                    image.compress(Bitmap.CompressFormat.JPEG, 70, stream);
                                    byte[] imageByte = stream.toByteArray();
                                    Favourite favourite = new Favourite(holder.mItem.id, holder.mItem.details, imageByte, holder.mItem.bookTitle, holder.mItem.year);
                                    db.favourites_dao().insert(favourite);
                                }
                            }).start();

                            Toast.makeText(v.getContext(), "Added to favourites", Toast.LENGTH_SHORT).show();
                            holder.mFavourite.setImageResource(R.drawable.favourite);
                            holder.mIsFavorite = true;
                        }
                    }
                });
            }
        }).start();



        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });




    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
//        public final TextView mIdView;
        public APIBook mItem;
        public ImageView mBookCover;
        public ImageButton mFavourite;
        public Boolean mIsFavorite;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mBookCover = view.findViewById(R.id.bookCover);
            mFavourite = view.findViewById(R.id.favouriteButton);
            mIsFavorite = false;

//            mIdView = (TextView) view.findViewById(R.id.item_number);

            //networkImageView = (NetworkImageView) view.findViewById(R.id.bookCover);


        }

        @Override
        public String toString() {
            return super.toString() + " '" + "'";
        }
    }

}
