package com.example.gohotel.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gohotel.GoHotelApplication;
import com.example.gohotel.R;
import com.example.gohotel.activity.ChooseAreaActivity;
import com.example.gohotel.activity.HotelDetailActivity;
import com.example.gohotel.activity.SortFilterActivity;
import com.example.gohotel.adapter.HotelAdapter;
import com.example.gohotel.dialog.DialogLoadingProgress;
import com.example.gohotel.model.HotelForm;
import com.example.gohotel.utils.ParamConstants;
import com.example.gohotel.utils.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private String address;
    private RecyclerView rcvHotel;
    private ImageView imgSort;
    private Context context;
    private int offset = 0;
    private HotelAdapter hotelAdapter;
    private List<HotelForm> hotelForms;
    private LinearLayout layoutChooseArea;
    private int priceStart;
    private int priceEnd = 3000000;
    private int typeSort = 0;
    private int city = 0;
    private String provine;

    public int getCity() {
        return city;
    }

    public void setCity(int city) {
        this.city = city;
    }

    public int getDistrict() {
        return district;
    }

    public void setDistrict(int district) {
        this.district = district;
    }

    private int district = 0;

    public int getPriceStart() {
        return priceStart;
    }

    public int getPriceEnd() {
        return priceEnd;
    }

    public void setPriceEnd(int priceEnd) {
        this.priceEnd = priceEnd;
    }

    TextView tvChooseArea;

    public static HomeFragment newInstance(String address) {
        HomeFragment myFragment = new HomeFragment();

        Bundle args = new Bundle();
        args.putString("address", address);
        myFragment.setArguments(args);

        return myFragment;
    }

    public void resetHotel() {
        hotelForms = new ArrayList<>();
        offset = 0;
        if (hotelAdapter != null) {
            hotelAdapter.notifyDataSetChanged();
            hotelAdapter = null;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
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
        View rootView = inflater.inflate(R.layout.home_fragment, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ((Activity) context).getWindow().setStatusBarColor(context.getResources().getColor(R.color.colorWhite));
            }
        }

        TextView tvAddress = rootView.findViewById(R.id.tvAddress);
        imgSort = rootView.findViewById(R.id.imgSort);
        imgSort.setOnClickListener(this);
        rcvHotel = rootView.findViewById(R.id.rcvHotel);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rcvHotel.setLayoutManager(linearLayoutManager);
        rcvHotel.setHasFixedSize(true);
        tvChooseArea = rootView.findViewById(R.id.tvChooseArea);
        layoutChooseArea = rootView.findViewById(R.id.layoutChooseArea);
        layoutChooseArea.setOnClickListener(view -> gotoChooseArea());
        tvAddress.setText(address);
        getHotelHome();
        return rootView;

    }

    private void gotoChooseArea() {
        Intent intent = new Intent(context, ChooseAreaActivity.class);
        getActivity().startActivityForResult(intent, ParamConstants.REQUEST_CHOOSE_AREA_HOME);
    }

    private void getHotelHome() {
        String lat = PreferenceUtils.getLatLocation(context);
        String longtidue = PreferenceUtils.getLongLocation(context);
        DialogLoadingProgress.getInstance().show(context);

        GoHotelApplication.serviceApi.getHotelHomeDistance(lat, longtidue, offset, GoHotelApplication.limit, priceStart, priceEnd, "ASC").enqueue(new Callback<List<HotelForm>>() {
            @Override
            public void onResponse(Call<List<HotelForm>> call, Response<List<HotelForm>> response) {
                DialogLoadingProgress.getInstance().hide();

                if (response.code() == 200) {
                    List<HotelForm> hotelForms = response.body();
                    if (hotelForms != null && hotelForms.size() > 0) {
                        handleListHotel(hotelForms);
                    }
                } else {
                    Toast.makeText(context, "Không thể lấy danh sách khách sạn", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<HotelForm>> call, Throwable t) {
                DialogLoadingProgress.getInstance().hide();

                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getHotelByPriceDESC() {
        if (district != 0) {
            getHotelByPriceDESCAndDistrict();
        } else if (city != 0) {
            getHotelByPriceDESCAndCity();
        } else getHotelByPriceDESCAndAllHotel();
    }

    private void getHotelByPriceDESCAndAllHotel() {
        String lat = PreferenceUtils.getLatLocation(context);
        String longtidue = PreferenceUtils.getLongLocation(context);

        DialogLoadingProgress.getInstance().show(context);

        GoHotelApplication.serviceApi.getHotelHome(lat, longtidue, offset, GoHotelApplication.limit, priceStart, priceEnd, "DESC").enqueue(new Callback<List<HotelForm>>() {
            @Override
            public void onResponse(Call<List<HotelForm>> call, Response<List<HotelForm>> response) {
                DialogLoadingProgress.getInstance().hide();

                if (response.code() == 200) {
                    List<HotelForm> hotelForms = response.body();
                    if (hotelForms != null && hotelForms.size() > 0) {
                        handleListHotel(hotelForms);
                    }
                } else {
                    Toast.makeText(context, "Không thể lấy danh sách khách sạn", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<HotelForm>> call, Throwable t) {
                DialogLoadingProgress.getInstance().hide();

                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getHotelByPriceDESCAndCity() {
        String lat = PreferenceUtils.getLatLocation(context);
        String longtidue = PreferenceUtils.getLongLocation(context);

        DialogLoadingProgress.getInstance().show(context);

        GoHotelApplication.serviceApi.getHotelHome(lat, longtidue, offset, GoHotelApplication.limit, city, priceStart, priceEnd, "DESC").enqueue(new Callback<List<HotelForm>>() {
            @Override
            public void onResponse(Call<List<HotelForm>> call, Response<List<HotelForm>> response) {
                DialogLoadingProgress.getInstance().hide();

                if (response.code() == 200) {
                    List<HotelForm> hotelForms = response.body();
                    if (hotelForms != null && hotelForms.size() > 0) {
                        handleListHotel(hotelForms);
                    }
                } else {
                    Toast.makeText(context, "Không thể lấy danh sách khách sạn", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<HotelForm>> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                DialogLoadingProgress.getInstance().hide();

            }
        });
    }

    private void getHotelByPriceDESCAndDistrict() {
        String lat = PreferenceUtils.getLatLocation(context);
        String longtidue = PreferenceUtils.getLongLocation(context);

        DialogLoadingProgress.getInstance().show(context);

        GoHotelApplication.serviceApi.getHotelHome(lat, longtidue, offset, GoHotelApplication.limit, city, district, priceStart, priceEnd, "ASC").enqueue(new Callback<List<HotelForm>>() {
            @Override
            public void onResponse(Call<List<HotelForm>> call, Response<List<HotelForm>> response) {
                DialogLoadingProgress.getInstance().hide();

                if (response.code() == 200) {
                    List<HotelForm> hotelForms = response.body();
                    if (hotelForms != null && hotelForms.size() > 0) {
                        handleListHotel(hotelForms);
                    }
                } else {
                    Toast.makeText(context, "Không thể lấy danh sách khách sạn", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<HotelForm>> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                DialogLoadingProgress.getInstance().hide();

            }
        });
    }

    public void getHotelByPriceASC() {
        if (district != 0) {
            getHotelByPriceASCAndDistrict();
        } else if (city != 0) {
            getHotelByPriceASCAndCity();
        } else getHotelByPriceASCAndAllHotel();
    }

    public void getHotelByPriceASCAndAllHotel() {
        String lat = PreferenceUtils.getLatLocation(context);
        String longtidue = PreferenceUtils.getLongLocation(context);

        DialogLoadingProgress.getInstance().show(context);

        GoHotelApplication.serviceApi.getHotelHome(lat, longtidue, offset, GoHotelApplication.limit, priceStart, priceEnd, "ASC").enqueue(new Callback<List<HotelForm>>() {
            @Override
            public void onResponse(Call<List<HotelForm>> call, Response<List<HotelForm>> response) {
                DialogLoadingProgress.getInstance().hide();

                if (response.code() == 200) {
                    List<HotelForm> hotelForms = response.body();
                    if (hotelForms != null && hotelForms.size() > 0) {
                        handleListHotel(hotelForms);
                    }
                } else {
                    Toast.makeText(context, "Không thể lấy danh sách khách sạn", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<HotelForm>> call, Throwable t) {
                DialogLoadingProgress.getInstance().hide();

                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getHotelByPriceASCAndCity() {
        String lat = PreferenceUtils.getLatLocation(context);
        String longtidue = PreferenceUtils.getLongLocation(context);

        DialogLoadingProgress.getInstance().show(context);

        GoHotelApplication.serviceApi.getHotelHome(lat, longtidue, offset, GoHotelApplication.limit, city, priceStart, priceEnd, "ASC").enqueue(new Callback<List<HotelForm>>() {
            @Override
            public void onResponse(Call<List<HotelForm>> call, Response<List<HotelForm>> response) {
                DialogLoadingProgress.getInstance().hide();


                if (response.code() == 200) {
                    List<HotelForm> hotelForms = response.body();
                    if (hotelForms != null && hotelForms.size() > 0) {
                        handleListHotel(hotelForms);
                    }
                } else {
                    Toast.makeText(context, "Không thể lấy danh sách khách sạn", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<HotelForm>> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                DialogLoadingProgress.getInstance().hide();

            }
        });
    }

    public void getHotelByPriceASCAndDistrict() {
        String lat = PreferenceUtils.getLatLocation(context);
        String longtidue = PreferenceUtils.getLongLocation(context);

        DialogLoadingProgress.getInstance().show(context);

        GoHotelApplication.serviceApi.getHotelHome(lat, longtidue, offset, GoHotelApplication.limit, city, district, priceStart, priceEnd, "ASC").enqueue(new Callback<List<HotelForm>>() {
            @Override
            public void onResponse(Call<List<HotelForm>> call, Response<List<HotelForm>> response) {
                DialogLoadingProgress.getInstance().hide();

                if (response.code() == 200) {
                    List<HotelForm> hotelForms = response.body();
                    if (hotelForms != null && hotelForms.size() > 0) {
                        handleListHotel(hotelForms);
                    }
                } else {
                    Toast.makeText(context, "Không thể lấy danh sách khách sạn", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<HotelForm>> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                DialogLoadingProgress.getInstance().hide();

            }
        });
    }

    private void handleListHotel(List<HotelForm> hotelForms) {
        if (this.hotelForms == null)
            this.hotelForms = new ArrayList<>();
        this.hotelForms.addAll(hotelForms);
        if (hotelAdapter != null) {
            hotelAdapter.notifyItemRangeInserted(offset, hotelForms.size());
        } else {
            hotelAdapter = new HotelAdapter(context, this.hotelForms, this::gotoHotelDetail);
            rcvHotel.setAdapter(hotelAdapter);
        }
        offset = this.hotelForms.size();
    }

    private void gotoHotelDetail(HotelForm hotelForm) {
        Intent intent = new Intent(context, HotelDetailActivity.class);
        intent.putExtra("hotelKey", hotelForm.getIdHotel());
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgSort:
                gotoSortFilter();
                break;
        }
    }

    private void gotoSortFilter() {
        Intent intent = new Intent(getContext(), SortFilterActivity.class);
        getActivity().startActivityForResult(intent, ParamConstants.REQUEST_SORT_FILTER);
    }

    public void setPriceStart(int priceStart) {
        this.priceStart = priceStart;
    }

    public int getTypeSort() {
        return typeSort;
    }

    public void setTypeSort(int typeSort) {
        this.typeSort = typeSort;
    }

    public void getHotelByRating() {
        if (district != 0) {
            getHotelByRatingAndDistrict();
        } else if (city != 0) {
            getHotelByRatingAndCity();
        } else getHotelByRatingAndAllHotel();
    }

    private void getHotelByRatingAndCity() {
        String lat = PreferenceUtils.getLatLocation(context);
        String longtidue = PreferenceUtils.getLongLocation(context);

        DialogLoadingProgress.getInstance().show(context);

        GoHotelApplication.serviceApi.getHotelHomeStar(lat, longtidue, offset, GoHotelApplication.limit, city, priceStart, priceEnd, "DESC").enqueue(new Callback<List<HotelForm>>() {
            @Override
            public void onResponse(Call<List<HotelForm>> call, Response<List<HotelForm>> response) {
                DialogLoadingProgress.getInstance().hide();

                if (response.code() == 200) {
                    List<HotelForm> hotelForms = response.body();
                    if (hotelForms != null && hotelForms.size() > 0) {
                        handleListHotel(hotelForms);
                    }
                } else {
                    Toast.makeText(context, "Không thể lấy danh sách khách sạn", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<HotelForm>> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                DialogLoadingProgress.getInstance().hide();

            }
        });
    }

    private void getHotelByRatingAndAllHotel() {
        String lat = PreferenceUtils.getLatLocation(context);
        String longtidue = PreferenceUtils.getLongLocation(context);

        DialogLoadingProgress.getInstance().show(context);

        GoHotelApplication.serviceApi.getHotelHomeStar(lat, longtidue, offset, GoHotelApplication.limit, priceStart, priceEnd, "DESC").enqueue(new Callback<List<HotelForm>>() {
            @Override
            public void onResponse(Call<List<HotelForm>> call, Response<List<HotelForm>> response) {
                DialogLoadingProgress.getInstance().hide();

                if (response.code() == 200) {
                    List<HotelForm> hotelForms = response.body();
                    if (hotelForms != null && hotelForms.size() > 0) {
                        handleListHotel(hotelForms);
                    }
                } else {
                    Toast.makeText(context, "Không thể lấy danh sách khách sạn", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<HotelForm>> call, Throwable t) {
                DialogLoadingProgress.getInstance().hide();

                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getHotelByRatingAndDistrict() {
        String lat = PreferenceUtils.getLatLocation(context);
        String longtidue = PreferenceUtils.getLongLocation(context);

        DialogLoadingProgress.getInstance().show(context);

        GoHotelApplication.serviceApi.getHotelHomeStar(lat, longtidue, offset, GoHotelApplication.limit, city, district, priceStart, priceEnd, "DESC").enqueue(new Callback<List<HotelForm>>() {
            @Override
            public void onResponse(Call<List<HotelForm>> call, Response<List<HotelForm>> response) {
                DialogLoadingProgress.getInstance().hide();

                if (response.code() == 200) {
                    List<HotelForm> hotelForms = response.body();
                    if (hotelForms != null && hotelForms.size() > 0) {
                        handleListHotel(hotelForms);
                    }
                } else {
                    Toast.makeText(context, "Không thể lấy danh sách khách sạn", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<HotelForm>> call, Throwable t) {
                DialogLoadingProgress.getInstance().hide();

                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public String getProvine() {
        return provine;
    }

    public void setProvine(String provine) {
        this.provine = provine;
    }

    public void getHotelByDistance() {
        if (district != 0) {
            getHotelByDistanceAndDistrict();
        } else if (city != 0) {
            getHotelByDistanceAndCity();
        } else getHotelByDistanceAndAllHotel();
    }

    private void getHotelByDistanceAndAllHotel() {
        String lat = PreferenceUtils.getLatLocation(context);
        String longtidue = PreferenceUtils.getLongLocation(context);

        DialogLoadingProgress.getInstance().show(context);

        GoHotelApplication.serviceApi.getHotelHomeDistance(lat, longtidue, offset, GoHotelApplication.limit, priceStart, priceEnd, "ASC").enqueue(new Callback<List<HotelForm>>() {
            @Override
            public void onResponse(Call<List<HotelForm>> call, Response<List<HotelForm>> response) {
                if (response.code() == 200) {
                    List<HotelForm> hotelForms = response.body();
                    if (hotelForms != null && hotelForms.size() > 0) {
                        handleListHotel(hotelForms);
                    }
                } else {
                    Toast.makeText(context, "Không thể lấy danh sách khách sạn", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<HotelForm>> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getHotelByDistanceAndCity() {
        String lat = PreferenceUtils.getLatLocation(context);
        String longtidue = PreferenceUtils.getLongLocation(context);

        DialogLoadingProgress.getInstance().show(context);

        GoHotelApplication.serviceApi.getHotelHomeDistance(lat, longtidue, offset, GoHotelApplication.limit, city, priceStart, priceEnd, "DESC").enqueue(new Callback<List<HotelForm>>() {
            @Override
            public void onResponse(Call<List<HotelForm>> call, Response<List<HotelForm>> response) {
                DialogLoadingProgress.getInstance().hide();

                if (response.code() == 200) {
                    List<HotelForm> hotelForms = response.body();
                    if (hotelForms != null && hotelForms.size() > 0) {
                        handleListHotel(hotelForms);
                    }
                } else {
                    Toast.makeText(context, "Không thể lấy danh sách khách sạn", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<HotelForm>> call, Throwable t) {
                DialogLoadingProgress.getInstance().hide();

                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getHotelByDistanceAndDistrict() {
        String lat = PreferenceUtils.getLatLocation(context);
        String longtidue = PreferenceUtils.getLongLocation(context);

        DialogLoadingProgress.getInstance().show(context);

        GoHotelApplication.serviceApi.getHotelHomeDistance(lat, longtidue, offset, GoHotelApplication.limit, city, district, priceStart, priceEnd, "ASC").enqueue(new Callback<List<HotelForm>>() {
            @Override
            public void onResponse(Call<List<HotelForm>> call, Response<List<HotelForm>> response) {
                DialogLoadingProgress.getInstance().hide();

                if (response.code() == 200) {
                    List<HotelForm> hotelForms = response.body();
                    if (hotelForms != null && hotelForms.size() > 0) {
                        handleListHotel(hotelForms);
                    }
                } else {
                    Toast.makeText(context, "Không thể lấy danh sách khách sạn", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<HotelForm>> call, Throwable t) {
                DialogLoadingProgress.getInstance().hide();

                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
