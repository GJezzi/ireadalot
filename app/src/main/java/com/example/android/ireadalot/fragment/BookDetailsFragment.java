package com.example.android.ireadalot.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.ireadalot.R;

/**
 * Created by gjezzi on 25/07/16.
 */
public class BookDetailsFragment extends Fragment{

    public BookDetailsFragment() { setHasOptionsMenu(true); }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        View rootView = inflater.inflate(R.layout.fragment_books, container, false);
        return rootView;
    }
}
