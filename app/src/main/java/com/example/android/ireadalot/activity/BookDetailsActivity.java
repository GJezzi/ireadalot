package com.example.android.ireadalot.activity;

import android.os.Bundle;

import com.example.android.ireadalot.R;
import com.example.android.ireadalot.fragment.BookDetailsFragment;

/**
 * Created by gjezzi on 25/07/16.
 */
public class BookDetailsActivity extends BaseActivity {

    public final static int BOOK_DETAILS_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_details_activity);

        if (savedInstanceState == null) {
            Bundle args = new Bundle();
            BookDetailsFragment bookDetailsFragment = new BookDetailsFragment();

            bookDetailsFragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.book_details_container, bookDetailsFragment)
                    .commit();
        }
    }
}
