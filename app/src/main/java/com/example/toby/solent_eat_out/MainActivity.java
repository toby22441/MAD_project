package com.example.toby.solent_eat_out;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import android.location.LocationListener;
import android.location.Location;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import android.widget.EditText;
import android.widget.Toast;
import java.io.*;

public class MainActivity extends AppCompatActivity implements LocationListener {

    MapView mv;
    ItemizedIconOverlay<OverlayItem> items;
    ItemizedIconOverlay.OnItemGestureListener<OverlayItem> markerGestureListener;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));

        setContentView(R.layout.activity_main);

        mv = (MapView) findViewById(R.id.map1);

        mv.setBuiltInZoomControls(true);
        mv.getController().setZoom(0);
        mv.getController().setCenter(new GeoPoint(50.907479, -1.413357));


        markerGestureListener = new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
            public boolean onItemLongPress(int i, OverlayItem item) {
                Toast.makeText(MainActivity.this, item.getSnippet(), Toast.LENGTH_SHORT).show();
                return true;
            }

            public boolean onItemSingleTapUp(int i, OverlayItem item) {
                Toast.makeText(MainActivity.this, item.getSnippet(), Toast.LENGTH_SHORT).show();
                return true;
            }
        };
        items = new ItemizedIconOverlay<OverlayItem>(this, new ArrayList<OverlayItem>(), markerGestureListener);
        mv.getOverlays().add(items);

        LocationManager mgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    public void onLocationChanged(Location newLoc) {
       Toast.makeText(this, "!", Toast.LENGTH_LONG).show();
        mv.getController().setCenter(new GeoPoint(newLoc));
    }

    public void onProviderDisabled(String provider) {
       Toast.makeText(this, "Provider " + provider +
               " disabled", Toast.LENGTH_LONG).show();
    }

    public void onProviderEnabled(String provider) {
      Toast.makeText(this, "Provider " + provider +
              " enabled", Toast.LENGTH_LONG).show();
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {

        Toast.makeText(this, "Status changed: " + status,
              Toast.LENGTH_LONG).show();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.addrestaurant) {
            Intent intent = new Intent(this, AddRestaurantActivity.class);
            startActivityForResult(intent, 0);
            return true;
        }
        if (item.getItemId() == R.id.save) {

            try
            {
                PrintWriter printwriter =
                        new PrintWriter(new FileWriter(Environment.getExternalStorageDirectory().getAbsolutePath() + "/R.csv", true));

                for(int i=0; i<items.size(); i++)
                {
                    OverlayItem itm = items.getItem(i);
                    printwriter.println(itm.getTitle()+","+itm.getSnippet()+","+itm.getPoint().getLatitude()
                            +","+itm.getPoint().getLongitude());
                }
                printwriter.close();
            }
            catch(IOException e)
            {
                System.out.println (e);
            }

            return true;
        }
        if (item.getItemId() == R.id.preferences) {
            Intent intent = new Intent(this, PreferencesActivity.class);
            startActivityForResult(intent, 0);
            return true;
        }

        if (item.getItemId() == R.id.load) {
            try
            {
                BufferedReader reader = new BufferedReader(new FileReader(Environment.getExternalStorageDirectory().getAbsolutePath() + "/R.csv"));
                String line;

                while((line = reader.readLine()) != null)
                {

                    String[] comp = line.split(",");
                    if(comp.length==4)
                    {
                        Double lat = Double.valueOf(comp[2]).doubleValue();
                        Double lon = Double.valueOf(comp[3]).doubleValue();

                        OverlayItem restaurant = new OverlayItem(comp[0],comp[1], new GeoPoint(lat,lon));

                        restaurant.setMarker(getResources().getDrawable(R.drawable.marker));
                        items.addItem(restaurant);
                        mv.getOverlays().add(items);

                    }


                }
            }
            catch(IOException e)
            {
                System.out.println (e);
            }

            return true;
        }

        if (item.getItemId() == R.id.InternetLoad) {

            NetLoad n = new NetLoad();
            n.execute();

            return true;
        }


        return false;
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode == 0) {

            if (resultCode == RESULT_OK) {

                Bundle extras = intent.getExtras();

                String name = extras.getString("com.example.toby.solent_eat_out.name");
                String address = extras.getString("com.example.toby.solent_eat_out.address");
                String cuisine = extras.getString("com.example.toby.solent_eat_out.cuisine_type");
                String rating = extras.getString("com.example.toby.solent_eat_out.restaurant_rating");


                OverlayItem restaurant = new OverlayItem(name,  address  + " " + cuisine +" " + rating, new GeoPoint(mv.getMapCenter().getLatitude(), mv.getMapCenter().getLongitude()));

                restaurant.setMarker(getResources().getDrawable(R.drawable.marker));
                items.addItem(restaurant);


            }

        }


    }

//auto preference code
    public void onStart() {
        super.onStart();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean auto = prefs.getBoolean("auto", true);


        if (items != null) {
            if (auto) {


                try {
                    PrintWriter printwriter =
                            new PrintWriter(new FileWriter(Environment.getExternalStorageDirectory().getAbsolutePath() + "/R.csv", true));

                    for (int i = 0; i < items.size(); i++) {
                        OverlayItem itm = items.getItem(i);
                        printwriter.println(itm.getTitle() + "," + itm.getSnippet() + "," + itm.getPoint().getLatitude()
                                + "," + itm.getPoint().getLongitude());
                    }
                    printwriter.close();
                } catch (IOException e) {
                    System.out.println(e);
                }
            }

        }

    }



    class NetLoad extends AsyncTask<Void,Void,String> {


        public String doInBackground(Void... unused) {

            HttpURLConnection conn = null;
            try {

                URL url = new URL("http://www.free-map.org.uk/course/mad/ws/get.php?year=18&username=user011&format=csv ");
                conn = (HttpURLConnection) url.openConnection();
                InputStream in = conn.getInputStream();

                if (conn.getResponseCode() == 200) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    String result = "", line;

                    while ((line = br.readLine()) != null) {

                        String[] comp = line.split(",");

                        if(comp.length==6) {

                            Double lat = Double.valueOf(comp[5]).doubleValue();
                            Double lon = Double.valueOf(comp[4]).doubleValue();

                            OverlayItem restaurant = new OverlayItem(comp[0], comp[1] + comp[2]+ comp[3], new GeoPoint(lat, lon));


                            items.addItem(restaurant);

                        }
                    }
                    return result;
                } else {
                    return "HTTP ERROR: " + conn.getResponseCode();
                }
            } catch (IOException e) {
                return e.toString();
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        }

        public void onPostExecute(String result)
        {



        }
    }
}


