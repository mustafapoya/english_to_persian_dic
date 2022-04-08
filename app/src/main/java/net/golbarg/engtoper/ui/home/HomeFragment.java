package net.golbarg.engtoper.ui.home;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import net.golbarg.engtoper.R;
import net.golbarg.engtoper.db.DatabaseHandler;
import net.golbarg.engtoper.db.TableConfig;
import net.golbarg.engtoper.db.TablePhraseEnglish;
import net.golbarg.engtoper.models.PhraseEnglish;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    public static final String TAG = HomeFragment.class.getName();

    Context context;
    ProgressBar progressLoading;
    AdView mAdViewScreenBanner;
    SearchView searchView;
    private ListView listViewPhrase;
    PhraseEnglishListAdapter phraseEnglishListAdapter;
    ArrayList<PhraseEnglish> phraseEnglishArrayList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);
        context = root.getContext();

        mAdViewScreenBanner = root.findViewById(R.id.adViewScreenBanner);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdViewScreenBanner.loadAd(adRequest);

        progressLoading = root.findViewById(R.id.progress_loading);
        progressLoading.setVisibility(View.GONE);
        searchView = root.findViewById(R.id.search_view_phrase);
        listViewPhrase = root.findViewById(R.id.list_view_phrase);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                phraseEnglishListAdapter.getFilter().filter(newText);
                return true;
            }
        });

        phraseEnglishListAdapter = new PhraseEnglishListAdapter(getActivity(), phraseEnglishArrayList);
        listViewPhrase.setAdapter(phraseEnglishListAdapter);

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
                TablePhraseEnglish tablePhraseEnglish = new TablePhraseEnglish(context);

                result = tablePhraseEnglish.getAll();

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

            phraseEnglishArrayList.clear();
            phraseEnglishArrayList.addAll(result);
            phraseEnglishListAdapter.notifyDataSetChanged();
        }
    }
}