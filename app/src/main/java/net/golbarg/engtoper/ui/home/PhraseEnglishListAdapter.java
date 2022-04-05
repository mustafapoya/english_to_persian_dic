package net.golbarg.engtoper.ui.home;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import net.golbarg.engtoper.R;
import net.golbarg.engtoper.models.PhraseEnglish;

import java.util.ArrayList;

public class PhraseEnglishListAdapter  extends ArrayAdapter<PhraseEnglish> {
    public static final String TAG = PhraseEnglishListAdapter.class.getName();

    private final Activity context;
    private final ArrayList<PhraseEnglish> phraseEnglishArrayList;

    public PhraseEnglishListAdapter(Activity context, ArrayList<PhraseEnglish> phraseEnglishArrayList) {
        super(context, R.layout.custom_list_phrase_english, phraseEnglishArrayList);
        this.context = context;
        this.phraseEnglishArrayList = phraseEnglishArrayList;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.custom_list_phrase_english, null, true);

        TextView txtNumber = rowView.findViewById(R.id.txt_word);
        txtNumber.setText(String.valueOf(phraseEnglishArrayList.get(position).getFromLanguage()));

        return rowView;
    }
}
