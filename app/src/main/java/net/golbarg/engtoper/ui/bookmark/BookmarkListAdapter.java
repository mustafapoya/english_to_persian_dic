package net.golbarg.engtoper.ui.bookmark;

import android.app.Activity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import net.golbarg.engtoper.R;
import net.golbarg.engtoper.db.TablePhrase;
import net.golbarg.engtoper.models.Phrase;
import net.golbarg.engtoper.ui.home.PhraseEnglishListAdapter;
import net.golbarg.engtoper.util.UtilController;

import java.util.ArrayList;

public class BookmarkListAdapter extends ArrayAdapter<Phrase> {
    public static final String TAG = PhraseEnglishListAdapter.class.getName();

    private final Activity context;
    private ArrayList<Phrase> phraseArrayList;
    TablePhrase tablePhrase;

    public BookmarkListAdapter(Activity context, ArrayList<Phrase> phraseArrayList) {
        super(context, R.layout.custom_list_phrase_english, phraseArrayList);
        this.context = context;
        this.phraseArrayList = phraseArrayList;
        this.tablePhrase = new TablePhrase(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.custom_list_phrase_english, null,true);

        TextView txtWord = rowView.findViewById(R.id.txt_word);
        txtWord.setText(String.valueOf(phraseArrayList.get(position).getFromLanguage()));

        TextView txtTranslate = rowView.findViewById(R.id.txt_translate);
        String trans = UtilController.removeHTMLTags(phraseArrayList.get(position).getToLanguage());
        txtTranslate.setText(Html.fromHtml(trans));

        ImageButton btnBookmark = rowView.findViewById(R.id.btn_bookmark);

        if(phraseArrayList.get(position).getFavorite() == 1) {
            btnBookmark.setImageDrawable(context.getDrawable(R.drawable.ic_bookmark_yes));
        } else {
            btnBookmark.setImageDrawable(context.getDrawable(R.drawable.ic_bookmark_no));
        }

        btnBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(phraseArrayList.get(position).getFavorite() == 1) {
                        phraseArrayList.get(position).setFavorite(0);
                        ImageButton btnBookmark = (ImageButton) v;
                        btnBookmark.setImageDrawable(context.getDrawable(R.drawable.ic_bookmark_no));
                    } else {
                        phraseArrayList.get(position).setFavorite(1);
                        ImageButton btnBookmark = (ImageButton) v;
                        btnBookmark.setImageDrawable(context.getDrawable(R.drawable.ic_bookmark_yes));
                    }
                    tablePhrase.updateFavorite(phraseArrayList.get(position));
                    phraseArrayList.remove(position);
                    notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        return rowView;
    }
}
