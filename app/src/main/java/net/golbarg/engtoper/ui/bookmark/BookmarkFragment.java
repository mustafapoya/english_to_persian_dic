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
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import net.golbarg.engtoper.R;
import net.golbarg.engtoper.db.TablePhraseEnglish;
import net.golbarg.engtoper.models.PhraseEnglish;

import java.util.ArrayList;


public class BookmarkFragment extends Fragment {

    Context context;
    AdView mAdViewScreenBanner;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_bookmark, container, false);
        context = root.getContext();

        mAdViewScreenBanner = root.findViewById(R.id.adViewScreenBanner);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdViewScreenBanner.loadAd(adRequest);

        TabLayout tabLayout = root.findViewById(R.id.tab_layout_bookmark);
        TabItem tabEnglish  = root.findViewById(R.id.tab_english);
        TabItem tabPersian  = root.findViewById(R.id.tab_persian);

        ViewPager2 viewPager = root.findViewById(R.id.view_pager_container);

        BookmarkPagerAdapter pagerAdapter = new BookmarkPagerAdapter(getActivity(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setUserInputEnabled(false);

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(
                tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText(R.string.title_english);
                        tab.setIcon(R.drawable.english);
                        break;
                    case 1:
                        tab.setText(R.string.title_persian);
                        tab.setIcon(R.drawable.persian);
                        break;
                }
            }
        }
        );
        tabLayoutMediator.attach();



        return root;
    }


}