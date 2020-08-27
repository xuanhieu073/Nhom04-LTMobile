package com.example.gohotel.gps;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;

import com.example.gohotel.R;
import com.example.gohotel.utils.ParamConstants;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GeoCodeService extends IntentService {
    protected ResultReceiver mReceiver;

    public GeoCodeService() {
        super("GeoCodeService");
    }

    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if(intent!=null){
            Bundle bundle=intent.getExtras();
            if (bundle != null) {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                String errorMessage = "";
                double latitude = intent.getDoubleExtra(ParamConstants.LATITUDE_DATA_EXTRA, 0);
                double longitude = intent.getDoubleExtra(ParamConstants.LONGITUDE_DATA_EXTRA, 0);
                mReceiver = intent.getParcelableExtra(ParamConstants.RECEIVER);
                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocation(latitude, longitude, 1);
                    // Handle case where no address was found.
                    if (addresses == null || addresses.size() == 0) {
                        if (errorMessage.isEmpty()) {
                            errorMessage = getString(R.string.no_address_found);
                        }
                        deliverFailureResultToReceiver(ParamConstants.FAILURE_RESULT, errorMessage, latitude, longitude);
                    } else {
                        Address address = addresses.get(0);
                        String addressString = "";
                        // Fetch the address lines using getAddressLine,
                        // join them, and send them to the thread.
                        for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                            addressString += address.getAddressLine(i) + " ";
                        }
                        deliverSuccessResultToReceiver(ParamConstants.SUCCESS_RESULT, addressString, address);
                    }
                } catch (IOException ioException) {
                    errorMessage = getString(R.string.service_not_available);
                    deliverFailureResultToReceiver(ParamConstants.FAILURE_RESULT, errorMessage, latitude, longitude);
                } catch (IllegalArgumentException illegalArgumentException) {
                    // Catch invalid latitude or longitude values.
                    errorMessage = getString(R.string.invalid_lat_long_used);
                    deliverFailureResultToReceiver(ParamConstants.FAILURE_RESULT, errorMessage, latitude, longitude);
                }
            }
        }

    }

    private void deliverFailureResultToReceiver(int resultCode, String message, double latitude, double longitude) {
        Bundle bundle = new Bundle();
        bundle.putString(ParamConstants.RESULT_DATA_KEY, message);
        bundle.putDouble(ParamConstants.LATITUDE_DATA_EXTRA, latitude);
        bundle.putDouble(ParamConstants.LONGITUDE_DATA_EXTRA, longitude);
        mReceiver.send(resultCode, bundle);
    }

    private void deliverSuccessResultToReceiver(int resultCode, String message, Address address) {
        Bundle bundle = new Bundle();
        bundle.putString(ParamConstants.RESULT_DATA_KEY, message);
        bundle.putParcelable(ParamConstants.RESULT_ADDRESS, address);
        mReceiver.send(resultCode, bundle);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
