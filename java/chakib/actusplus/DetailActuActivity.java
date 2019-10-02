package chakib.actusplus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rtoshiro.view.video.FullscreenVideoLayout;
import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import chakib.actusplus.Utilities.DownloadImageFromInternet;
import chakib.actusplus.data.Actus;
import chakib.actusplus.data.ActusAdapter;

public class DetailActuActivity extends AppCompatActivity implements AsyncResponse {
    private static final String FORECAST_SHARE_HASHTAG = " #ActusPlusApp";

    TextView mTitleText,mUpperTitleText,mContentText;
    ImageView mBanner,mHeader;
    WebView mWebView;
    Actus actusToStart;
    private String login ;
    private int id;
    FullscreenVideoLayout videoLayout;
    private Menu menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_actu);

        mBanner = (ImageView) findViewById(R.id.iv_detail_image_baniere);
        mUpperTitleText = (TextView) findViewById(R.id.tv_detail_upper_title);
        mTitleText = (TextView) findViewById(R.id.tv_detail_title);
        mHeader = (ImageView) findViewById(R.id.iv_detail_image_header);
        mWebView = (WebView) findViewById(R.id.wv_detail_video);
        mContentText = (TextView) findViewById(R.id.tv_detail_content);

        Intent intentToStart = getIntent();
        actusToStart = (Actus) intentToStart.getExtras().getSerializable("Actus");

        if (intentToStart != null)
        {    mUpperTitleText.setText(actusToStart.getUpperTitle());
             mTitleText.setText(actusToStart.getTitre());
             mContentText.setText(actusToStart.getContenu());


            new DownloadImageFromInternet(mBanner)
                    .execute(actusToStart.getImageBanner());


            new DownloadImageFromInternet(mHeader)
                    .execute(actusToStart.getImageHeader());


            videoLayout = (FullscreenVideoLayout) findViewById(R.id.videoview);
            videoLayout.setActivity(this);

            Uri videoUri = Uri.parse(actusToStart.getRessourceUrl());
            try {
                videoLayout.setVideoURI(videoUri);

            } catch (IOException e) {
                e.printStackTrace();
            }
            //for favs
            id = actusToStart.getId();
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            login = sharedPreferences.getString(getString(R.string.pref_mail_key),getString(R.string.pref_mail_default));
            //Toast.makeText(this, "id : "+id+"\n login :"+login, Toast.LENGTH_LONG).show();

        }
        //recuperation de barre d'acion
        ActionBar actionBar = this.getSupportActionBar();
        //affichage de bouton de retour
        if(actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        //fav or not
        HashMap postData = new HashMap();
        postData.put("mobile", "android");
        postData.put("txtLogin",login);
        postData.put("id_actus",String.valueOf(id));
        PostResponseAsyncTask task = new PostResponseAsyncTask(DetailActuActivity.this,postData,false,DetailActuActivity.this);
        task.execute("http://192.168.137.1:8080/actusplus/getdataFav.php");
    }

    private Intent createShareForecastIntent() {
        String mlinkActus = actusToStart.getUpperTitle()+"\n"+actusToStart.getTitre()+"\n\n"
                            +actusToStart.getDate()+"\n\n"+actusToStart.getContenu()+"\n"
                            +"\n\n"+actusToStart.getRessourceUrl()+"\n\n\n";
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(mlinkActus + FORECAST_SHARE_HASHTAG)
                .getIntent();
        return shareIntent;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_detail_actus, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        //set intent pour l'action on click
        menuItem.setIntent(createShareForecastIntent());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItemThatWasSelected=item.getItemId();
        if(menuItemThatWasSelected == android.R.id.home)
        {    onBackPressed(); }
        if(menuItemThatWasSelected == R.id.action_settings)
        {
            Intent startSettingsAct = new Intent(this,SettingsActivity.class);
            startActivity(startSettingsAct);
        }
        if(menuItemThatWasSelected == R.id.action_favorite)
        {
            HashMap postData = new HashMap();
            postData.put("mobile", "android");
            postData.put("txtLogin",login);
            postData.put("id_actus",String.valueOf(id));
            PostResponseAsyncTask task = new PostResponseAsyncTask(DetailActuActivity.this,postData,false,DetailActuActivity.this);
               task.execute("http://192.168.137.1:8080/actusplus/addFav.php");

        }
        if (menuItemThatWasSelected == R.id.action_favoris) {
            Intent startFavoris = new Intent(this, FavorisActivity.class);
            startActivity(startFavoris);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void processFinish(String result) {
        if(result.equals("added")) {
            //Toast.makeText(this, "added to favorites ", Toast.LENGTH_LONG).show();
            menu.getItem(1).setIcon(getResources().getDrawable(R.drawable.ic_favorite_white_24dp1));
        }else if(result.equals("exist")) {
            //Toast.makeText(this, "Favoris Exists in DataBase , will be deleted ", Toast.LENGTH_LONG).show();
            HashMap postData = new HashMap();
            postData.put("mobile", "android");
            postData.put("txtLogin",login);
            postData.put("id_actus",String.valueOf(id));
            PostResponseAsyncTask task = new PostResponseAsyncTask(DetailActuActivity.this,postData,false,DetailActuActivity.this);
            task.execute("http://192.168.137.1:8080/actusplus/deleteFav.php");
        }else if(result.equals("deleted")) {
            //Toast.makeText(this, "deleted from favorites ", Toast.LENGTH_LONG).show();
            menu.getItem(1).setIcon(getResources().getDrawable(R.drawable.ic_favorite_white_24dp));
        }else if (result.length()>20){
            //parse JSon
            JSONObject obj = null;
            try {
                obj = new JSONObject(result);
                JSONArray arr = obj.getJSONArray("result");

                ArrayList<Actus> actusArr = new ArrayList();
                for (int i = 0; i < arr.length(); i++)
                {
                    int id = arr.getJSONObject(i).getInt("id");
                    String upper_title = arr.getJSONObject(i).getString("upper_title");
                    String title = arr.getJSONObject(i).getString("title");
                    String date = arr.getJSONObject(i).getString("date");
                    String image_url = arr.getJSONObject(i).getString("image_url");
                    String contenu = arr.getJSONObject(i).getString("content");
                    String ressource_url = arr.getJSONObject(i).getString("ressource_url");
                    String imageBanner_url = arr.getJSONObject(i).getString("banner-url");
                    String imageHeader_url = arr.getJSONObject(i).getString("header_url");
                    actusArr.add(new Actus(id , title , upper_title ,date , contenu,ressource_url,image_url,imageBanner_url,imageHeader_url));
                }
                for (int i = 0; i < actusArr.size(); i++)
                        if(actusArr.get(i).getId() == actusToStart.getId())
                            menu.getItem(1).setIcon(getResources().getDrawable(R.drawable.ic_favorite_white_24dp1));

            } catch (JSONException e) {
                e.printStackTrace();
                //Toast.makeText(this,"exception : "+ e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }else
            Toast.makeText(this, "L'opération a échoué, réessayez s'il vous plait!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
