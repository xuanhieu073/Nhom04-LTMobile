package com.example.gohotel.utils;

import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

public class AddressResultReceiver extends ResultReceiver {
    private ResultCallback callback;

    public AddressResultReceiver(Handler handler, ResultCallback callback) {
        super(handler);
        this.callback = callback;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        super.onReceiveResult(resultCode, resultData);
        String province = "";
        try {
            if (resultCode == ParamConstants.SUCCESS_RESULT) {
                Address address = resultData.getParcelable(ParamConstants.RESULT_ADDRESS);
                if (address != null && address.getAdminArea() != null) {
                    province = address.getAdminArea();
                }
            }
            callback.onFinishedResult(province, resultData.getString(ParamConstants.RESULT_DATA_KEY));
        } catch (Exception e) {
        }
    }
    public interface ResultCallback  {
        void onFinishedResult(String province, String messageResult);
    }
}
