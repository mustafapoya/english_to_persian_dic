package net.golbarg.engtoper.ui.bookmark;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import net.golbarg.engtoper.R;
import net.golbarg.engtoper.db.TablePhrase;
import net.golbarg.engtoper.models.Phrase;

import java.util.ArrayList;


public class BookmarkFragment extends Fragment {

    Context context;
    ProgressBar progressLoading;
    private ListView listViewBookmark;
    BookmarkListAdapter bookmarkListAdapter;
    ArrayList<Phrase> bookmarkArrayList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_bookmark, container, false);
        context = root.getContext();

        progressLoading = root.findViewById(R.id.progress_loading);
        progressLoading.setVisibility(View.GONE);

        listViewBookmark = root.findViewById(R.id.list_view_bookmark);

        bookmarkListAdapter = new BookmarkListAdapter(getActivity(), bookmarkArrayList);
        listViewBookmark.setAdapter(bookmarkListAdapter);

        new FetchPhraseEnglishDataTask().execute();

        return root;
    }

    private class FetchPhraseEnglishDataTask extends AsyncTask<String, String, ArrayList<Phrase>> {
        boolean successful = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressLoading.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Phrase> doInBackground(String... params) {
            ArrayList<Phrase> result = new ArrayList<>();

            try {
                TablePhrase tablePhrase = new TablePhrase(context);

                result = tablePhrase.getAll();

                successful = true;
                return result;
            }catch(Exception e) {
                successful = false;
                e.printStackTrace();
            }
            successful = false;
            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<Phrase> result) {
            super.onPostExecute(result);
            progressLoading.setVisibility(View.GONE);

            bookmarkArrayList.clear();
            bookmarkArrayList.addAll(result);
            bookmarkListAdapter.notifyDataSetChanged();
        }
    }
}