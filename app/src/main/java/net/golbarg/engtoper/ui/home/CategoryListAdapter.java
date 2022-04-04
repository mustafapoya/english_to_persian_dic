package net.golbarg.engtoper.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import net.golbarg.engtoper.R;
import net.golbarg.engtoper.db.DatabaseHandler;
import net.golbarg.engtoper.db.TableCategory;
import net.golbarg.engtoper.db.TableConfig;
import net.golbarg.engtoper.db.TableQuestion;
import net.golbarg.engtoper.db.TableQuestionAnswer;
import net.golbarg.engtoper.models.Category;
import net.golbarg.engtoper.models.Config;
import net.golbarg.engtoper.models.Question;
import net.golbarg.engtoper.models.QuestionAnswer;
import net.golbarg.engtoper.ui.dialog.CreditDialog;
import net.golbarg.engtoper.ui.question.QuestionActivity;
import net.golbarg.engtoper.util.CryptUtil;
import net.golbarg.engtoper.util.JsonUtil;
import net.golbarg.engtoper.util.UtilController;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class CategoryListAdapter extends ArrayAdapter<Category> {
    public static final String TAG = CategoryListAdapter.class.getName();

    private final Activity context;
    private final ArrayList<Category> categories;
    DatabaseHandler dbHandler;
    TableCategory tableCategory;
    TableQuestion tableQuestion;
    TableQuestionAnswer tableQuestionAnswer;
    TableConfig tableConfig;
    FragmentManager fragmentManager;
    private boolean isDownloading = false;
    private ListView parentListView;

    private InterstitialAd mInterstitialAd;

    public CategoryListAdapter(Activity context, ArrayList<Category> categories, FragmentManager fragmentManager, ListView parentListView) {
        super(context, R.layout.custom_list_category, categories);
        this.context = context;
        this.categories = categories;
        this.dbHandler = new DatabaseHandler(context);
        this.tableCategory = new TableCategory(dbHandler);
        this.tableQuestion = new TableQuestion(dbHandler);
        this.tableQuestionAnswer = new TableQuestionAnswer(dbHandler);
        this.tableConfig = new TableConfig(dbHandler);
        this.fragmentManager = fragmentManager;
        this.parentListView = parentListView;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.custom_list_category, null,true);

        Category selectedCategory = categories.get(position);

        TextView txtNumber = rowView.findViewById(R.id.txt_number);
        txtNumber.setText(String.valueOf(selectedCategory.getId()));

        TextView txtTitle = rowView.findViewById(R.id.txt_category_title);
        txtTitle.setText(selectedCategory.getTitle().replace("-", " "));

        TextView txtNumberOfQuestion = rowView.findViewById(R.id.txt_number_of_question);
        txtNumberOfQuestion.setText(String.valueOf(selectedCategory.getNumberOfQuestion()) + " questions");

        Button btnDownload = rowView.findViewById(R.id.btn_download);
        ProgressBar progress = rowView.findViewById(R.id.progress_download);

        Category categoryInDB = tableCategory.get(selectedCategory.getId());

        if(categoryInDB != null && tableQuestion.getCountOf(selectedCategory.getId()) == selectedCategory.getNumberOfQuestion()) {
            btnDownload.setText(context.getResources().getString(R.string.added));
            btnDownload.setEnabled(false);
            progress.setVisibility(View.VISIBLE);
            progress.setProgress(100);
        } else {
            btnDownload.setText(context.getResources().getString(R.string.add));
            btnDownload.setEnabled(true);
            progress.setVisibility(View.INVISIBLE);
            progress.setProgress(0);
        }

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Config credit = tableConfig.getByKey(UtilController.KEY_CREDIT);
                    int userCredit = Integer.valueOf(credit.getDecryptedValue());

                    if(userCredit >= 2 && !isDownloading) {
                        try {
                            if(tableCategory.get(selectedCategory.getId()) == null || tableQuestion.getCountOf(selectedCategory.getId()) < selectedCategory.getNumberOfQuestion()) {
                                isDownloading = true;
                                parentListView.setEnabled(false);
                                new FetchCategoryQuestionDataTask(rowView, progress, btnDownload, selectedCategory).execute();
                            }
                        } catch (Exception e) {
                            UtilController.showSnackMessage(rowView, context.getString(R.string.add_failed_try_again),
                                    context.getResources().getColor(R.color.red_500), R.id.nav_view);
                        }
                    } else {
                        UtilController.showSnackMessage(rowView, context.getString(R.string.you_dont_have_enough_credit),
                                context.getResources().getColor(R.color.red_500), R.id.nav_view);
                        CreditDialog creditDialog = new CreditDialog();
                        creditDialog.show(fragmentManager, CreditDialog.TAG);
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isDownloading) {
                    UtilController.showSnackMessage(rowView, context.getString(R.string.please_wait_for_download_to_finish),
                            context.getResources().getColor(R.color.blue_600), R.id.nav_view);
                    return;
                }
                if(tableCategory.get(selectedCategory.getId()) != null &&
                    tableQuestion.getCountOf(selectedCategory.getId()) == selectedCategory.getNumberOfQuestion()) {
                    Intent categoryQuestionIntent = new Intent(context, QuestionActivity.class);
                    categoryQuestionIntent.putExtra("category_id", categories.get(position).getId());

                    loadInterstitialAd();

                    if(mInterstitialAd != null) {
                        mInterstitialAd.show(context);
                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                super.onAdDismissedFullScreenContent();
                                context.startActivity(categoryQuestionIntent);
                            }
                        });
                    } else {
                        context.startActivity(categoryQuestionIntent);
                    }
                } else {
                    UtilController.showSnackMessage(rowView, context.getString(R.string.to_view_question_add_first),
                            context.getResources().getColor(R.color.green_500), R.id.nav_view);
                }
            }
        });

        return rowView;
    }

    private void loadInterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        // TODO: on publish add real ad unit
        /* real ad Unit: ca-app-pub-1361000594268534/6283312483 */
        /* test ad Unit: ca-app-pub-3940256099942544/1033173712 */
        InterstitialAd.load(context,"ca-app-pub-1361000594268534/6283312483", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        mInterstitialAd = null;
                    }
                });

    }

    private class FetchCategoryQuestionDataTask extends AsyncTask<String, String, ArrayList<Question>> {
        View rowView;
        ProgressBar progress;
        Button btnDownload;
        Category selectedCategory;
        boolean successful = false;

        FetchCategoryQuestionDataTask(View rowView, ProgressBar progress, Button btnDownload, Category selectedCategory) {
            this.rowView = rowView;
            this.progress = progress;
            this.btnDownload = btnDownload;
            this.selectedCategory = selectedCategory;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);
            progress.setProgress(0);
            btnDownload.setEnabled(false);
        }

        @Override
        protected ArrayList<Question> doInBackground(String... params) {
            ArrayList<Question> questionArrayList = new ArrayList<>();

            try {
                publishProgress(String.valueOf(5));
                JSONArray categories =  JsonUtil.getJSONQuestion(context);
                publishProgress(String.valueOf(10));

                for(int categoryIndex = 0; categoryIndex < categories.length(); categoryIndex++) {

                    try {
                        JSONObject jCategoryObject = categories.getJSONObject(categoryIndex);
                        if (jCategoryObject.getInt("id") == selectedCategory.getId()) {
                            publishProgress(String.valueOf(15));
                            tableCategory.delete(selectedCategory.getId());
                            tableQuestion.deleteByCategoryId(selectedCategory.getId());
                            tableQuestionAnswer.deleteByCategoryId(selectedCategory.getId());
                            publishProgress(String.valueOf(20));

                            //
                            tableCategory.create(selectedCategory);

                            JSONArray jQuestionsArr = jCategoryObject.getJSONArray("questions");
                            Log.d(TAG, "number of questions: " + jQuestionsArr.length());
                            publishProgress(String.valueOf(30));

                            for (int questionIndex = 0; questionIndex < jQuestionsArr.length(); questionIndex++) {
                                publishProgress(String.valueOf((questionIndex * categories.length() / 60) + 30));
                                // create and add to database
                                JSONObject jQuestionObj = jQuestionsArr.getJSONObject(questionIndex);
                                Question question = Question.createFromJson(jQuestionObj);
                                tableQuestion.create(question);

                                // query question answers and add to db
                                JSONArray jAnswersArr = jQuestionObj.getJSONArray("answers");
                                for(int answerIndex = 0; answerIndex < jAnswersArr.length(); answerIndex++) {
                                    JSONObject jAnswerObj = jAnswersArr.getJSONObject(answerIndex);
                                    QuestionAnswer answer = QuestionAnswer.createFromJson(jAnswerObj);
                                    tableQuestionAnswer.create(answer);
                                }
                            }
                            //break after category found and data inserted

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                publishProgress(String.valueOf(90));
                successful = true;
                return questionArrayList;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return questionArrayList;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            progress.setProgress(Integer.valueOf(values[0]));
        }

        @Override
        protected void onPostExecute(ArrayList<Question> questionArrayList) {
            super.onPostExecute(questionArrayList);
            isDownloading = false;
            parentListView.setEnabled(true);
            if(successful) {
                progress.setProgress(100);
                btnDownload.setEnabled(false);
                UtilController.showSnackMessage(rowView, context.getString(R.string.added_successfully),
                        context.getResources().getColor(R.color.green_500), R.id.nav_view);
                try {
                    Config credit = tableConfig.getByKey(UtilController.KEY_CREDIT);
                    int userCredit = Integer.valueOf(credit.getDecryptedValue()) - 2;
                    credit.setValue(CryptUtil.encrypt(String.valueOf(userCredit)));
                    tableConfig.updateByKey(credit);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                progress.setProgress(100);
                progress.setProgressTintList(ColorStateList.valueOf(Color.RED));
                btnDownload.setEnabled(true);
                UtilController.showSnackMessage(rowView, context.getString(R.string.add_failed_try_again),
                        context.getResources().getColor(R.color.red_500), R.id.nav_view);
            }
        }
    }

}
