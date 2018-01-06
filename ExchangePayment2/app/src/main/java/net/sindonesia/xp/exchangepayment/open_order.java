package net.sindonesia.xp.exchangepayment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class open_order extends AppCompatActivity {

    private String price;
    private Object type;
    private String sisa;

    @RequiresApi(api = Build.VERSION_CODES.O)
    ArrayList<open_order.Item> generateData() {
        request data = new request();
        Context context = getApplicationContext();
        String datas = null;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String sceret = preferences.getString("Secret", null);
        String kunci = preferences.getString("Key", null);
        String nama = preferences.getString("Nama", null);
        ArrayList<open_order.Item> items = new ArrayList<open_order.Item>();

        try {

            datas = data.ambil("openOrders", sceret.trim(), kunci);
            Log.d("assss", datas);
            JSONObject hasil = new JSONObject(datas).getJSONObject("return");
            JSONObject array = hasil.getJSONObject("orders");
            JSONObject ignis_idr = array.getJSONArray("ignis_idr").getJSONObject(0);

            items.add(new open_order.Item("Order: "+ ignis_idr.getString("price"),"Sell: "+ ignis_idr.getString("order_ignis"), "Sisa: "+ignis_idr.getString("remain_ignis")));

            } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }




        return items;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_order);


        // 1. pass context and data to the custom adapter
        open_order.MyAdapter adapter = new open_order.MyAdapter(this, generateData());

        //2. setListAdapter
        ListView listview =(ListView) findViewById(R.id.open_order_list);
        listview.setAdapter(adapter);


    }


    private class MyAdapter extends ArrayAdapter<open_order.Item> {

        private final Context context;
        private final ArrayList<open_order.Item> itemsArrayList;

        public MyAdapter(Context context, ArrayList<open_order.Item> itemsArrayList) {

            super(context, R.layout.list_open_oreder, itemsArrayList);

            this.context = context;
            this.itemsArrayList = itemsArrayList;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            // 1. Create inflater
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // 2. Get rowView from inflater
            View rowView = inflater.inflate(R.layout.list_open_oreder, parent, false);

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
