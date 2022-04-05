package net.golbarg.engtoper.ui.persian;

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

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import net.golbarg.engtoper.R;
import net.golbarg.engtoper.db.TablePhrasePersian;
import net.golbarg.engtoper.models.PhrasePersian;
import net.golbarg.engtoper.ui.home.HomeFragment;

import java.util.ArrayList;

public class PersianFragment extends Fragment {
    Context context;
    ProgressBar progressLoading;
    AdView mAdViewHomeScreenBanner;
    private ListView listViewPhrase;
    PhrasePersianListAdapter phrasePersianListAdapter;
    ArrayList<PhrasePersian> phrasePersianArrayList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_persian, container, false);
        context = root.getContext();

        mAdViewHomeScreenBanner = root.findViewById(R.id.adViewScreenBanner);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdViewHomeScreenBanner.loadAd(adRequest);

        progressLoading = root.findViewById(R.id.progress_loading);
        progressLoading.setVisibility(View.GONE);
        listViewPhrase = root.findViewById(R.id.list_view_phrase);


        phrasePersianListAdapter = new PhrasePersianListAdapter(getActivity(), phrasePersianArrayList);
        listViewPhrase.setAdapter(phrasePersianListAdapter);

        new FetchPhrasePersianDataTask().execute();


        return root;
    }

    private class FetchPhrasePersianDataTask extends AsyncTask<String, String, ArrayList<PhrasePersian>> {
        boolean successful = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressLoading.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<PhrasePersian> doInBackground(String... params) {
            ArrayList<PhrasePersian> result = new ArrayList<>();

            try {
                TablePhrasePersian tablePhrasePersian = new TablePhrasePersian(context);

                result = tablePhrasePersian.getAll();

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
        protected void onPostExecute(ArrayList<PhrasePersian> result) {
            super.onPostExecute(result);
            progressLoading.setVisibility(View.GONE);

            phrasePersianArrayList.clear();
            phrasePersianArrayList.addAll(result);
            phrasePersianListAdapter.notifyDataSetChanged();
        }
    }
}