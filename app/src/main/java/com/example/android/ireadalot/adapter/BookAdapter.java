package com.example.android.ireadalot.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.ireadalot.R;
import com.example.android.ireadalot.model.Book;

import java.util.ArrayList;

/**
 * Created by gjezzi on 22/06/16.
 */
public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    public interface OnBookClickListener {
        void onBookClicked(Book book);
    }

    private ArrayList<Book> mBooks;
    private OnBookClickListener mBookClickListener;
    private Context mContext;

    public BookAdapter(Context context, ArrayList<Book> books, OnBookClickListener onBookClickListener) {
        this.mContext = context;
        this.mBooks = books;
        this.mBookClickListener = onBookClickListener;

    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {

        public final CardView mBookCardView;
        public final LinearLayout mBooksLayout;
        public final TextView mAuthorName;
        public final TextView mBookTitle;
        public final TextView mBookDesc;
        public final ImageView mBookImage;

        public BookViewHolder(View view) {
            super(view);

            mBookCardView = (CardView) view.findViewById(R.id.book_card_view);
            mBooksLayout = (LinearLayout) view.findViewById(R.id.book_list_item_layout);
            mAuthorName = (TextView) view.findViewById(R.id.author_title);
            mBookTitle = (TextView) view.findViewById(R.id.book_title);
            mBookDesc = (TextView) view.findViewById(R.id.book_description);
            mBookImage = (ImageView) view.findViewById(R.id.book_thumbnail);
        }
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.book_list_item, viewGroup, false);
        BookViewHolder bookViewHolder = new BookViewHolder(view);
        view.setFocusable(true);
        return bookViewHolder;
    }

    @Override
    public void onBindViewHolder(BookViewHolder booksViewHolder, int position){
        final Book book = mBooks.get(position);
        StringBuilder builder = new StringBuilder();

        for (String string : book.getVolumeInfo().getAuthors()) {
            if(builder.length() > 0) {
                builder.append(", ");
            }
            builder.append(string);
        }

        booksViewHolder.mAuthorName.setText(builder.toString());
        booksViewHolder.mBookTitle.setText(book.getVolumeInfo().getTitle());
        booksViewHolder.mBookDesc.setText(book.getVolumeInfo().getDescription());

        Glide.with(mContext)
                .load(book.getVolumeInfo().getImageLinks().getThumbnail())
                .crossFade()
                .into(booksViewHolder.mBookImage);

        booksViewHolder.mBookCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBookClickListener.onBookClicked(book);
            }
        });
    }

    @Override
    public int getItemCount() {
        if ( null == mBooks ) return 0;
        return mBooks.size();
    }
}


