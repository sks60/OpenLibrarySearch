package com.example.sandysaju.openlibrarysearch;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sandysaju.openlibrarysearch.FavouriteListItemFragment.OnListFragmentInteractionListener;
import com.example.sandysaju.openlibrarysearch.dummy.DummyContent.DummyItem;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyFavouriteListItemRecyclerViewAdapter extends RecyclerView.Adapter<MyFavouriteListItemRecyclerViewAdapter.ViewHolder> {

    private final List<Favourite> mValues;
    private final OnListFragmentInteractionListener mListener;
    public MyDatabase db;

    public MyFavouriteListItemRecyclerViewAdapter(List<Favourite> items, OnListFragmentInteractionListener listener) {
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

        //holder.mIdView.setText(mValues.get(position).getIsbn());
        //holder.mContentView.setText(mValues.get(position).getBookTitle());

        BitmapFactory.Options imageOptions = new BitmapFactory.Options();
        imageOptions.inMutable = true;
        Bitmap image = BitmapFactory.decodeByteArray(holder.mItem.getImage(), 0, holder.mItem.getImage().length, imageOptions);
        holder.mBookCover.setImageBitmap(image);

        new Thread(new Runnable() {
            @Override
            public void run() {
                final int favCount = db.favourites_dao().countFav(holder.mItem.getIsbn());
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
                                    db.favourites_dao().deleteFav(holder.mItem.getIsbn());
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
                                    Favourite favourite = new Favourite(holder.mItem.getIsbn(), holder.mItem.getUrl(), imageByte, holder.mItem.getBookTitle(), holder.mItem.getYear());
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
        public Favourite mItem;
        public ImageView mBookCover;
        public ImageButton mFavourite;
        public Boolean mIsFavorite;
        //public TextView mIdView;
        //public TextView mContentView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mBookCover = view.findViewById(R.id.bookCover);
            mFavourite = view.findViewById(R.id.favouriteButton);
            mIsFavorite = false;
            //mIdView = view.findViewById(R.id.item_number);
            //mContentView = view.findViewById(R.id.content);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + "'";
        }
    }
}
