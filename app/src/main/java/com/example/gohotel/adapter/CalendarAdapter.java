package com.example.gohotel.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.example.gohotel.R;
import com.example.gohotel.widgets.Calendar.CellDayClickListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CalendarAdapter extends PagerAdapter {
    private Context context;
    private Calendar calendar, calendarSelected, minDate;
    private SimpleDateFormat monthFormat;
    private CellDayClickListener cellDayClickListener;

    public CalendarAdapter(Context context, Calendar calendar, Calendar calendarSelected, Calendar minDate, CellDayClickListener cellDayClickListener) {
        this.context = context;
        this.calendarSelected = calendarSelected;
        this.minDate = minDate;
        this.cellDayClickListener = cellDayClickListener;
        this.calendar = calendar;
        monthFormat = new SimpleDateFormat(context.getString(R.string.month_year_format_view));
    }

    public void updateCalendarSelected(Calendar calendarSelected) {
        this.calendarSelected = calendarSelected;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.calendar_view, container, false);
        TextView tvCurrentMonth = view.findViewById(R.id.tvCurrentMonth);
        GridView calendarView = view.findViewById(R.id.calendar);
        int month, year;
        Calendar currentMonth = Calendar.getInstance();
        currentMonth.setTimeInMillis(this.calendar.getTimeInMillis());
        currentMonth.add(Calendar.MONTH, position);
        month = currentMonth.get(Calendar.MONTH) + 1;
        year = currentMonth.get(Calendar.YEAR);

        tvCurrentMonth.setText(monthFormat.format(currentMonth.getTime()));

        CalendarCellAdapter adapter = new CalendarCellAdapter(context, month, year, calendarSelected, minDate, cellDayClickListener);
        calendarView.setAdapter(adapter);
        container.addView(view);

        return view;
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        //super.destroyItem(container, position, object);
        container.removeView((View) object);
    }
}

