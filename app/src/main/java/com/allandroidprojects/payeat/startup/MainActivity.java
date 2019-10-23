package com.allandroidprojects.payeat.startup;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.allandroidprojects.payeat.R;
import com.allandroidprojects.payeat.cart;
import com.allandroidprojects.payeat.kontak.about;
import com.allandroidprojects.payeat.kontak.contact;
import com.allandroidprojects.payeat.login;
import com.allandroidprojects.payeat.merchant.merchant;
import com.allandroidprojects.payeat.miscellaneous.EmptyActivity;
import com.allandroidprojects.payeat.notification.NotificationCountSetClass;
import com.allandroidprojects.payeat.options.CartListActivity;
import com.allandroidprojects.payeat.options.SearchResultActivity;
import com.allandroidprojects.payeat.promo.promosi;
import com.allandroidprojects.payeat.scanQr;
import com.allandroidprojects.payeat.testkuncoro.cart_kuncoro;
import com.allandroidprojects.payeat.testkuncoro.history_point;
import com.allandroidprojects.payeat.testkuncoro.history_saldo;
import com.allandroidprojects.payeat.testkuncoro.notification;
import com.allandroidprojects.payeat.testkuncoro.topup;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.payeat.login.AppController;
import com.payeat.login.Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.allandroidprojects.payeat.login.session_status;

public class MainActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {

   // private static final String TAG = "MainActivity";
    String tag_json_obj = "json_obj_req";
    String global_id;
    ConnectivityManager conMgr;
    private Button signOut;
    public static int notificationCountCart = 0;
    public static int notificationCount = 0;
    static ViewPager viewPager;
    static TabLayout tabLayout;
    public static int hitungcart = 0;
    int success;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;

    SharedPreferences sharedpreferences;

    private String url = Server.URL + "data.php";
    public static final String TAG_ID = "id";
    public static final String TAG_NAMA = "nama";

    public static final String TAG_USERNAME = "username";
    private static final String TAG_MESSAGE = "message";
    public static final String TAG_SALDO = "saldo";
    public static final String TAG_POIN = "poin";
    public static final String TAG_NOTIF = "notif";
    public static final String TAG_NOTIF2 = "notif2";
    private static final String TAG_SUCCESS = "success";
    String idx, username;
    String poin;
    String saldo;


    public MainActivity(){



    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = mSettings.edit();

        int id = 0;

        editor.putFloat("saldo", id);
        editor.apply();

        float saldo = mSettings.getFloat("saldo", 0 );
        Locale localeID = new Locale("in", "ID");
        NumberFormat format = NumberFormat.getCurrencyInstance(localeID);
        String currency = format.format(saldo);
        TextView asd = findViewById(R.id.saldo);
        asd.setText(currency);


        sharedpreferences = getSharedPreferences(login.my_shared_preferences, Context.MODE_PRIVATE);
        idx = getIntent().getStringExtra(TAG_ID);
        setGlobal_id(idx);
        username = getIntent().getStringExtra(TAG_USERNAME);
        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr.getActiveNetworkInfo() != null
                && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnected()) {

            checkLogin(idx);
        } else {
            Toast.makeText(getApplicationContext() ,"No Internet Connection", Toast.LENGTH_LONG).show();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }

    public void signOut() {
        auth.signOut();
    }





    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Get the notifications MenuItem and
        // its LayerDrawable (layer-list)
        MenuItem item = menu.findItem(R.id.action_cart);
        MenuItem item2 = menu.findItem(R.id.action_notifications);
        NotificationCountSetClass.setAddToCart2(MainActivity.this, item2,notificationCount);
        NotificationCountSetClass.setAddToCart(MainActivity.this, item,notificationCountCart);
        // force the ActionBar to relayout its MenuItems.
        // onCreateOptionsMenu(Menu) will be called again.
        invalidateOptionsMenu();
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            startActivity(new Intent(MainActivity.this, SearchResultActivity.class));
            return true;
        }else if (id == R.id.action_cart) {

           /* NotificationCountSetClass.setAddToCart(MainActivity.this, item, notificationCount);
            invalidateOptionsMenu();*/

            Intent intent = new Intent(MainActivity.this, cart_kuncoro.class);
            intent.putExtra("idUser", idx);
            startActivity(intent);

           /* notificationCount=0;//clear notification count
            invalidateOptionsMenu();*/
            return true;
        }else if (id == R.id.action_notifications){
            Intent intent = new Intent(MainActivity.this, notification.class);
            intent.putExtra("idUser", idx);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }

   /*  private void setupViewPager(ViewPager viewPager) {
       Adapter adapter = new Adapter(getSupportFragmentManager());
        ImageListFragment fragment = new ImageListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", 1);
        fragment.setArguments(bundle);
        adapter.addFragment(fragment, getString(R.string.item_1));
        fragment = new ImageListFragment();
        bundle = new Bundle();
        bundle.putInt("type", 2);
        fragment.setArguments(bundle);
        adapter.addFragment(fragment, getString(R.string.item_2));
        fragment = new ImageListFragment();
        bundle = new Bundle();
       bundle.putInt("type", 3);
        fragment.setArguments(bundle);
        adapter.addFragment(fragment, getString(R.string.item_3));
        fragment = new ImageListFragment();
        bundle = new Bundle();
        bundle.putInt("type", 4);
        fragment.setArguments(bundle);
        adapter.addFragment(fragment, getString(R.string.item_4));
        fragment = new ImageListFragment();
        bundle = new Bundle();
        bundle.putInt("type", 5);
        fragment.setArguments(bundle);
        adapter.addFragment(fragment, getString(R.string.item_5));
        fragment = new ImageListFragment();
        bundle = new Bundle();
        bundle.putInt("type", 6);
        fragment.setArguments(bundle);
        adapter.addFragment(fragment, getString(R.string.item_6));
        viewPager.setAdapter(adapter);
    }*/

     @SuppressWarnings("StatementWithEmptyBody")
   @Override
   public boolean onNavigationItemSelected(MenuItem item) {
         // Handle navigation view item clicks here.
         int id = item.getItemId();

         if (id == R.id.nav_item1) {
             Intent intent = new Intent(MainActivity.this, promosi.class);
             intent.putExtra("idUser", idx);
             intent.putExtra("poin", getPoin());
             startActivity(intent);
            // viewPager.setCurrentItem(0);

         } else if (id == R.id.my_cart)
         {
             Intent intent = new Intent(MainActivity.this, history_saldo.class);
             intent.putExtra("idUser", idx);
             startActivity(intent);

         }
         else if (id == R.id.my_account)
         {
             Intent intent = new Intent(MainActivity.this, topup.class);
             intent.putExtra("idUser", idx);
             startActivity(intent);

         }

         else if (id == R.id.my_point)
         {
             Intent intent = new Intent(MainActivity.this, history_point.class);
             intent.putExtra("idUser", idx);
             startActivity(intent);

         }
         else if (id == R.id.nav_item4) {
             Intent intent = new Intent(MainActivity.this, merchant.class);
             startActivity(intent);
         }
         else if (id == R.id.contact_us) {
             Intent intent = new Intent(MainActivity.this, contact.class);
             startActivity(intent);
         }
         else if (id == R.id.terms_conditions) {
             Intent intent = new Intent(MainActivity.this, about.class);
             startActivity(intent);
         }




       /*  } else if (id == R.id.nav_item2) {
            viewPager.setCurrentItem(1);
        } else if (id == R.id.nav_item3) {

         //   viewPager.setCurrentItem(2);
        }else if (id == R.id.nav_item4) {
            viewPager.setCurrentItem(3);
        } else if (id == R.id.nav_item5) {
            viewPager.setCurrentItem(4);
        }else if (id == R.id.nav_item6) {
            viewPager.setCurrentItem(5);
        }else if (id == R.id.my_wishlist) {
            startActivity(new Intent(MainActivity.this, WishlistActivity.class));
        }else if (id == R.id.my_cart) {
            startActivity(new Intent(MainActivity.this, CartListActivity.class));
        }else {
            startActivity(new Intent(MainActivity.this, EmptyActivity.class));
        }*/

             DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
             drawer.closeDrawer(GravityCompat.START);
             return true;
         }


    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }
    public void yay(View view){
        Intent intent = new Intent(MainActivity.this, scanQr.class);
        intent.putExtra("idUser", idx);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(TAG_ID, idx);
        editor.commit();
        startActivity(intent);

    }

    public int getHitung(){
        return hitungcart;
    }

    public void setHitung(int x){
         hitungcart = x;
    }


    public void log_out(View view){
        Toast.makeText(MainActivity.this, "Anda Telah Keluar ...", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(MainActivity.this, login.class));
        signOut();
    }

    public void sign_out(View view){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(session_status, false);
        editor.putString(TAG_ID, null);
        editor.putString(TAG_USERNAME, null);
        editor.commit();

        Intent intent = new Intent(MainActivity.this, login.class);
        finish();
        startActivity(intent);
    }

    private void checkLogin(final String id) {
       // final String[] saldo = {""};
        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override

            public void onResponse(String response) {
              // Log.e(TAG, "Login Response: " + response.toString());


                try {
                    JSONObject jObj = new JSONObject(response);

                    success = jObj.getInt(TAG_SUCCESS);

                    // Check for error node in json
                    if (success == 1) {
                        String saldo = jObj.getString(TAG_SALDO);
                        String poin = jObj.getString(TAG_POIN);
                        String nama = jObj.getString(TAG_NAMA);
                        String qty = jObj.getString(TAG_NOTIF);
                        String qty2 = jObj.getString(TAG_NOTIF2);

                       // String qtys = jObj.
                      //  Toast.makeText(getApplicationContext(), "Welcome Back", Toast.LENGTH_LONG).show();

                        SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                        SharedPreferences.Editor editor = mSettings.edit();


                        Locale localeID = new Locale("in", "ID");
                        NumberFormat format = NumberFormat.getCurrencyInstance(localeID);
                        String currency = format.format(Float.parseFloat(saldo));

                        TextView asd = findViewById(R.id.saldo);
                        asd.setText(currency);

                        TextView pon = findViewById(R.id.txt_point);
                        pon.setText(poin);
                        setPoin(poin);

                        TextView namee = findViewById(R.id.txt_nama);
                        namee.setText(nama);
                        if(qty==null){
                            notificationCountCart = Integer.parseInt("0");
                        }else notificationCountCart = Integer.parseInt(qty);

                        if(qty2==null){
                            notificationCount = Integer.parseInt("0");
                        }else notificationCount = Integer.parseInt(qty2);


                    } else {
                        Toast.makeText(getApplicationContext(),
                                jObj.getString("ERROR"), Toast.LENGTH_LONG).show();

                    }




                } catch (JSONException e) {
                    // JSON error
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }
        }

                , new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);



    }

    public void refresh(View view){
        sharedpreferences = getSharedPreferences(login.my_shared_preferences, Context.MODE_PRIVATE);
        idx = getIntent().getStringExtra(TAG_ID);
        username = getIntent().getStringExtra(TAG_USERNAME);
        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr.getActiveNetworkInfo() != null
                && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnected()) {

            checkLogin(idx);
        } else {
            Toast.makeText(getApplicationContext() ,"No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    public void intentpromosi(View view){
        Intent intent = new Intent(MainActivity.this, promosi.class);
        intent.putExtra("idUser", idx);
        intent.putExtra("poin", getPoin());
        startActivity(intent);
    }

    public void setPoin (String poin){
        this.poin = poin;
    }

    public String getPoin(){
        return poin;
    }

    public void setGlobal_id(String global_id){
        this.global_id = global_id;
    }

    public String getGlobal_id(){
        return global_id;
    }









}
