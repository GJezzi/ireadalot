package com.example.android.ireadalot.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.example.android.ireadalot.R;
import com.example.android.ireadalot.model.Book;
import com.example.android.ireadalot.utils.Constants;
import com.firebase.client.Firebase;

/**
 * Created by gjezzi on 30/08/16.
 */
public class RemoveBookDialogFragment extends DialogFragment{


    final static String LOG_TAG = "RemoveBookFragment";
    String mBookId;

    public RemoveBookDialogFragment() {}

    public static RemoveBookDialogFragment newInstance(Book book, String bookId) {
        RemoveBookDialogFragment removeBookDialogFragment = new RemoveBookDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_BOOK_ID, bookId);
        removeBookDialogFragment.setArguments(bundle);
        return removeBookDialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBookId = getArguments().getString(Constants.KEY_BOOK_ID);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.FirebaseUI_Dialog)
                .setTitle(getActivity().getResources().getString(R.string.action_remove_book))
                .setMessage(getString(R.string.dialog_message_are_you_sure_remove_book))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        removeBook();
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert);
        return builder.create();
    }

    private void removeBook() {
        final Firebase bookToRemoveRef = new Firebase(Constants.FIREBASE_URL_MY_SHELF_LIST).child(mBookId);
        Log.d(LOG_TAG, "Book Id: " + bookToRemoveRef);
        bookToRemoveRef.removeValue();
    }
}
