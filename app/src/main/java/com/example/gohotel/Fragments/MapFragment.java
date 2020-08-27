package com.example.gohotel.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.gohotel.GoHotelApplication;
import com.example.gohotel.R;
import com.example.gohotel.activity.ChooseAreaActivity;
import com.example.gohotel.adapter.HotelMapAdapter;
import com.example.gohotel.dialog.DialogLoadingProgress;
import com.example.gohotel.gps.GeoCodeService;
import com.example.gohotel.model.HotelForm;
import com.example.gohotel.model.MarkerWrapper;
import com.example.gohotel.utils.AddressResultReceiver;
import com.example.gohotel.utils.ParamConstants;
import com.example.gohotel.utils.PreferenceUtils;
import com.example.gohotel.utils.TextOnDrawable;
import com.example.gohotel.utils.Utils;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapFragment extends Fragment {
    private TextView tvAddress;
    private String address;
    private GoogleMap mMap;
    private Context context;
    private double latitude;
    private double longitude;
    private float currentZoom = 12.0f;
    private List<MarkerWrapper> markers = new ArrayList<>();
    private Marker previousMarkerClick = null;
    private AddressResultReceiver mResultReceiver;
    private double mapLat, mapLng;
    private LatLng currentPosition;
    private LinearLayout layoutChooseArea;
    private RecyclerView rcvHotel;
    private LinearLayoutManager linearLayoutManager;
    private HotelMapAdapter hotelMapAdapter;
    private int width;
    private List<HotelForm> hotelForms;
    private TextView tvChooseArea;
    private boolean filterDistrict;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public static MapFragment newInstance(String address) {
        MapFragment myFragment = new MapFragment();

        Bundle args = new Bundle();
        args.putString("address", address);
        myFragment.setArguments(args);

        return myFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            address = getArguments().getString("address");
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.map_fragment, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ((Activity)context).getWindow().setStatusBarColor(context.getResources().getColor(R.color.colorWhite));
            }
        }

        tvAddress = rootView.findViewById(R.id.tvAddress);
        tvAddress.setText(address);
        tvChooseArea = rootView.findViewById(R.id.tvChooseArea);
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;

        String lastLocation = PreferenceUtils.getLatLocation(context);
        if (!lastLocation.equals("")) {
            latitude = Double.parseDouble(PreferenceUtils.getLatLocation(context));
            longitude = Double.parseDouble(PreferenceUtils.getLongLocation(context));
        }
        currentPosition = new LatLng(latitude, longitude);
        rcvHotel = rootView.findViewById(R.id.rcvHotel);
        linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        rcvHotel.setHasFixedSize(true);
        rcvHotel.setLayoutManager(linearLayoutManager);
        hotelMapAdapter = new HotelMapAdapter(context, null, width);
        rcvHotel.setAdapter(hotelMapAdapter);
        hotelMapAdapter.notifyDataSetChanged();
        LinearSnapHelper linearSnapHelper = new LinearSnapHelper();
        linearSnapHelper.attachToRecyclerView(rcvHotel);
        rcvHotel.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == 0) {
                    if (rcvHotel.getLayoutManager() != null) {
                        int potition = (((LinearLayoutManager) rcvHotel.getLayoutManager()).findFirstCompletelyVisibleItemPosition());
                        List<HotelForm> hotelForms = hotelMapAdapter.getHotelFormList();
                        if (potition != -1 && hotelForms != null && hotelForms.size() > 0) {
                            if (hotelForms.size() > potition) {
                                HotelForm hotelForm = hotelForms.get(potition);
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.valueOf(hotelForm.getLatitude()),
                                        Double.valueOf(hotelForm.getLongitude())), currentZoom));
                            }
                        }
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        setUpMapIfNeeded();
        mResultReceiver = new AddressResultReceiver(new Handler(), (province, messageResult) -> {
            try {
                // when choose district move to distrct call result
                // countshow district when user choose distrct countshowdistrict =0
                if (province.equals("") && messageResult.equals(getString(R.string.service_not_available))) {
                    startGeoCodeIntentService(mapLat, mapLng);
                } else {
                    if (!filterDistrict)
                        getHotelMap();
                }
            } catch (Exception e) {
            }
        });
        layoutChooseArea = rootView.findViewById(R.id.layoutChooseArea);
        layoutChooseArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoChooseArea();
            }
        });
        return rootView;
    }

    private void gotoChooseArea() {
        Intent intent = new Intent(context, ChooseAreaActivity.class);
        getActivity().startActivityForResult(intent, ParamConstants.REQUEST_CHOOSE_AREA_MAP);
    }


    protected void startGeoCodeIntentService(double lat, double lng) {
        if (getActivity() != null) {
            Intent intent = new Intent(getActivity(), GeoCodeService.class);
            intent.putExtra(ParamConstants.RECEIVER, mResultReceiver);
            intent.putExtra(ParamConstants.LATITUDE_DATA_EXTRA, lat);
            intent.putExtra(ParamConstants.LONGITUDE_DATA_EXTRA, lng);
            getActivity().startService(intent);
        }
    }

    private void setUpMapIfNeeded() {
        if (getActivity() != null && mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            SupportMapFragment mapFragment = null;

            try {
                mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (mapFragment != null) {
                mapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        mMap = googleMap;
                        // Check if we were successful in obtaining the map.
                        if (mMap != null) {
                            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }

                            mMap.setMyLocationEnabled(true);
                            mMap.getUiSettings().setMapToolbarEnabled(false);
                            mMap.getUiSettings().setCompassEnabled(true);
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);

                            // move camera to current position
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,
                                    longitude), currentZoom));
                            Log.d("radius", "onCreateView: " + radiusMap() / 1000);

                            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                                @Override
                                public void onMapClick(LatLng latLng) {
//                                    hideHotelForm();
                                }
                            });

                            mMap.setOnCameraIdleListener(() -> {
                                try {

                                    if (currentZoom != mMap.getCameraPosition().zoom || currentPosition != mMap.getCameraPosition().target) {
                                        currentZoom = mMap.getCameraPosition().zoom;
                                        currentPosition = mMap.getCameraPosition().target;
                                        mapLat = currentPosition.latitude;
                                        mapLng = currentPosition.longitude;
                                        startGeoCodeIntentService(mapLat, mapLng);
                                    }
                                } catch (Exception e) {
                                }
                            });
                            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                @Override
                                public boolean onMarkerClick(Marker marker) {
                                    return false;
                                }
                            });

                        }

                    }
                });


            }

        }
    }

    private void getHotelMap() {
        String lat = PreferenceUtils.getLatLocation(context);
        String longlati = PreferenceUtils.getLongLocation(context);
        DialogLoadingProgress.getInstance().show(context);

        GoHotelApplication.serviceApi.getHotelMap(lat, longlati, radiusMap() / 1000).enqueue(new Callback<List<HotelForm>>() {
            @Override
            public void onResponse(Call<List<HotelForm>> call, Response<List<HotelForm>> response) {
                DialogLoadingProgress.getInstance().hide();

                if (response.code() == 200) {
                    hotelForms = response.body();
                    if (hotelForms != null && hotelForms.size() > 0) {
                        LatLngBounds.Builder builder = new LatLngBounds.Builder();

                        if (markers != null) {
                            markers.clear();

                        }
                        for (HotelForm hotelForm : hotelForms) {
                            LatLng latLng = new LatLng(Double.valueOf(hotelForm.getLatitude()), Double.valueOf(hotelForm.getLongitude()));
                            setupMarkerForMap(hotelForm, latLng);
                            builder.include(latLng);
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<List<HotelForm>> call, Throwable t) {
                DialogLoadingProgress.getInstance().hide();

            }
        });
    }

    private void setupMarkerForMap(final HotelForm hotelForm, final LatLng latLng) {
        try {
            String fileName = "marker_green";
            String price = getPricePromotion(hotelForm);

            if (context != null) {
                final int idResource = getResources().getIdentifier(fileName, "drawable", context.getPackageName());


                final MarkerWrapper currentMarker = isExitMarker(hotelForm.getHotelId());

                //Create new Maker
                if (currentMarker == null && mMap != null) {
                    TextOnDrawable textOnDrawable = new TextOnDrawable(Objects.requireNonNull(getActivity()), idResource, price, bitmap -> {
                        try {
                            if (bitmap != null) {

                                MarkerOptions markerOptions = new MarkerOptions();
                                markerOptions.position(latLng);
//                                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));

                                MarkerWrapper markerWrapper = new MarkerWrapper(hotelForm, mMap.addMarker(markerOptions));
                                markers.add(markerWrapper);
                                markerWrapper.getMarker().setVisible(true);

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });

                    textOnDrawable.execute();

                } else {
                    if (previousMarkerClick != null && mMap != null) {
                        final MarkerWrapper previousMarker = findHotelSn(previousMarkerClick);
                        if (previousMarker != null && previousMarker.getHotelForm() != null && previousMarker.getHotelForm().getHotelId() == currentMarker.getHotelForm().getHotelId()) {
                            final int identifier = getResources().getIdentifier("marker_onclick", "drawable", context.getPackageName());
                            TextOnDrawable textOnDrawable = new TextOnDrawable(context, identifier, "-1", bitmap -> {
                                try {
                                    bitmap = Bitmap.createScaledBitmap(bitmap, 60, 80, false);
                                    currentMarker.getMarker().setIcon(BitmapDescriptorFactory.fromBitmap(bitmap));
                                } catch (Exception ignored) {
                                }
                            });
                            textOnDrawable.execute();
                        }
                    } else {
                        if (mMap != null) {
                            currentMarker.setHotelForm(hotelForm);
                            TextOnDrawable textOnDrawable = new TextOnDrawable(Objects.requireNonNull(getActivity()), idResource, price, bitmap -> {
                                try {
                                    if (bitmap != null) {

                                        currentMarker.getMarker().setIcon(BitmapDescriptorFactory.fromBitmap(bitmap));
                                    }
                                } catch (Exception ignored) {

                                }


                            });
                            textOnDrawable.execute();
                        }

                    }
                    if (currentMarker != null && currentMarker.getMarker() != null) {
                        currentMarker.getMarker().setVisible(true);
                    }
                }

            }
        } catch (Exception ignored) {

        }


    }

    private MarkerWrapper findHotelSn(Marker marker) {
        for (int i = 0; i < markers.size(); i++) {
            if (marker.getId().equals(markers.get(i).getMarker().getId())) {
                return markers.get(i);
            }
        }
        return null;
    }

    private MarkerWrapper isExitMarker(int hotelId) {
        for (int i = 0; i < markers.size(); i++) {
            if (markers.get(i).getHotelForm().getHotelId() == hotelId) {
                return markers.get(i);
            }
        }
        return null;
    }

    private String getPricePromotion(HotelForm hotelForm) {
        String price = "0K";
        //Get Price

        if (hotelForm.getPriceRoomPerDay() > 0) {

            price = String.valueOf(Utils.formatCurrencyK(hotelForm.getPriceRoomPerDay()));

        }
        return "";
//        return price;
    }

    public float radiusMap() {
        try {
            if (mMap != null) {
                VisibleRegion visibleRegion = mMap.getProjection().getVisibleRegion();
                LatLng farRight = visibleRegion.farRight;
                LatLng farLeft = visibleRegion.farLeft;
                LatLng nearRight = visibleRegion.nearRight;
                LatLng nearLeft = visibleRegion.nearLeft;

                float[] distanceWidth = new float[2];
                Location.distanceBetween(
                        (farRight.latitude + nearRight.latitude) / 2,
                        (farRight.longitude + nearRight.longitude) / 2,
                        (farLeft.latitude + nearLeft.latitude) / 2,
                        (farLeft.longitude + nearLeft.longitude) / 2,
                        distanceWidth
                );


                float[] distanceHeight = new float[2];
                Location.distanceBetween(
                        (farRight.latitude + nearRight.latitude) / 2,
                        (farRight.longitude + nearRight.longitude) / 2,
                        (farLeft.latitude + nearLeft.latitude) / 2,
                        (farLeft.longitude + nearLeft.longitude) / 2,
                        distanceHeight
                );

                if (distanceWidth[0] > distanceHeight[0]) {
                    return distanceWidth[0];
                } else {
                    return distanceHeight[0];
                }
            }
            return 0;
        } catch (Exception e) {
            return 0;
        }
    }

    private void hideHotelForm() {
        try {
            if (rcvHotel != null && rcvHotel.getVisibility() == View.VISIBLE) {
                Animation slide_down = AnimationUtils.loadAnimation(context,
                        R.anim.hotel_slide_down);
                rcvHotel.setVisibility(View.GONE);
                slide_down.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        rcvHotel.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                rcvHotel.startAnimation(slide_down);
            }
        } catch (Exception ignored) {

        }

    }


    private void showHotelForm(int hotelSn) {
        if (rcvHotel != null && rcvHotel.getVisibility() == View.GONE && linearLayoutManager != null) {
            if (hotelForms != null && hotelForms.size() > 0) {
                hotelMapAdapter.updateData(hotelForms);
                hotelMapAdapter.notifyDataSetChanged();
            }
            int potition = findPotition(hotelSn, hotelForms);
            rcvHotel.scrollToPosition(potition);

            Animation slide_up = AnimationUtils.loadAnimation(getContext(),
                    R.anim.hotel_slide_up);
            slide_up.setFillBefore(true);
            slide_up.setFillAfter(true);
            rcvHotel.setVisibility(View.VISIBLE);
            slide_up.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    rcvHotel.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            rcvHotel.startAnimation(slide_up);
        } else if (rcvHotel != null && rcvHotel.getVisibility() == View.VISIBLE && linearLayoutManager != null) {
            if (hotelForms != null && hotelForms.size() > 0) {
                hotelMapAdapter.updateData(hotelForms);
                hotelMapAdapter.notifyDataSetChanged();
            }
            int potition = findPotition(hotelSn, hotelForms);
            rcvHotel.scrollToPosition(potition);
        }

    }

    private int findPotition(int hotelSn, List<HotelForm> hotelFormList) {
        if (hotelFormList != null) {
            int size = hotelFormList.size();
            for (int i = 0; i < size; i++) {
                if (hotelSn == hotelFormList.get(i).getHotelId()) return i;
            }
        }
        return 0;
    }

    public void getHotelCityDistrict(int city, int district, String districtName) {
        tvChooseArea.setText(districtName);
        String lat = PreferenceUtils.getLatLocation(context);
        String longlati = PreferenceUtils.getLongLocation(context);
        DialogLoadingProgress.getInstance().show(context);

        GoHotelApplication.serviceApi.getHotelMap(lat, longlati, radiusMap() / 1000, city, district).enqueue(new Callback<List<HotelForm>>() {
            @Override
            public void onResponse(Call<List<HotelForm>> call, Response<List<HotelForm>> response) {
                DialogLoadingProgress.getInstance().hide();

                if (response.code() == 200) {
                    hotelForms = response.body();
                    if (hotelForms != null && hotelForms.size() > 0) {
                        LatLngBounds.Builder builder = new LatLngBounds.Builder();

                        if (markers != null) {
                            markers.clear();
                        }
                        for (HotelForm hotelForm : hotelForms) {
                            LatLng latLng = new LatLng(Double.valueOf(hotelForm.getLatitude()), Double.valueOf(hotelForm.getLongitude()));
                            setupMarkerForMap(hotelForm, latLng);
                            builder.include(latLng);
                        }
                        filterDistrict = true;

                        LatLngBounds bounds = builder.build();
                        int height = getResources().getDisplayMetrics().heightPixels - 3 * getResources().getDimensionPixelSize(R.dimen.map_icon_size);
                        int width = getResources().getDisplayMetrics().widthPixels - getResources().getDimensionPixelSize(R.dimen.map_icon_size);
                        int padding = (int) (width * 0.10);
                        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
                        mMap.animateCamera(cu);
                    }
                }

            }

            @Override
            public void onFailure(Call<List<HotelForm>> call, Throwable t) {
                DialogLoadingProgress.getInstance().hide();

            }
        });
    }

    public void getHotelCity(int city, String cityName) {
        tvChooseArea.setText(cityName);
        String lat = PreferenceUtils.getLatLocation(context);
        String longlati = PreferenceUtils.getLongLocation(context);
        DialogLoadingProgress.getInstance().show(context);

        GoHotelApplication.serviceApi.getHotelMap(lat, longlati, radiusMap() / 1000, city).enqueue(new Callback<List<HotelForm>>() {
            @Override
            public void onResponse(Call<List<HotelForm>> call, Response<List<HotelForm>> response) {
                DialogLoadingProgress.getInstance().hide();

                if (response.code() == 200) {
                    hotelForms = response.body();
                    if (hotelForms != null && hotelForms.size() > 0) {
                        LatLngBounds.Builder builder = new LatLngBounds.Builder();

                        if (markers != null) {
                            markers.clear();
                        }
                        for (HotelForm hotelForm : hotelForms) {
                            LatLng latLng = new LatLng(Double.valueOf(hotelForm.getLatitude()), Double.valueOf(hotelForm.getLongitude()));
                            setupMarkerForMap(hotelForm, latLng);
                            builder.include(latLng);
                        }
                        filterDistrict = true;
                        LatLngBounds bounds = builder.build();
                        int height = getResources().getDisplayMetrics().heightPixels - 3 * getResources().getDimensionPixelSize(R.dimen.map_icon_size);
                        int width = getResources().getDisplayMetrics().widthPixels - getResources().getDimensionPixelSize(R.dimen.map_icon_size);
                        int padding = (int) (width * 0.10);
                        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
                        mMap.animateCamera(cu);
                    }
                }

            }

            @Override
            public void onFailure(Call<List<HotelForm>> call, Throwable t) {
                DialogLoadingProgress.getInstance().hide();

            }
        });
    }
}
