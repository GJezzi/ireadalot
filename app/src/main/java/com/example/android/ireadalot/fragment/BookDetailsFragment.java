package com.example.android.ireadalot.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.ireadalot.R;
import com.example.android.ireadalot.adapter.BookAdapter;
import com.example.android.ireadalot.model.Book;
import com.example.android.ireadalot.utils.Constants;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by gjezzi on 25/07/16.
 */
public class BookDetailsFragment extends Fragment {

    private static final String LOG_TAG = BookDetailsFragment.class.getSimpleName();

    public static final String EXTRA_BOOK = "book";

    private ArrayList<Book> mMyshelfBookList;
    private BookAdapter mBookAdapter;

    private Context mContext;
    private ImageView mBookIcon;
    private TextView mAuthorName;
    private TextView mBookTitle;
    private TextView mBookYear;
    private TextView mBookPages;
    private TextView mBookDescription;
    private Book mBook;
    private String mId;
    private Toolbar mToolbar;
    private AppBarLayout mAppBar;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private FloatingActionButton mFab;
    private String mBookId;
    private String fBookTitleName;

    public BookDetailsFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_book_details, container, false);

        Firebase bookIdRef = new Firebase(Constants.FIREBASE_URL).child("myBookList");
        bookIdRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(LOG_TAG, "The Data Has Changed!");

                mBook = dataSnapshot.getValue(Book.class);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        mBook = (Book) getActivity().getIntent().getSerializableExtra(EXTRA_BOOK);
        mToolbar = (Toolbar) rootView.findViewById(R.id.book_details_toolbar);
        mAppBar = (AppBarLayout) rootView.findViewById(R.id.book_details_appBar);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) rootView.findViewById(R.id.book_details_collapsing_toolbar);
        mFab = (FloatingActionButton) rootView.findViewById(R.id.fab_my_book);
        mBookIcon = (ImageView) rootView.findViewById(R.id.book_details_book_cover);
        mBookTitle = (TextView) rootView.findViewById(R.id.book_content_title);
        mAuthorName = (TextView) rootView.findViewById(R.id.book_content_author);
        mBookYear = (TextView) rootView.findViewById(R.id.book_content_year);
        mBookPages = (TextView) rootView.findViewById(R.id.book_content_pages);
        mBookDescription = (TextView) rootView.findViewById(R.id.book_content_description);

        setActionBarTitle(mBook.getVolumeInfo().getTitle());

        loadBookCover(mBook);
        loadBookDetailsFields(mBook);
        addBook();
        return rootView;
    }

    private void loadBookCover(Book book) {
        Glide.with(getActivity())
                .load(mBook.getVolumeInfo().getImageLinks().getThumbnail())
                .crossFade()
                .into(mBookIcon);
    }

    private void loadBookDetailsFields(Book book) {
        StringBuilder builder = new StringBuilder();
        mBookTitle.setText(book.getVolumeInfo().getTitle());

        for (String string : book.getVolumeInfo().getAuthors()) {
            if (builder.length() > 0) {
                builder.append(", ");
            }
            builder.append(string);
            mAuthorName.setText(builder.toString());

            mBookYear.setText(book.getVolumeInfo().getBookYear());
            mBookPages.setText(book.getVolumeInfo().getPages());
            mBookDescription.setText(book.getVolumeInfo().getDescription());

        }
    }

    private void setActionBarTitle(String title) {
        AppCompatActivity activity = ((AppCompatActivity) getActivity());
        activity.setSupportActionBar(mToolbar);

        if (mAppBar != null) {
            if (mAppBar.getLayoutParams() instanceof CoordinatorLayout.LayoutParams) {
                CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams)
                        mAppBar.getLayoutParams();
                layoutParams.height = getResources().getDisplayMetrics().widthPixels;
            }
        }
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (mCollapsingToolbarLayout != null) {
            activity.getSupportActionBar().setDisplayShowTitleEnabled(true);
            mCollapsingToolbarLayout.setTitle(title);
        } else {
            activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    public void addBook() {
        final Firebase ref = new Firebase(Constants.FIREBASE_URL);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
           public void onClick(View view) {
                ref.child("myBookList").setValue(mBook);

            }
        });

    }
}
