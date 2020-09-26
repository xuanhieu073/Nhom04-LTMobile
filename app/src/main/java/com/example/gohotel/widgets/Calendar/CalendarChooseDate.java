package com.example.gohotel.widgets.Calendar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.gohotel.R;
import com.example.gohotel.adapter.CalendarAdapter;
import com.example.gohotel.utils.AppTimeUtils;
import com.example.gohotel.utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CalendarChooseDate {
    private TextView tvButtonLeft, tvButtonRight;
    private Context context;
    private static CalendarChooseDate Instance = null;
    private String dateFrom, dateTo;
    private ViewPager vpCalendarFrom, vpCalendarTo;
    CalendarAdapter calendarAdapterFrom, calendarAdapterTo;
    SimpleDateFormat sdf = new SimpleDateFormat(AppTimeUtils.ddMMyyyy2, Locale.ENGLISH);


    public static CalendarChooseDate getInstance() {
        if (Instance == null) {
            Instance = new CalendarChooseDate();
        }
        return Instance;
    }

    private CellDayClickListener cellDayClickListenerFrom = date -> {
        //dialog.dismiss();
        dateFrom = date;

        chooseCurrentDate(date, sdf, calendarAdapterFrom);
        Date dateBooking = Utils.convertStringToDate(dateFrom
                , AppTimeUtils.ddMMyyyy2);
        java.util.Calendar calendarSelected = Calendar.getInstance();
        calendarSelected.setTime(dateBooking);
        Date dateBookingTo = Utils.convertStringToDate(dateTo
                , AppTimeUtils.ddMMyyyy2);
        java.util.Calendar calendarSelectedTo = Calendar.getInstance();
        calendarSelectedTo.setTime(dateBookingTo);
        if (calendarSelected.getTime().getTime() >= calendarSelectedTo.getTime().getTime()) {
            calendarSelected.add(Calendar.DAY_OF_MONTH, 1);

            dateTo = sdf.format(calendarSelected.getTime());
            tvButtonRight.setText(dateTo);
        }
        //callbackCalendarValue.CallbackCalendar(date);
        //checkHourlyReservation(date);
        tvButtonLeft.setText(date);
        //getCoupon();

    };

    private CellDayClickListener cellDayClickListenerTo = date -> {
        //dialog.dismiss();
        dateTo = date;

        chooseCurrentDate(date, sdf, calendarAdapterTo);
        Date dateBooking = Utils.convertStringToDate(dateTo
                , AppTimeUtils.ddMMyyyy2);
        java.util.Calendar calendarSelected = Calendar.getInstance();
        calendarSelected.setTime(dateBooking);
        Date dateBookingFrom = Utils.convertStringToDate(dateFrom
                , AppTimeUtils.ddMMyyyy2);
        java.util.Calendar calendarSelectedFrom = Calendar.getInstance();
        calendarSelectedFrom.setTime(dateBookingFrom);
        if (calendarSelected.getTime().getTime() <= calendarSelectedFrom.getTime().getTime()) {
            calendarSelected.add(Calendar.DAY_OF_MONTH, -1);
            dateFrom = sdf.format(calendarSelected.getTime());
            tvButtonLeft.setText(dateFrom);
        }
        //callbackCalendarValue.CallbackCalendar(date);
        //checkHourlyReservation(date);
        tvButtonRight.setText(date);
        //getCoupon();

    };

    private void chooseCurrentDate(String date, SimpleDateFormat sdf, CalendarAdapter calendarAdapter) {
        Date date2 = null;
        try {
            date2 = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(date2);
        if (calendarAdapter != null) {
            calendarAdapter.updateCalendarSelected(cal);
        }
    }


    public void show(Context context, String startDate, String endDate, boolean isRight, final CallbackResulCalendar callbackResulCalendar) {
        if (context != null && context instanceof Activity && !((Activity) context).isFinishing()) {
            this.context = context;
            final Dialog dialog = new Dialog(context, R.style.dialog_full_transparent_background);
            dialog.setOwnerActivity((Activity) context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.calendar_choose_date);
            Window window = dialog.getWindow();
            if (window != null) {
                WindowManager.LayoutParams wlp = window.getAttributes();
                wlp.gravity = Gravity.CENTER;
                window.setAttributes(wlp);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                dialog.show();
                dateFrom = startDate;
                dateTo = endDate;
                // FIND VIEW BY ID
                dialog.findViewById(R.id.dialog_clock).setOnClickListener(view -> dialog.dismiss());
                tvButtonLeft = dialog.findViewById(R.id.tvButtonLeft);
                tvButtonLeft.setText(startDate);
                tvButtonLeft.setOnClickListener(v -> changeTypeChoose(1));
                tvButtonRight = dialog.findViewById(R.id.tvButtonRight);
                tvButtonRight.setText(endDate);
                tvButtonRight.setOnClickListener(v -> changeTypeChoose(2));
                dialog.findViewById(R.id.tv_clock_ok).setOnClickListener(v -> {
                    dialog.dismiss();
                    callbackResulCalendar.CallbackResult(dateFrom, dateTo);
                });

                vpCalendarFrom = dialog.findViewById(R.id.vpCalendarFrom);
                vpCalendarTo = dialog.findViewById(R.id.vpCalendarTo);
                final java.util.Calendar calendar = java.util.Calendar.getInstance();
                Date date2 = null;
                try {
                    date2 = sdf.parse(startDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                java.util.Calendar calendarSelected = java.util.Calendar.getInstance();
                calendarSelected.setTime(date2);
                java.util.Calendar minDate = java.util.Calendar.getInstance();

                Date dateBooking = Utils.convertStringToDate(endDate
                        , AppTimeUtils.ddMMyyyy2);
                java.util.Calendar calendarSelectedTo = Calendar.getInstance();
                calendarSelectedTo.setTime(dateBooking);

                calendarAdapterFrom = new CalendarAdapter(context, calendar, calendarSelected, minDate, cellDayClickListenerFrom);
                vpCalendarFrom.setAdapter(calendarAdapterFrom);
                java.util.Calendar minDateTo = java.util.Calendar.getInstance();
                minDateTo.add(Calendar.DAY_OF_MONTH, 1);
                calendarAdapterTo = new CalendarAdapter(context, calendar, calendarSelectedTo, minDateTo, cellDayClickListenerTo);
                vpCalendarTo.setAdapter(calendarAdapterTo);
                if (isRight) {
                    changeTypeChoose(2);
                } else {
                    changeTypeChoose(1);
                }
            }
        }
    }

    private void changeTypeChoose(int i) {
        final java.util.Calendar calendar = java.util.Calendar.getInstance();
        java.util.Calendar minDate = java.util.Calendar.getInstance();
        if (i == 1) {
            tvButtonRight.setBackground(context.getResources().getDrawable(R.drawable.bg_cornor_default));
            tvButtonLeft.setBackground(context.getResources().getDrawable(R.drawable.bg_primary_cornor));
            tvButtonRight.setTextColor(context.getResources().getColor(R.color.lightGrey));
            tvButtonLeft.setTextColor(context.getResources().getColor(R.color.colorWhite));
            Date dateBooking = Utils.convertStringToDate(dateFrom
                    , AppTimeUtils.ddMMyyyy2);
            java.util.Calendar calendarSelected = Calendar.getInstance();
            calendarSelected.setTime(dateBooking);
            vpCalendarTo.setVisibility(View.GONE);
            vpCalendarFrom.setVisibility(View.VISIBLE);
            calendarAdapterFrom = new CalendarAdapter(context, calendar, calendarSelected, minDate, cellDayClickListenerFrom);
            vpCalendarFrom.setAdapter(calendarAdapterFrom);
        } else {
            Date dateBooking = Utils.convertStringToDate(dateTo
                    , AppTimeUtils.ddMMyyyy2);
            java.util.Calendar calendarSelected = Calendar.getInstance();
            calendarSelected.setTime(dateBooking);

            tvButtonRight.setBackground(context.getResources().getDrawable(R.drawable.bg_primary_cornor));
            tvButtonLeft.setBackground(context.getResources().getDrawable(R.drawable.bg_cornor_default));
            tvButtonRight.setTextColor(context.getResources().getColor(R.color.colorWhite));
            tvButtonLeft.setTextColor(context.getResources().getColor(R.color.lightGrey));
            vpCalendarTo.setVisibility(View.VISIBLE);
            vpCalendarFrom.setVisibility(View.GONE);
            java.util.Calendar minDateTo = java.util.Calendar.getInstance();
            minDateTo.add(Calendar.DAY_OF_MONTH, 1);
            calendarAdapterTo = new CalendarAdapter(context, calendar, calendarSelected, minDateTo, cellDayClickListenerTo);
            vpCalendarTo.setAdapter(calendarAdapterTo);
        }
    }
}
