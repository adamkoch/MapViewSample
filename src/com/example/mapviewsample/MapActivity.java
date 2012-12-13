package com.example.mapviewsample;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.panorama.PanoramaClient;

import com.example.mapviewsample.provider.StationProvider;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;

import java.util.Hashtable;

public class MapActivity extends FragmentActivity implements GoogleMap.OnMarkerClickListener,
        GoogleMap.OnInfoWindowClickListener, GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener,
        PanoramaClient.OnPanoramaInfoLoadedListener, GoogleMap.OnCameraChangeListener {

    private GoogleMap mMap;
    private PanoramaClient mPanoramaClient;
    private boolean mPlayAnimation = false;
    private Hashtable<String, Integer> mMarkerMap = new Hashtable<String, Integer>();
    private int mCurrentStation = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Check if Play Services is available
        int result = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
        if (result != ConnectionResult.SUCCESS) {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(result, this, 0);
            if (dialog != null) {
                dialog.show();
            } else {
                finish();
            }
        }
        setUpMapIfNeeded();
        mPanoramaClient = new PanoramaClient(this, this, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPanoramaClient.connect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPanoramaClient.disconnect();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment)
                    getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        final View mapView = (getSupportFragmentManager().findFragmentById(R.id.map)).getView();
        mMap.setMyLocationEnabled(true);
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
        mMap.setIndoorEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setOnCameraChangeListener(this);

        if (mapView.getViewTreeObserver().isAlive()) {
            mapView.getViewTreeObserver()
                    .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @SuppressLint("NewApi") // We check which build version we are using.
                        @Override
                        public void onGlobalLayout() {
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(StationProvider.NEWYORK));
                            mMap.moveCamera(CameraUpdateFactory.zoomTo(12));
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                                mapView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                            } else {
                                mapView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            }
                        }
                    });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.menu_previous):
                mCurrentStation = --mCurrentStation % StationProvider.STATIONS.size();
                loadMarkersForStation(StationProvider.STATIONS.get(mCurrentStation));
                break;
            case (R.id.menu_next):
                mCurrentStation = ++mCurrentStation % StationProvider.STATIONS.size();
                loadMarkersForStation(StationProvider.STATIONS.get(mCurrentStation));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadMarkersForStation(StationProvider.StationInfo stationInfo) {
        mMap.clear();
        mMarkerMap.clear();
        if (stationInfo == null) {
            return;
        }
        LatLngBounds.Builder bounds = new LatLngBounds.Builder();
        Marker newMarker;
        for (int i = 0; i < stationInfo.entrances.size(); i++) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(stationInfo.entrances.get(i));
            bounds.include(stationInfo.entrances.get(i));
            markerOptions.title(stationInfo.line);
            markerOptions.snippet(stationInfo.title);
            newMarker = mMap.addMarker(markerOptions);
            if (!mMarkerMap.containsKey(newMarker.getId())) {
                mMarkerMap.put(newMarker.getId(), stationInfo.panorama);
            }
        }
        mPlayAnimation = true;
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 17), 1000, null);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        String id = marker.getId();
        if (mMarkerMap.containsKey(id)) {
            int panoResId = mMarkerMap.get(marker.getId());
            Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + panoResId);
            mPanoramaClient.loadPanoramaInfo(this, uri);
        }
    }

    @Override
    public void onConnected() {
    }

    @Override
    public void onDisconnected() {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    public void onPanoramaInfoLoaded(ConnectionResult result, Intent intent) {
        if (result.isSuccess()) {
            if (intent != null) {
                startActivity(intent);
            } else {
                // Error
            }
        }
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        if (mPlayAnimation) {
            mPlayAnimation = false;
            // Play a simple animation, rotating 180 degrees while zooming out and tilting the camera
            CameraPosition newPosition = new CameraPosition.Builder(cameraPosition)
                    .bearing(cameraPosition.bearing - 180)
                    .tilt(35F)
                    .zoom(cameraPosition.zoom - 1)
                    .build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(newPosition), 4000, null);
        }
    }
}
