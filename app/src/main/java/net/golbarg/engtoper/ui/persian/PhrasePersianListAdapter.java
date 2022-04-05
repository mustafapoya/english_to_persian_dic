package net.golbarg.engtoper.ui.persian;

import android.app.Activity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import net.golbarg.engtoper.R;
import net.golbarg.engtoper.models.PhraseEnglish;
import net.golbarg.engtoper.models.PhrasePersian;
import net.golbarg.engtoper.ui.home.PhraseEnglishListAdapter;

import java.util.ArrayList;

public class PhrasePersianListAdapter extends ArrayAdapter<PhrasePersian> {
    public static final String TAG = PhraseEnglishListAdapter.class.getName();

    private final Activity context;
    private final ArrayList<PhrasePersian> phrasePersianArrayList;

    public PhrasePersianListAdapter(Activity context, ArrayList<PhrasePersian> phrasePersianArrayList) {
        super(context, R.layout.custom_list_phrase_persian, phrasePersianArrayList);
        this.context = context;
        this.phrasePersianArrayList = phrasePersianArrayList;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.custom_list_phrase_persian, null, true);

        TextView txtWord = rowView.findViewById(R.id.txt_word);
        txtWord.setText(String.valueOf(phrasePersianArrayList.get(position).getFromLanguage()));

        TextView txtTranslate = rowView.findViewById(R.id.txt_translate);
        txtTranslate.setText(Html.fromHtml(phrasePersianArrayList.get(position).getToLanguage().replace("');", "")));

        return rowView;
    }
}
