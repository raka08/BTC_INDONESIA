package net.sindonesia.xp.exchangepayment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.design.internal.NavigationMenuPresenter;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utama extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView namas;
    TextView saldos;
    ArrayList<Item> generateData(){
        request data = new request();
        String price_btc = null;
        String valume_btc = null;        
        String price_ltc = null;
        String valume_ltc = null;
        String price_bch = null;
        String valume_bch = null;
        try {

            String btc = data.curl("https://vip.bitcoin.co.id/api/btc_idr/ticker");            
            JSONObject tiker_btc = new  JSONObject (btc).getJSONObject("ticker");
            price_btc = tiker_btc.getString("last");
            valume_btc =  tiker_btc.getString("vol_btc");
            
            String ltc = data.curl("https://vip.bitcoin.co.id/api/ltc_idr/ticker");
            JSONObject tiker_ltc = new  JSONObject (ltc).getJSONObject("ticker");
            price_ltc = tiker_ltc.getString("last");
            valume_ltc =  tiker_ltc.getString("vol_ltc");

            String bch = data.curl("https://vip.bitcoin.co.id/api/bch_idr/ticker");
            JSONObject tiker_bch = new  JSONObject (bch).getJSONObject("ticker");
            price_bch = tiker_bch.getString("last");
            valume_bch =  tiker_bch.getString("vol_bch");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayList<Item> items = new ArrayList<Item>();
        items.add(new Item("BTC/IDR                   Harga: "+price_btc+" IDR","(Bitcoin)","Volume:" + valume_btc + " BTC" ));
        items.add(new Item("LTC/IDR                   Harga: "+price_ltc+" IDR","(Litecoin)", "Volume:" + valume_ltc + " LTC"));
        items.add(new Item("BCH/IDR                   Harga: "+price_bch+" IDR","(Bitcoincash)", "Volume:" + valume_bch + " BCH"));


        return items;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utama);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationViewRight = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationViewRight.getHeaderView(0);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Context context = getApplicationContext();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (preferences.getBoolean("Login", false) == false) {
            Intent intent = new Intent(Utama.this, Start.class);
            startActivity(intent);
        }
            request data = new request();
            String sceret = preferences.getString("Secret", null);
            String kunci = preferences.getString("Key", null);
            String nama = preferences.getString("Nama", null);
            String datas = null;
            namas = (TextView) header.findViewById(R.id.textViewnama);
            namas.setText(nama);



            try {
                datas = data.ambil("getInfo", sceret.trim(), kunci);
                JSONObject jsonObj = new JSONObject(datas);

                // Getting JSON Array node
                JSONObject saldo = jsonObj.getJSONObject("return");
                saldos = (TextView) header.findViewById(R.id.saldo);
                saldos.setText("Saldo Rp."+saldo.getJSONObject("balance").getString("idr"));

                Log.d("info_debug", saldo.getJSONObject("balance").getString("idr"));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


        // 1. pass context and data to the custom adapter
        MyAdapter adapter = new MyAdapter(this, generateData());

        //2. setListAdapter
        ListView listview =(ListView) findViewById(R.id.list);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                View wantedView = listview.getChildAt(position);
                TextView harga = (TextView) wantedView.findViewById(R.id.Harga);
                Context context = getApplicationContext();
                CharSequence text = harga.getText();
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });








            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
        }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
        public boolean onCreateOptionsMenu (Menu menu){
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.utama, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected (MenuItem item){
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings) {
                return true;
            }

            return super.onOptionsItemSelected(item);
        }

        @SuppressWarnings("StatementWithEmptyBody")
        @Override
        public boolean onNavigationItemSelected (MenuItem item){
            // Handle navigation view item clicks here.
            int id = item.getItemId();

             if (id == R.id.logout)
             {
                    Context context = getApplicationContext();
                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.clear().apply();
                    Intent intent = new Intent(Utama.this, Start.class);
                    startActivity(intent);

             }else if (id == R.id.open){
                 Intent intent = new Intent(Utama.this, open_order.class);
                 startActivity(intent);
             }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
    private class MyAdapter extends ArrayAdapter<Item> {

        private final Context context;
        private final ArrayList<Item> itemsArrayList;

        public MyAdapter(Context context, ArrayList<Item> itemsArrayList) {

            super(context, R.layout.list_btc, itemsArrayList);

            this.context = context;
            this.itemsArrayList = itemsArrayList;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            // 1. Create inflater
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // 2. Get rowView from inflater
            View rowView = inflater.inflate(R.layout.list_btc, parent, false);

            // 3. Get the two text view from the rowView
            TextView harga = (TextView) rowView.findViewById(R.id.Harga);
            TextView nama = (TextView) rowView.findViewById(R.id.nama_btc);
            TextView volum = (TextView) rowView.findViewById(R.id.volume);

            // 4. Set the text for textView
            harga.setText(itemsArrayList.get(position).getHarga());
            nama.setText(itemsArrayList.get(position).getNama());
            volum.setText(itemsArrayList.get(position).getVolum());

            // 5. retrn rowView
            return rowView;
        }
    }
    private  class Item {

        private String harga;
        private String nama;
        private String volum;

        public Item(String harga, String nama, String volum ) {
            super();
            this.harga = harga;
            this.nama = nama;
            this.volum = volum;
        }

        public String getVolum() {
            return volum;
        }

        public String getNama() {
            return nama;
        }

        public String getHarga() {
            return harga;
        }
        // getters and setters...
    }

}


