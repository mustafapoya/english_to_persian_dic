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
import net.golbarg.engtoper.db.TablePhraseEnglish;
import net.golbarg.engtoper.models.PhraseEnglish;
import net.golbarg.engtoper.ui.home.PhraseEnglishListAdapter;

import java.util.ArrayList;

public class BookmarkEnglishFragment extends Fragment {
    Context context;
    ProgressBar progressLoading;

    private ListView listViewBookmark;
    PhraseEnglishListAdapter bookmarkListAdapter;
    ArrayList<PhraseEnglish> bookmarkArrayList = new ArrayList<>();

    public BookmarkEnglishFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_bookmark_english, container, false);
        context = root.getContext();

        progressLoading = root.findViewById(R.id.progress_loading);
        progressLoading.setVisibility(View.GONE);

        listViewBookmark = root.findViewById(R.id.list_view_bookmark);

        bookmarkListAdapter = new PhraseEnglishListAdapter(getActivity(), bookmarkArrayList);
        listViewBookmark.setAdapter(bookmarkListAdapter);

        new FetchPhraseEnglishDataTask().execute();

        return root;
    }

    private class FetchPhraseEnglishDataTask extends AsyncTask<String, String, ArrayList<PhraseEnglish>> {
        boolean successful = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressLoading.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<PhraseEnglish> doInBackground(String... params) {
            ArrayList<PhraseEnglish> result = new ArrayList<>();

            try {
                TablePhraseEnglish tablePhrase = new TablePhraseEnglish(context);

                result = tablePhrase.getBookmarks();

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
        protected void onPostExecute(ArrayList<PhraseEnglish> result) {
            super.onPostExecute(result);
            progressLoading.setVisibility(View.GONE);
            bookmarkArrayList.clear();
            bookmarkArrayList.addAll(result);
            bookmarkListAdapter.notifyDataSetChanged();
        }
    }
}
