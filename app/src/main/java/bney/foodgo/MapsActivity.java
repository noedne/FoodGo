package bney.foodgo;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap
        .OnMarkerClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient
        .OnConnectionFailedListener, GetSource.AsyncResponse {

    private static final LatLng CMU_LOC = new LatLng(40.4435, -79.9435);

    private GoogleMap mMap;
    private GoogleApiClient mClient;
    private Location mLastLocation;
    private ArrayList<DiningLocation> locs;

    /*String url = "http://webapps.studentaffairs.cmu.edu/dining/ConceptInfo/?page=searchConcepts&OpenNow=on";
    OkHttpClient client = new OkHttpClient();
    Call call = client.newCall(request);
    try (Response response = call.execute()) {

    }*/


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        new GetSource(this).execute();
        final GetSource.AsyncResponse th = this;

        final ImageButton refresh = (ImageButton) findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new GetSource(th).execute();
            }
        });
    }

    //this override the implemented method from AsyncResponse
    @Override
    public void processFinish(String output){
        //Here you will receive the result fired from async class
        //of onPostExecute(result) methods
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Log.d("LOCNAME: ", output);
        locs = new ParseSource().parse(output);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        int duration = 2000;
        int initialZoom = 10;
        int finalZoom = 15;
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(CMU_LOC, initialZoom));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(finalZoom), duration, null);
        mMap.setMyLocationEnabled(true);
        mMap.setOnMarkerClickListener(this);

        displayMarkers();
    }

    private void displayMarkers() {
        /*LatLng[] MOCK_DATA = new LatLng[]{new LatLng(40.4443963, -79.944846),
                new LatLng(40.4428963, -79.941846), new LatLng(40.4435963, -79.945846)};

        for (LatLng ll : MOCK_DATA) {
            mMap.addMarker(new MarkerOptions().title("CMU").snippet("bruh").position(ll));
        }*/
        mMap.clear();
        for (int i = 0; i < locs.size(); i++) {
            LatLng l1 = new LatLng(locs.get(i).nums[0], locs.get(i).nums[1]);
            String name = locs.get(i).name;
            mMap.addMarker(new MarkerOptions().title(name).position(l1));
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        //Toast.makeText(this, "Dope", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mClient);
        Log.d("Connection", "Last location is :" + mLastLocation);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        //mClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mClient.isConnected()) {
            mClient.disconnect();
        }
    }

}