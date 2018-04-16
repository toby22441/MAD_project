package com.example.toby.solent_eat_out;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

        import android.location.LocationManager;
import android.location.LocationListener;

        import java.util.ArrayList;

        public class MapOverlayGpsActivity extends Activity implements LocationListener {
  private static final String LOG_TAG = MapOverlayGpsActivity.class.getName();


            MapView mv;
   ItemizedIconOverlay<OverlayItem> items;
   ItemizedIconOverlay.OnItemGestureListener<OverlayItem> markerGestureListener;

            @Override
    protected void onCreate(Bundle savedInstanceState) {
              super.onCreate(savedInstanceState);

                        // This line sets the user agent, a requirement to download OSM maps
                       Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));

                        setContentView(R.layout.activity_overlay_map_app);
              mv = (MapView) findViewById(R.id.overlayappmap);
                mv.setBuiltInZoomControls(true);

                        mv.getController().setZoom(14);

                       markerGestureListener = new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
            public boolean onItemLongPress(int i, OverlayItem item) {
                                Toast.makeText(MapOverlayGpsActivity.this, item.getSnippet(), Toast.LENGTH_SHORT).show();
                                return true;
                            }

                  public boolean onItemSingleTapUp(int i, OverlayItem item) {
                                Toast.makeText(MapOverlayGpsActivity.this, item.getSnippet(), Toast.LENGTH_SHORT).show();
                               return true;
                           }
       };

                 // set up location manager
                                LocationManager mgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                        // default crown pub 50.9319 , -1.4011
                                double lat=50.9319;
              double lon=-1.4011;
                       Location location =  mgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
              if (location!=null){
                     lat = location.getLatitude();
                      lon = location.getLongitude();
                 }

                        // center location
                                mv.getController().setCenter(new GeoPoint(lat, lon));
                items = new ItemizedIconOverlay<OverlayItem>(this, new ArrayList<OverlayItem>(), markerGestureListener);

                      OverlayItem mylocation = new OverlayItem("My Location", "where I am", new GeoPoint(lat, lon));
               // NOTE is just this.getDrawable() if supporting API 21+ only
                        mylocation.setMarker(getResources().getDrawable(R.drawable.marker));
               items.addItem(mylocation);

                        mv.getOverlays().add(items);

                        // set up location listener
                                mgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

                    }

            public void onLocationChanged(Location newLoc) {

                        double lat = newLoc.getLatitude();
                        double lon = newLoc.getLongitude();

                        // center location
                                mv.getController().setCenter(new GeoPoint(lat, lon));

                      OverlayItem mylocation = new OverlayItem("My Location", "where I am", new GeoPoint(lat, lon));
               // NOTE is just this.getDrawable() if supporting API 21+ only
                        mylocation.setMarker(getResources().getDrawable(R.drawable.marker));

                       items.removeAllItems();
                items.addItem(mylocation);

                       Toast.makeText(this, "Location=" +
                                                newLoc.getLatitude() + " " +
                                                newLoc.getLongitude(), Toast.LENGTH_LONG).show();
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


        }}
