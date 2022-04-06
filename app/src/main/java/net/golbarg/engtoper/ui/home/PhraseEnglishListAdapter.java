package net.golbarg.engtoper.ui.home;

import android.app.Activity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;

import net.golbarg.engtoper.R;
import net.golbarg.engtoper.models.PhraseEnglish;

import java.util.ArrayList;
import java.util.Locale;

public class PhraseEnglishListAdapter  extends ArrayAdapter<PhraseEnglish> implements Filterable {
    public static final String TAG = PhraseEnglishListAdapter.class.getName();

    private final Activity context;
    private ArrayList<PhraseEnglish> phraseEnglishArrayList;
    private ArrayList<PhraseEnglish> mDisplayedValues;
    LayoutInflater inflater;

    public PhraseEnglishListAdapter(Activity context, ArrayList<PhraseEnglish> phraseEnglishArrayList) {
        super(context, R.layout.custom_list_phrase_english, phraseEnglishArrayList);
        this.context = context;
        this.phraseEnglishArrayList = phraseEnglishArrayList;
        this.mDisplayedValues = phraseEnglishArrayList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return this.mDisplayedValues.size();
    }

    @Nullable
    @Override
    public PhraseEnglish getItem(int position) {
        return mDisplayedValues.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        ConstraintLayout constraintLayoutContainer;
        TextView txtWord, txtTranslate;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder holder = null;

        if(convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.custom_list_phrase_english, null);
            holder.constraintLayoutContainer = convertView.findViewById(R.id.layout_container);
            holder.txtWord = convertView.findViewById(R.id.txt_word);
            holder.txtTranslate = convertView.findViewById(R.id.txt_translate);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtWord.setText(String.valueOf(mDisplayedValues.get(position).getFromLanguage()));
        holder.txtTranslate.setText(Html.fromHtml(mDisplayedValues.get(position).getToLanguage().replace("');", "")));

        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mDisplayedValues = (ArrayList<PhraseEnglish>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                ArrayList<PhraseEnglish> filteredArrayList = new ArrayList<>();

                if(phraseEnglishArrayList == null) {
                    phraseEnglishArrayList = new ArrayList<>(mDisplayedValues);
                }

                /********
                 *
                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)
                 *
                 ********/
                if(constraint == null || constraint.length() == 0 || constraint.toString().trim().length() == 0) {
                    results.count = phraseEnglishArrayList.size();
                    results.values = phraseEnglishArrayList;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for(int i = 0; i < phraseEnglishArrayList.size(); i++) {
                        String data = phraseEnglishArrayList.get(i).getFromLanguage();
                        if(data.toLowerCase().startsWith(constraint.toString())) {
                            filteredArrayList.add(phraseEnglishArrayList.get(i));
                        }
                    }
                    // set the filtered result to return
                    results.count = filteredArrayList.size();
                    results.values = filteredArrayList;
                }
                return results;
            }

        };

        return filter;
    }
}
