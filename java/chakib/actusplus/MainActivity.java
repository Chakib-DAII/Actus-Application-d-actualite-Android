package chakib.actusplus;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import chakib.actusplus.Utilities.EndlessRecyclerOnScrollListener;
import chakib.actusplus.data.Actus;
import chakib.actusplus.data.ActusAdapter;

import com.kosalgeek.android.caching.FileCacher;
import com.kosalgeek.asynctask.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static java.lang.Thread.sleep;


public class MainActivity extends AppCompatActivity implements ActusAdapter.ActusAdapterOnClickHandler , SharedPreferences.OnSharedPreferenceChangeListener
        , AsyncResponse , SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    ArrayList actusArrayList;
    private ActusAdapter mActusAdapter;
    LinearLayoutManager manager;
    RelativeLayout mbuttonRl;
    RecyclerView recyclerView;
    EditText mEmailText, mEmailSign, mPasswordText, mPasswordSign, mVerifyPass, mName, mFamilyName;
    TextView mSignupLink, mLoginLink, mErrorMessageDisplay;
    LinearLayout mLinearLayout, mLlSign;
    ScrollView mScrollView, mSvSign;
    Button mButtonLogin, mButtonSignin , mloadMoreButton;
    CheckBox mCbShowPwd, mCbShowPwdSign, mCbShowPwdVerify, mCbMale, mCbFemale, mCbRememberMe;
    SwipeRefreshLayout swipeRefreshLayout;
    private String count = "10";
    private boolean loadMore = false;
    private boolean load = false;
    private String login, offset;

    //loader Id
    private static final int FORECAST_LOADER_ID = 0;

    private static boolean PREFERENCES_HAVE_BEEN_UPDATED = false;
    private boolean rememberMe;
    private static boolean logged = false;
    private static boolean loggedSet = false;
    private static boolean loggedFav = false;
    private static boolean loggedRec = false;

    FileCacher<String> ActusCacher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView = (RecyclerView) findViewById(R.id.rv_actus);
        mEmailText = (EditText) findViewById(R.id.ev_input_email);
        mPasswordText = (EditText) findViewById(R.id.ev_input_password);
        mEmailSign = (EditText) findViewById(R.id.ev_input_email_sign);
        mPasswordSign = (EditText) findViewById(R.id.ev_input_password_sign);
        mSignupLink = (TextView) findViewById(R.id.tv_link_signup);
        mLoginLink = (TextView) findViewById(R.id.tv_link_login);
        mLinearLayout = (LinearLayout) findViewById(R.id.ll_log_in);
        //mbuttonRl = (RelativeLayout) findViewById(R.id.ButtonRelativeLayout);
        mLlSign = (LinearLayout) findViewById(R.id.ll_sign_in);
        mScrollView = (ScrollView) findViewById(R.id.sv_log_in);
        mSvSign = (ScrollView) findViewById(R.id.sv_sign_in);
        mButtonLogin = (Button) findViewById(R.id.btn_login);
        mButtonSignin = (Button) findViewById(R.id.btn_sign);
        //mloadMoreButton  = (Button) findViewById(R.id.btn_load_more);
        mName = (EditText) findViewById(R.id.ev_name);
        mFamilyName = (EditText) findViewById(R.id.ev_family_name);
        mCbMale = (CheckBox) findViewById(R.id.cb_gender_m);
        mCbFemale = (CheckBox) findViewById(R.id.cb_gender_f);
        mVerifyPass = (EditText) findViewById(R.id.ev_verify_password);
        mCbShowPwd = (CheckBox) findViewById(R.id.cbShowPwd);
        mCbShowPwdVerify = (CheckBox) findViewById(R.id.cbShowPwdverify);
        mCbShowPwdSign = (CheckBox) findViewById(R.id.cbShowPwd_sign);
        mCbRememberMe = (CheckBox) findViewById(R.id.cb_remember_me);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        mActusAdapter = new ActusAdapter(this);
        recyclerView.setAdapter(mActusAdapter);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);

                                        HashMap postData = new HashMap();
                                        postData.put("mobile", "android");
                                        postData.put("txtoffset", "0");
                                        postData.put("txtlimit", count);
                                        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
                                        load = true;

                                        PostResponseAsyncTask task = new PostResponseAsyncTask(MainActivity.this, postData, false, MainActivity.this);
                                        task.execute("http://192.168.137.1:8080/actusplus/getdataMain.php");

                                        swipeRefreshLayout.setRefreshing(false);


                                    }
                                }
        );


        SetupSharedPreferences();
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);

        mCbShowPwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // checkbox status is changed from uncheck to checked.
                if (!isChecked) {
                    // show password
                    mPasswordText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    // hide password
                    mPasswordText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });
        mCbShowPwdSign.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // checkbox status is changed from uncheck to checked.
                if (!isChecked) {
                    // show password
                    mPasswordSign.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    // hide password
                    mPasswordSign.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });

        mCbShowPwdVerify.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // checkbox status is changed from uncheck to checked.
                if (!isChecked) {
                    // show password
                    mVerifyPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    // hide password
                    mVerifyPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });

     /*   recyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener(manager) {
            @Override
            public void onLoadMore(int current_page) {
                HashMap postData = new HashMap();
                postData.put("mobile", "android");
                String offset = String.valueOf(mActusAdapter.getItemCount());
                postData.put("txtoffset", offset);
                String toLoad = String.valueOf(mActusAdapter.getItemCount() + Integer.parseInt(count));
                postData.put("txtlimit", toLoad);
                //Toast.makeText(MainActivity.this,"offset"+ offset + "\nto load "+ toLoad, Toast.LENGTH_LONG).show();
                loadMore = true;
                //load = true;
                mErrorMessageDisplay.setVisibility(View.INVISIBLE);
                PostResponseAsyncTask task = new PostResponseAsyncTask(MainActivity.this, postData, false, MainActivity.this);
                task.execute("http://192.168.137.1:8080/actusplus/getdataMain.php");
            }
        });*/

        ActusCacher = new FileCacher<>(MainActivity.this, "ActusCache.txt");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItemThatWasSelected = item.getItemId();

        if (logged == true) {
            if (menuItemThatWasSelected == R.id.action_settings) {
                Intent startSettingsAct = new Intent(this, SettingsActivity.class);
                startActivity(startSettingsAct);
            }
            if (menuItemThatWasSelected == R.id.action_favoris) {
                Intent startFavoris = new Intent(this, FavorisActivity.class);
                startActivity(startFavoris);
            }
        } else {
            //Toast.makeText(MainActivity.this, "you have to login first", Toast.LENGTH_LONG).show();
//            mbuttonRl.setVisibility(View.INVISIBLE);
            mScrollView.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setVisibility(View.INVISIBLE);
            if (menuItemThatWasSelected == R.id.action_settings)
                loggedSet = true;
            if (menuItemThatWasSelected == R.id.action_favoris)
                loggedFav = true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        float touchPointX = ev.getX();
        float touchPointY = ev.getY();
        int[] coordinates = new int[2];
        if (mScrollView.getVisibility() == View.VISIBLE) {
            mLinearLayout.getLocationOnScreen(coordinates);
            if (touchPointX < coordinates[0] || touchPointX > coordinates[0] + mLinearLayout.getWidth() || touchPointY < coordinates[1] || touchPointY > coordinates[1] + mLinearLayout.getHeight()) {
                mScrollView.setVisibility(View.INVISIBLE);
                mSvSign.setVisibility(View.INVISIBLE);
                swipeRefreshLayout.setVisibility(View.VISIBLE);
                //mbuttonRl.setVisibility(View.VISIBLE);
                return true;
            }
        } else if (mSvSign.getVisibility() == View.VISIBLE) {
            mLlSign.getLocationOnScreen(coordinates);
            if (touchPointX < coordinates[0] || touchPointX > coordinates[0] + mLlSign.getWidth() || touchPointY < coordinates[1] || touchPointY > coordinates[1] + mLlSign.getHeight()) {
                mScrollView.setVisibility(View.INVISIBLE);
                mSvSign.setVisibility(View.INVISIBLE);
                swipeRefreshLayout.setVisibility(View.VISIBLE);
                //mbuttonRl.setVisibility(View.VISIBLE);
                return true;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    Intent intentDetailActus;

    @Override
    public void onClick(Actus selectedActus) {
        Context context = this;
        intentDetailActus = new Intent(MainActivity.this, DetailActuActivity.class);
        intentDetailActus.putExtra("Actus", selectedActus);
        loggedRec = true;
        if (logged == false) {
            mScrollView.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setVisibility(View.INVISIBLE);
            //mbuttonRl.setVisibility(View.INVISIBLE);
        } else {
            startActivity(intentDetailActus);
        }


    }

    public void login(View view) {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        HashMap postData = new HashMap();
        postData.put("mobile", "android");
        postData.put("txtLogin", mEmailText.getText().toString());
        postData.put("txtPassword",mPasswordText.getText().toString());

        PostResponseAsyncTask task = new PostResponseAsyncTask(this, postData);
        task.execute("http://192.168.137.1:8080/actusplus/login.php");
    }

    @Override
    public void processFinish(String result) {
        //Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        if (result.equals("success")) {
            //Toast.makeText(this, "Login Successfully", Toast.LENGTH_LONG).show();
            EditSharedPreferencesRememberMe();
            logged = true;
            if (loggedSet == true) {
                Intent startSettingsAct = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(startSettingsAct);
            }
            if (loggedFav == true) {
                Intent startFavoris = new Intent(MainActivity.this, FavorisActivity.class);
                startActivity(startFavoris);
            }
            if (loggedRec == true) startActivity(intentDetailActus);
            mScrollView.setVisibility(View.INVISIBLE);
            swipeRefreshLayout.setVisibility(View.VISIBLE);
        } else if (result.equals("registered")) {
            Toast.makeText(this, "Bienvenue " + mName.getText().toString(), Toast.LENGTH_LONG).show();
            if (loggedSet == true) {
                Intent startSettingsAct = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(startSettingsAct);
            }
            if (loggedFav == true) {
                Intent startFavoris = new Intent(MainActivity.this, FavorisActivity.class);
                startActivity(startFavoris);
            }
            if (loggedRec == true) startActivity(intentDetailActus);
            logged = true;
            mSvSign.setVisibility(View.INVISIBLE);
            swipeRefreshLayout.setVisibility(View.VISIBLE);
            EditSharedPreferences();

        } else if (result.equals("exist")) {
            Toast.makeText(this, "Login Existant dans la Base de données , vous ne pouvez pas le dupliquer ", Toast.LENGTH_LONG).show();
        } else if (result.length() > 20) {
            //write in cache
            try {ActusCacher.clearCache();
                ActusCacher.writeCache(result);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //parse JSon
            JSONObject obj = null;
            try {
                obj = new JSONObject(result);
                JSONArray arr = obj.getJSONArray("result");

                ArrayList<Actus> actusArr = new ArrayList();
                for (int i = 0; i < arr.length(); i++) {
                    int id = arr.getJSONObject(i).getInt("id");
                    String upper_title = arr.getJSONObject(i).getString("upper_title");
                    String title = arr.getJSONObject(i).getString("title");
                    String date = arr.getJSONObject(i).getString("date");
                    String image_url = arr.getJSONObject(i).getString("image_url");
                    String contenu = arr.getJSONObject(i).getString("content");
                    String ressource_url = arr.getJSONObject(i).getString("ressource_url");
                    String imageBanner_url = arr.getJSONObject(i).getString("banner-url");
                    String imageHeader_url = arr.getJSONObject(i).getString("header_url");
                    actusArr.add(new Actus(id, title, upper_title, date, contenu, ressource_url, image_url, imageBanner_url, imageHeader_url));
                }
                if (loadMore == true) {
                    mErrorMessageDisplay.setVisibility(View.INVISIBLE);
                    recyclerView.setLayoutManager(manager);
                    mActusAdapter.addToActusData(actusArr);
                    recyclerView.setAdapter(mActusAdapter);
                    recyclerView.scrollToPosition(Integer.parseInt(offset));
                    loadMore = false;
                    load = false;
                } else {
                    mErrorMessageDisplay.setVisibility(View.INVISIBLE);
                    recyclerView.setLayoutManager(manager);
                    mActusAdapter = new ActusAdapter(actusArr, this);
                    recyclerView.setAdapter(mActusAdapter);
                    loadMore = false;
                    load = false;
                }

            } catch (JSONException e) {
                e.printStackTrace();
                //Toast.makeText(this, "exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "L'opération a échoué, réessayez s'il vous plait!", Toast.LENGTH_LONG).show();
            if(ActusCacher.hasCache()){
                try {
                    //parse JSon
                    JSONObject obj = null;
                        obj = new JSONObject( ActusCacher.readCache());
                        JSONArray arr = obj.getJSONArray("result");

                        ArrayList<Actus> actusArr = new ArrayList();
                        for (int i = 0; i < arr.length(); i++) {
                            int id = arr.getJSONObject(i).getInt("id");
                            String upper_title = arr.getJSONObject(i).getString("upper_title");
                            String title = arr.getJSONObject(i).getString("title");
                            String date = arr.getJSONObject(i).getString("date");
                            String image_url = arr.getJSONObject(i).getString("image_url");
                            String contenu = arr.getJSONObject(i).getString("content");
                            String ressource_url = arr.getJSONObject(i).getString("ressource_url");
                            String imageBanner_url = arr.getJSONObject(i).getString("banner-url");
                            String imageHeader_url = arr.getJSONObject(i).getString("header_url");
                            actusArr.add(new Actus(id, title, upper_title, date, contenu, ressource_url, image_url, imageBanner_url, imageHeader_url));
                        }

                    mErrorMessageDisplay.setVisibility(View.INVISIBLE);
                    recyclerView.setLayoutManager(manager);
                    mActusAdapter = new ActusAdapter(actusArr, this);
                    recyclerView.setAdapter(mActusAdapter);
                } catch (Exception e){
                    e.printStackTrace();
                }

                if (load == true && mActusAdapter.getItemCount() == 0)
                    mErrorMessageDisplay.setVisibility(View.VISIBLE);
                loadMore = false;
                load = false;

            }
        }
    }

    public void signup(View view) {

        HashMap postData = new HashMap();
        if (!mName.getText().toString().equals("") && !mFamilyName.getText().toString().equals("")
                && !mEmailSign.getText().toString().equals("") && !mPasswordSign.getText().toString().equals("")
                && !mVerifyPass.getText().toString().equals("")) {
            if (isValidEmail(mEmailSign.getText().toString())) {
                if (mVerifyPass.getText().toString().equals(mPasswordSign.getText().toString())) {
                    postData.put("mobile", "android");
                    postData.put("txtname", mName.getText().toString());
                    postData.put("txtfamilyname", mFamilyName.getText().toString());
                    if (mCbMale.isChecked()) {
                        postData.put("optgender", mCbMale.getText().toString());
                    } else {
                        postData.put("optgender", mCbFemale.getText().toString());
                    }
                    postData.put("txtLogin", mEmailSign.getText().toString());
                    postData.put("txtPassword", mPasswordSign.getText().toString());

                    PostResponseAsyncTask task = new PostResponseAsyncTask(this, postData);
                    task.execute("http://192.168.137.1:8080/actusplus/signin.php");
                } else
                    Toast.makeText(this, " Le mot de passe et le mot de passe de vérification doivent être identiques ! ", Toast.LENGTH_LONG).show();
            } else
                Toast.makeText(this, "Veuillez vérifier le courrier !! ", Toast.LENGTH_LONG).show();
        } else Toast.makeText(this, " Veuillez remplir toutes les valeurs! ", Toast.LENGTH_LONG).show();

    }

    public void changeToSignin(View view) {
        mScrollView.setVisibility(View.INVISIBLE);
        mSvSign.setVisibility(View.VISIBLE);
    }

    public void changeToLogin(View view) {
        mSvSign.setVisibility(View.INVISIBLE);
        mScrollView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    private void SetupSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        count = sharedPreferences.getString(getString(R.string.pref_load_key), getString(R.string.pref_load_default));
        login = sharedPreferences.getString(getString(R.string.pref_mail_key), getString(R.string.pref_mail_default));
        rememberMe = sharedPreferences.getBoolean(getString(R.string.pref_remember_me_key), getResources().getBoolean(R.bool.pref_remember_me_default));
        if (rememberMe == true) {
            mPasswordText.setText(sharedPreferences.getString(getString(R.string.pref_password_key), getString(R.string.pref_password_default)));
            mEmailText.setText(sharedPreferences.getString(getString(R.string.pref_mail_key), getString(R.string.pref_mail_default)));
        }
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    private void EditSharedPreferencesRememberMe() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        // Writing data in SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (mCbRememberMe.isChecked()) {
            editor.putBoolean(getString(R.string.pref_remember_me_key), true);
        } else {
            editor.putBoolean(getString(R.string.pref_remember_me_key), false);
        }
        editor.commit();
    }

    private void EditSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        // Writing data in SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.pref_name_key), mName.getText().toString());
        editor.putString(getString(R.string.pref_family_name_key), mFamilyName.getText().toString());
        if (mCbMale.isChecked()) {
            editor.putString(getString(R.string.pref_gender_key), mCbMale.getText().toString());
        } else {
            editor.putString(getString(R.string.pref_gender_key), mCbFemale.getText().toString());
        }
        editor.putString(getString(R.string.pref_mail_key), mEmailSign.getText().toString());
        editor.putString(getString(R.string.pref_password_key), mPasswordSign.getText().toString());
        editor.commit();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_load_key))) {
            count = sharedPreferences.getString(getString(R.string.pref_load_key), getString(R.string.pref_load_default));
        } else if (key.equals(getString(R.string.pref_remember_me_key))) {
            rememberMe = sharedPreferences.getBoolean(key, getResources().getBoolean(R.bool.pref_remember_me_default));
            if (rememberMe == true) {
                mPasswordText.setText(sharedPreferences.getString(getString(R.string.pref_password_key), getString(R.string.pref_password_default)));
                mEmailText.setText(sharedPreferences.getString(getString(R.string.pref_mail_key), getString(R.string.pref_mail_default)));
            }

        } else if (key.equals(getString(R.string.pref_mail_key)) || key.equals(getString(R.string.pref_password_key))
                || key.equals(getString(R.string.pref_name_key)) || key.equals(getString(R.string.pref_family_name_key))
                || key.equals(getString(R.string.pref_gender_key))) {
            HashMap postData = new HashMap();
            postData.put("mobile", "android");
            postData.put("txtname", sharedPreferences.getString(getString(R.string.pref_name_key), getString(R.string.pref_name_default)));
            postData.put("txtfamilyname", sharedPreferences.getString(getString(R.string.pref_family_name_key), getString(R.string.pref_family_name_default)));
            postData.put("optgender", sharedPreferences.getString(getString(R.string.pref_gender_key), ""));
            postData.put("txtNewLogin", sharedPreferences.getString(getString(R.string.pref_mail_key), getString(R.string.pref_mail_default)));
            postData.put("txtLogin", login);
            postData.put("txtPassword", sharedPreferences.getString(getString(R.string.pref_password_key), getString(R.string.pref_password_default)));

            PostResponseAsyncTask task = new PostResponseAsyncTask(this, postData);
            task.execute("http://192.168.137.1:8080/actusplus/modifyAccount.php");
        }
        PREFERENCES_HAVE_BEEN_UPDATED = true;
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch (view.getId()) {
            case R.id.cb_gender_m:
                if (!mCbMale.isChecked()) mCbMale.setChecked(true);
                if (mCbFemale.isChecked()) mCbFemale.setChecked(false);
                break;
            case R.id.cb_gender_f:
                if (mCbMale.isChecked()) mCbMale.setChecked(false);
                if (!mCbFemale.isChecked()) mCbFemale.setChecked(true);
                break;

        }
    }

    @Override
    public void onRefresh() {

        swipeRefreshLayout.setRefreshing(true);

        HashMap postData = new HashMap();
        postData.put("mobile", "android");
        postData.put("txtoffset", "0");
        postData.put("txtlimit", count);
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        load = true;

        PostResponseAsyncTask task = new PostResponseAsyncTask(this, postData, false, this);
        task.execute("http://192.168.137.1:8080/actusplus/getdataMain.php");

        swipeRefreshLayout.setRefreshing(false);
    }

    public void loadMore(View view)
    {
        HashMap postData = new HashMap();
        postData.put("mobile", "android");
        offset = String.valueOf(mActusAdapter.getItemCount());
        postData.put("txtoffset", offset);
        String toLoad = String.valueOf(mActusAdapter.getItemCount() + Integer.parseInt(count));
        postData.put("txtlimit", toLoad);
        //Toast.makeText(MainActivity.this,"offset"+ offset + "\nto load "+ toLoad, Toast.LENGTH_LONG).show();
        loadMore = true;
        //load = true;
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        PostResponseAsyncTask task = new PostResponseAsyncTask(MainActivity.this, postData, false, MainActivity.this);
        task.execute("http://192.168.137.1:8080/actusplus/getdataMain.php");
    }
    public static final Pattern EMAIL_ADDRESS
            = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );
    boolean isLastVisible() {
        int pos = manager.findLastCompletelyVisibleItemPosition();
        int numItems = recyclerView.getAdapter().getItemCount();
        return (pos >= numItems);
    }
    private boolean isValidEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    //cryptage
    public String encrypt(String text)
    {
        return Base64.encodeToString(text.getBytes(), Base64.DEFAULT);
    }

    //decryptage
    public String decrypt(String text)
    {
        return new String(Base64.decode(text, Base64.DEFAULT) );
    }
}
