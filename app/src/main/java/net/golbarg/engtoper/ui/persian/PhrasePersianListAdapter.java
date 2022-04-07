package net.golbarg.engtoper.ui.persian;

import android.app.Activity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import net.golbarg.engtoper.R;
import net.golbarg.engtoper.db.TablePhrasePersian;
import net.golbarg.engtoper.models.PhrasePersian;
import net.golbarg.engtoper.ui.home.PhraseEnglishListAdapter;
import net.golbarg.engtoper.util.UtilController;

import java.util.ArrayList;

public class PhrasePersianListAdapter extends ArrayAdapter<PhrasePersian> {
    public static final String TAG = PhraseEnglishListAdapter.class.getName();

    private final Activity context;
    private ArrayList<PhrasePersian> phrasePersianArrayList;
    private ArrayList<PhrasePersian> mDisplayedValues;
    LayoutInflater inflater;
    TablePhrasePersian tablePhrasePersian;
    public PhrasePersianListAdapter(Activity context, ArrayList<PhrasePersian> phrasePersianArrayList) {
        super(context, R.layout.custom_list_phrase_persian, phrasePersianArrayList);
        this.context = context;
        this.phrasePersianArrayList = phrasePersianArrayList;
        this.mDisplayedValues = phrasePersianArrayList;
        this.inflater = LayoutInflater.from(context);
        this.tablePhrasePersian = new TablePhrasePersian(context);
    }

    @Override
    public int getCount() {
        return this.mDisplayedValues.size();
    }

    @Nullable
    @Override
    public PhrasePersian getItem(int position) {
        return mDisplayedValues.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        ConstraintLayout constraintLayoutContainer;
        TextView txtWord, txtTranslate;
        ImageButton btnBookmark;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder holder = null;

        if(convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.custom_list_phrase_persian, null);
            holder.constraintLayoutContainer = convertView.findViewById(R.id.layout_container);
            holder.txtWord = convertView.findViewById(R.id.txt_word);
            holder.txtTranslate = convertView.findViewById(R.id.txt_translate);
            holder.btnBookmark = convertView.findViewById(R.id.btn_bookmark);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtWord.setText(String.valueOf(mDisplayedValues.get(position).getFromLanguage()));
        String trans = UtilController.removeHTMLTags(mDisplayedValues.get(position).getToLanguage());
        holder.txtTranslate.setText(Html.fromHtml(trans));

        if(mDisplayedValues.get(position).getFavorite() == 1) {
            holder.btnBookmark.setImageDrawable(context.getDrawable(R.drawable.ic_bookmark_yes));
        } else {
            holder.btnBookmark.setImageDrawable(context.getDrawable(R.drawable.ic_bookmark_no));
        }

        holder.btnBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(mDisplayedValues.get(position).getFavorite() == 1) {
                        mDisplayedValues.get(position).setFavorite(0);
                        ImageButton btnBookmark = (ImageButton) v;
                        btnBookmark.setImageDrawable(context.getDrawable(R.drawable.ic_bookmark_no));
                    } else {
                        mDisplayedValues.get(position).setFavorite(1);
                        ImageButton btnBookmark = (ImageButton) v;
                        btnBookmark.setImageDrawable(context.getDrawable(R.drawable.ic_bookmark_yes));
                    }
                    tablePhrasePersian.updateFavorite(mDisplayedValues.get(position));
//                    Toast.makeText(context, "word with id: " + mDisplayedValues.get(position).getId() + " updated", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mDisplayedValues = (ArrayList<PhrasePersian>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                ArrayList<PhrasePersian> filteredArrayList = new ArrayList<>();

                if(phrasePersianArrayList == null) {
                    phrasePersianArrayList = new ArrayList<>(mDisplayedValues);
                }

                /********
                 *
                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)
                 *
                 ********/
                if(constraint == null || constraint.length() == 0 || constraint.toString().trim().length() == 0) {
                    results.count = phrasePersianArrayList.size();
                    results.values = phrasePersianArrayList;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for(int i = 0; i < phrasePersianArrayList.size(); i++) {
                        String data = phrasePersianArrayList.get(i).getFromLanguage();
                        if(data.toLowerCase().startsWith(constraint.toString())) {
                            filteredArrayList.add(phrasePersianArrayList.get(i));
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
