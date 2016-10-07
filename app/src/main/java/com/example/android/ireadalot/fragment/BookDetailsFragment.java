package com.example.android.ireadalot.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.android.ireadalot.R;
import com.example.android.ireadalot.adapter.BookAdapter;
import com.example.android.ireadalot.model.Book;
import com.example.android.ireadalot.utils.Constants;
import com.example.android.ireadalot.utils.Utils;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by gjezzi on 25/07/16.
 */
public class BookDetailsFragment extends Fragment {

    private static final String LOG_TAG = "BookDetailsFragment";

    public static final String EXTRA_BOOK = "book";

    final Firebase mRef = new Firebase(Constants.FIREBASE_URL);
    final DatabaseReference mDBRef = FirebaseDatabase.getInstance().getReference();

    private Firebase mMyShelfListRef;
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
    private Toolbar mToolbar;
    private AppBarLayout mAppBar;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private FloatingActionButton mFab;
    private String mBookId;



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
        generatePalette();
        loadBookCover(mBook);
        loadBookDetailsFields(mBook);

        addBook();
        return rootView;
    }

    public void loadBookCover(Book book) {
        Glide.with(getActivity())
                .load(mBook.getVolumeInfo().getImageLinks().getThumbnail())
                .asBitmap()
                .into(mBookIcon);
    }

    public void loadBookDetailsFields(Book book) {
        StringBuilder builder = new StringBuilder();
        mBookTitle.setText(book.getVolumeInfo().getTitle());

        for (String string : book.getVolumeInfo().getAuthors()) {
            if (builder.length() > 0) {
                builder.append(", ");
            }
            builder.append(string);
        }
        mAuthorName.setText(builder.toString());

        mBookYear.setText(book.getVolumeInfo().getBookYear());
        mBookPages.setText(book.getVolumeInfo().getPages());
        mBookDescription.setText(book.getVolumeInfo().getDescription());
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
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBookAdded(mBook);
                getActivity().finish();
            }
        });
    }

    public void onBookAdded(Book book) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            String userEmail = firebaseUser.getEmail();
            userEmail = Utils.encodeEmail(userEmail);
            mRef.child(Constants.FIREBASE_LOCATION_USERS)
                    .child(userEmail)
                    .child("myShelfBooks").push().setValue(book);
        }
    }

    private void generatePalette() {
        Glide.with(getActivity())
                .load(mBook.getVolumeInfo().getImageLinks().getThumbnail())
                .asBitmap()
                .into(new SimpleTarget<Bitmap>(R.id.adjust_width, R.id.adjust_height) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        Palette.generateAsync(resource, new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette palette) {
                                int primaryDark = getResources().getColor(R.color.colorPrimaryDark);
                                int primary = getResources().getColor(R.color.colorPrimary);
                                Palette.Swatch swatch = palette.getVibrantSwatch();
                                mCollapsingToolbarLayout.setContentScrimColor(palette.getVibrantColor(primary));
                                mCollapsingToolbarLayout.setStatusBarScrimColor(palette.getDarkVibrantColor(primaryDark));
                                mCollapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
                            }
                        });
                    }
                });
    }
}
