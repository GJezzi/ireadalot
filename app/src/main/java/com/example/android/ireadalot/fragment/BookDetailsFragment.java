package com.example.android.ireadalot.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.ireadalot.R;
import com.example.android.ireadalot.model.Book;

/**
 * Created by gjezzi on 25/07/16.
 */
public class BookDetailsFragment extends Fragment {

    private Context mContext;
    private ImageView mBookIcon;
    private TextView mAuthorName;
    private TextView mBookTitle;
    private Book mBook;

    public BookDetailsFragment() { setHasOptionsMenu(true); }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        View rootView = inflater.inflate(R.layout.fragment_book_details, container, false);
        StringBuilder builder = new StringBuilder();

        mBookIcon = (ImageView) rootView.findViewById(R.id.book_details_thumb);
        mAuthorName = (TextView) rootView.findViewById(R.id.book_details_author);
        mBookTitle = (TextView) rootView.findViewById(R.id.book_details_title);

        Glide.with(mContext)
                .load(mBook.getVolumeInfo().getImageLinks().getThumbnail())
                .crossFade()
                .into(mBookIcon);

        for (String string : mBook.getVolumeInfo().getAuthors()) {
            if(builder.length() > 0) {
                builder.append(", ");
            }
            builder.append(string);
        }
        mAuthorName.setText(builder.toString());

        return rootView;
    }
}
