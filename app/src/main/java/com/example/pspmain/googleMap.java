package com.example.pspmain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class googleMap extends AppCompatActivity implements OnMapReadyCallback{

    boolean isPermissionGranter;
    GoogleMap googleMap;
    ImageView imageViewSearch, imageViewBusiness;
    EditText inputLocation;
    LocationRequest locationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);
        setContentView(R.layout.activity_main);

        imageViewSearch = (ImageView) findViewById(R.id.imageViewSearch);
        imageViewBusiness = (ImageView) findViewById(R.id.imageViewBusiness);
        inputLocation = (EditText) findViewById(R.id.inputlocation);

        checkPermission();

        if (isPermissionGranter) {
            if (checkGooglePlayServices()) {
                SupportMapFragment supportMapFragment = SupportMapFragment.newInstance();
                getSupportFragmentManager().beginTransaction().add(R.id.container, supportMapFragment).commit();
                supportMapFragment.getMapAsync(this);
            } else {
                Toast.makeText(googleMap.this, "Google Play Services Not Available", Toast.LENGTH_SHORT).show();
            }
        }

        imageViewSearch.setOnClickListener(v -> {
            String location = inputLocation.getText().toString();
            if (location == null) {
                Toast.makeText(googleMap.this, "Type in any location name", Toast.LENGTH_SHORT).show();
            } else {
                Geocoder geocoder = new Geocoder(googleMap.this, Locale.getDefault());
                try {
                    List<Address> listAddress = geocoder.getFromLocationName(location, 1);
                    if (listAddress.size() > 0) {
                        LatLng latLng = new LatLng(listAddress.get(0).getLatitude(), listAddress.get(0).getLongitude());

                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.title("Search Location");
                        markerOptions.position(latLng);
                        googleMap.addMarker(markerOptions);
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16);
                        googleMap.animateCamera(cameraUpdate);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        imageViewBusiness.setOnClickListener(v -> {
            LatLng latLng = new LatLng(1.3040348661360348, 103.83224783959925);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.title("Company Location");
            markerOptions.position(latLng);
            googleMap.addMarker(markerOptions);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16);
            googleMap.animateCamera(cameraUpdate);
        });
    }

    private boolean checkGooglePlayServices() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int result = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (result == ConnectionResult.SUCCESS) {
            return true;
        } else if (googleApiAvailability.isUserResolvableError(result)) {
            Dialog dialog = googleApiAvailability.getErrorDialog(this, result, 201, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    Toast.makeText(googleMap.this, "User Cancelled Dialog", Toast.LENGTH_SHORT).show();
                }
            });
            dialog.show();
        }

        return false;
    }

    private void checkPermission() {
        Dexter.withContext(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                isPermissionGranter = true;
                Toast.makeText(googleMap.this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), "");
                intent.setData(uri);
                startActivity(intent);
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                permissionToken.continuePermissionRequest();
            }
        }).check();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        LatLng latLng = new LatLng(1.3040348661360348, 103.83224783959925);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title("Company Location");
        markerOptions.position(latLng);
        googleMap.addMarker(markerOptions);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16);
        googleMap.animateCamera(cameraUpdate);

        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        googleMap.getUiSettings().setScrollGesturesEnabled(true);
        googleMap.getUiSettings().setRotateGesturesEnabled(false);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);

        googleMap.setOnMyLocationButtonClickListener(() -> {
            CheckGps();
            return false;
        });
    }

    private void CheckGps() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
                .setAlwaysShow(true);

        Task<LocationSettingsResponse>locationSettingsResponseTask = LocationServices.getSettingsClient(getApplicationContext())
                .checkLocationSettings(builder.build());

        locationSettingsResponseTask.addOnCompleteListener(task -> {
            try
            {
                LocationSettingsResponse response = task.getResult(ApiException.class);
                Toast.makeText(googleMap.this, "Current Location", Toast.LENGTH_SHORT).show();
                //Request Location from Device
            } catch (ApiException e)
            {
                if (e.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED)
                {
                    ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                    try
                    {
                        resolvableApiException.startResolutionForResult(googleMap.this, 101);
                    } catch (IntentSender.SendIntentException sendIntentException)
                    {
                        sendIntentException.printStackTrace();
                    }
                }
                if (e.getStatusCode() == LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE)
                {
                    Toast.makeText(googleMap.this, "Settings not Available", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101)
        {
            if (resultCode == RESULT_OK)
            {
                Toast.makeText(this, "GPS is enabled", Toast.LENGTH_SHORT).show();
            } if (resultCode == RESULT_CANCELED)
        {
            Toast.makeText(this, "GPS is denied and not enable", Toast.LENGTH_SHORT).show();
        }
        }
    }
}