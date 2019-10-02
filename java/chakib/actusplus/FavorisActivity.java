package chakib.actusplus;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import chakib.actusplus.Utilities.EndlessRecyclerOnScrollListener;
import chakib.actusplus.data.Actus;
import chakib.actusplus.data.ActusAdapter;
import chakib.actusplus.data.ActusAdapterFav;

public class FavorisActivity extends AppCompatActivity implements AsyncResponse , ActusAdapterFav.ActusAdapterOnClickHandler , SwipeRefreshLayout.OnRefreshListener {


    private static final String TAG = MainActivity.class.getSimpleName();
    private ActusAdapterFav mActusAdapter;
    RecyclerView recyclerView;
    TextView  mErrorMessageDisplay;
   SwipeRefreshLayout swipeRefreshLayout;
    private String count ="10";
    private String login;
    private boolean loadMore = false;
    private boolean load = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoris);

        //recuperation de barre d'acion
        ActionBar actionBar = this.getSupportActionBar();
        //affichage de bouton de retour
        if(actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        recyclerView = (RecyclerView) findViewById(R.id.rv_actus_fav);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout_fav);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display_fav);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        mActusAdapter = new ActusAdapterFav(this);
        recyclerView.setAdapter(mActusAdapter);

            swipeRefreshLayout.setOnRefreshListener(this);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        login = sharedPreferences.getString(getString(R.string.pref_mail_key),getString(R.string.pref_mail_default));
        //Toast.makeText(this, " login :"+login, Toast.LENGTH_LONG).show();
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);

                                        HashMap postData = new HashMap();
                                        postData.put("mobile", "android");
                                        postData.put("txtLogin", login);
                                        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
                                        load = true;

                                        PostResponseAsyncTask task = new PostResponseAsyncTask(FavorisActivity.this,postData,false,FavorisActivity.this);
                                        task.execute("http://192.168.137.1:8080/actusplus/getdataFav.php");

                                        swipeRefreshLayout.setRefreshing(false);


                                    }
                                }
        );

        //ItemTouchHelper which hadles LEFT and Right moves
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                long id =((Actus)mActusAdapter.getItem(viewHolder.getAdapterPosition())).getId();
                //delete and refresh data
                HashMap postData = new HashMap();
                postData.put("mobile", "android");
                postData.put("txtLogin",login);
                postData.put("id_actus",String.valueOf(id));
                PostResponseAsyncTask task = new PostResponseAsyncTask(FavorisActivity.this,postData,false,FavorisActivity.this);
                task.execute("http://192.168.137.1:8080/actusplus/deleteFav.php");
            }
        }).attachToRecyclerView(recyclerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_fav, menu);
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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void processFinish(String result) {
        if(result.equals("deleted")) {
            //Toast.makeText(this, "deleted from favorites ", Toast.LENGTH_LONG).show();

            HashMap postData = new HashMap();
            postData.put("mobile", "android");
            postData.put("txtLogin", login);
            mErrorMessageDisplay.setVisibility(View.INVISIBLE);
            load = true;

            PostResponseAsyncTask task = new PostResponseAsyncTask(FavorisActivity.this,postData,false,FavorisActivity.this);
            task.execute("http://192.168.137.1:8080/actusplus/getdataFav.php");

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
                if(loadMore == true)
                {LinearLayoutManager manager = new LinearLayoutManager(this);
                    mErrorMessageDisplay.setVisibility(View.INVISIBLE);
                    recyclerView.setLayoutManager(manager);
                    mActusAdapter.addToActusData(actusArr);
                    recyclerView.setAdapter(mActusAdapter);
                    loadMore = false;
                    load = false;
                }else
                {LinearLayoutManager manager = new LinearLayoutManager(this);
                    mErrorMessageDisplay.setVisibility(View.INVISIBLE);
                    recyclerView.setLayoutManager(manager);
                    mActusAdapter = new ActusAdapterFav(actusArr,this);
                    recyclerView.setAdapter(mActusAdapter);
                }

            } catch (JSONException e) {
                e.printStackTrace();
                //Toast.makeText(this,"exception : "+ e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this, "L'opération a échoué, réessayez s'il vous plait!", Toast.LENGTH_LONG).show();
            if(load == true)
                mErrorMessageDisplay.setVisibility(View.VISIBLE);
            loadMore = false;
            load = false;
        }
    }

    @Override
    public void onRefresh() {

        swipeRefreshLayout.setRefreshing(true);

        HashMap postData = new HashMap();
        postData.put("mobile", "android");
        postData.put("txtLogin", login);
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        load = true;

        PostResponseAsyncTask task = new PostResponseAsyncTask(FavorisActivity.this,postData,false,FavorisActivity.this);
        task.execute("http://192.168.137.1:8080/actusplus/getdataFav.php");

        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onClick(Actus selectedActus) {
        Context context = this;
        Intent intentDetailActus = new Intent(FavorisActivity.this, DetailActuActivity.class);
        intentDetailActus.putExtra("Actus",  selectedActus);
        startActivity(intentDetailActus);
        }
    }

