package com.tiva11.gettingstartedwithandroidwear;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class BooksRVAdapter extends RecyclerView.Adapter<BooksRVAdapter.BookViewHolder>
    implements View.OnClickListener
{
    private List<Book> mBooks;
    public BooksRVAdapter() {
        mBooks = buildBooksLibrary();
    }
    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.book_list_row,viewGroup,false);
        return new BookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final BookViewHolder bookViewHolder, final int i) {
        bookViewHolder.textViewTitle.setText(mBooks.get(i).title);
        bookViewHolder.textViewAuthor.setText(mBooks.get(i).author);
        bookViewHolder.book = mBooks.get(i);
        //bookViewHolder.itemPosition = i;
        //This creates an anonymous class behind the scene
//        bookViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(bookViewHolder.itemView.getContext(),mBooks.get(i).title,Toast.LENGTH_SHORT).show();
//            }
//        });
        //This is Java 8 lambda for Single Abstract Method interface implementation/invocation,
        //it doesn't create anonymous inner class, instead, it uses InvokeDynamic instruction (Java 7)
        //But, this is not true for the Android VM (ART), which removes lambda by de-sugar and replaces it with
        //anonymous class/object, which is not really a problem, since virtual calls are a lot faster
        //than dynamic calls as explained by Martin Felcyerek in his comments for Rohit Surwase article
        //Android RecyclerView onItemClickListener a better way on hackernoon.
        //On the other hand desugaring replaces lambdas only on Android API devices below API 26,
        //so, on Oreo (26) and Pie (28) lambdas use dynamic invocation.
        //All of these are "red herring" since (1) Recycler View creates only a handful of holder views only as
        //many as required, and recycles them, so the listeners are created again and again but the garbage
        //collector removes the unused ones, (2) invocation speed for a user click is absolutely not relevant,
        //you cannot notice the difference not even on a slow Android Wear/Wear OS device.
//        bookViewHolder.itemView.setOnClickListener((View view) -> {// onClick(View view)
//            Toast.makeText(bookViewHolder.itemView.getContext(),mBooks.get(i).title,Toast.LENGTH_SHORT).show();
//        });
        bookViewHolder.itemView.setTag(bookViewHolder);
        bookViewHolder.itemView.setOnClickListener(this);//This object has an appropriate onClick method
    }
    @Override
    public int getItemCount() {
        return mBooks.size();
    }
    @Override
    public void onClick(View view) { //for View.OnClickListener to handle item clicks in the recycler view
        //We expect that the view's tag has a BookViewHolder
        if(view.getTag() instanceof BookViewHolder) {
            BookViewHolder bvh = (BookViewHolder) view.getTag(); //This cast is dangerous, we
            Toast.makeText(view.getContext(),bvh.book.title,Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(view.getContext(),R.string.errorTagIsNotBookViewHolder,Toast.LENGTH_LONG).show();
        }
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        public final TextView textViewTitle;
        public final TextView textViewAuthor;
        public final View itemView;
        public Book book;
        //public int itemPosition; //Don'' save index, because items could be deleted
        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAuthor = (TextView) itemView.findViewById(R.id.author);
            textViewTitle = (TextView) itemView.findViewById(R.id.title);
            this.itemView = itemView;
        }
    }
    private List<Book> buildBooksLibrary(){
        List<Book> books = new ArrayList<>();
        books.add(new Book("The Deerslayer","J.F. Cooper"));
        books.add(new Book("The Son of Iroquios","Ana Jürgens"));
        books.add(new Book("The Nosty Son","Kalman Mikszath"));
        books.add(new Book("The Black City","Kalman Mikszath"));
        books.add(new Book("Ghost in Lublo","Kalman Mikszath"));
        books.add(new Book("St Peter's Umbrela","Kalman Mikszath"));
        return books;
    }
    private Book mRecentlyDeletedItem;
    public void deleteItem(@NonNull BookViewHolder bookVH) {
        mRecentlyDeletedItem = bookVH.book;
        int itemPosition = mBooks.indexOf(bookVH.book);
        mBooks.remove(mRecentlyDeletedItem);
        this.notifyItemRemoved(itemPosition);
        showUndoSnackbar(bookVH);
    }
    private void showUndoSnackbar(@NonNull BookViewHolder bookVH){
        //Unfortunately, Snackbar doesn't work on my Huawei Smartwatch 2
        //I've added implementation 'com.android.support:design:28.0.0' to be able to compile,
        //I think there is a good reason, the WearOS samples have not configured for Snackbar
        Snackbar.make(bookVH.itemView,"Deleted", Snackbar.LENGTH_SHORT).show();
        //TODO Continue exploring why Snackbar throws exception on my Huawei Smartwatch
    }
    public static class SwipeToDeleteBook extends ItemTouchHelper.SimpleCallback {
        private final BooksRVAdapter mAdapter;
        public SwipeToDeleteBook(final BooksRVAdapter adapter) {
            super(0, ItemTouchHelper.LEFT);
            mAdapter = adapter;
        }
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
            return false;
        }
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            if(viewHolder instanceof BookViewHolder) {
                BookViewHolder bvh = (BookViewHolder)viewHolder; //This cast, any cast, is dangerous, we
                mAdapter.deleteItem(bvh);
            } else {
                Log.e("MIKI","Swiped viewHolder is not BookViewHolder");
            }
        }
    }
}
