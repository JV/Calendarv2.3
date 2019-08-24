package com.example.calendarv2;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HolidayViewAdapter extends RecyclerView.Adapter<HolidayViewAdapter.HolidayHolder> {

    private ArrayList<String> mIds = new ArrayList<>();
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mDays = new ArrayList<>();
    private ArrayList<String> mMonths = new ArrayList<>();
    private ArrayList<String> mYears = new ArrayList<>();
    private ArrayList<String> mHolidayRed = new ArrayList<>();
    private ArrayList<String> mHolidayImage = new ArrayList<>();
    private ArrayList<String> mFastingDays = new ArrayList<>();
    private ArrayList<String> mDayOfWeek = new ArrayList<>();
    private ArrayList<String> mIsStepYear = new ArrayList<>();
    private ArrayList<Integer> mMonthChosen = new ArrayList<>();
    private ArrayList<String> mYearChosen = new ArrayList<>();

    private Context context;
    private int daysInSelectedMonthTrt = 30;
    private int daysInSelectedMonthTrtOne = 31;
    private int daysInSelectedMonthTwenNine = 29;
    private int daysInSelectedMonthTwenEight = 28;

    public HolidayViewAdapter(ArrayList<String> mIds, ArrayList<String> mNames, ArrayList<String> mDays, ArrayList<String> mMonths, ArrayList<String> mYears, ArrayList<String> mHolidayRed, ArrayList<String> mHolidayImage, ArrayList<String> mFastingDays, ArrayList<String> mDayOfWeek, ArrayList<String> mIsStepYear, ArrayList<Integer> mMonthChosen, ArrayList<String> mYearChosen, Context context) {

        this.mIds = mIds;
        this.mNames = mNames;
        this.mDays = mDays;
        this.mMonths = mMonths;
        this.mYears = mYears;
        this.mHolidayRed = mHolidayRed;
        this.mHolidayImage = mHolidayImage;
        this.mFastingDays = mFastingDays;
        this.mDayOfWeek = mDayOfWeek;
        this.mIsStepYear = mIsStepYear;
        this.mMonthChosen = mMonthChosen;
        this.mYearChosen = mYearChosen;
        this.context = context;
    }

    @NonNull
    @Override
    public HolidayHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.holiday_item, parent, false);
        HolidayHolder holder = new HolidayHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HolidayHolder holder, int position) {

        holder.tvDate.setText(mDays.get(position) + "." + mMonths.get(position) + "." + mYears.get(position));
        holder.tvDayOfWeek.setText(mDayOfWeek.get(position));
        holder.tvHolidayName.setText(mNames.get(position));

        if (mFastingDays.get(position).equals("Post")) {
            holder.ivFasting.setVisibility(View.VISIBLE);
        } else {
            holder.ivFasting.setVisibility(View.INVISIBLE);
        }

        if (mHolidayRed.get(position).equals("true")) {
            holder.tvHolidayName.setTextColor(Color.RED);
            holder.tvDayOfWeek.setTextColor(Color.RED);
        }

        if (mHolidayImage.get(position).equals("true")) {
            holder.ivFasting.setImageResource(R.drawable.ic_add_black_24dp);
        }
    }

    @Override
    public int getItemCount() {

        int month = Integer.parseInt(mMonthChosen.get(0).toString());
        int year = Integer.parseInt(mYearChosen.get(0));


        switch (month) {

            case 1:
                return daysInSelectedMonthTrtOne;
            case 2:
                if (year % 4 == 0) {
                    return daysInSelectedMonthTwenNine;
                } else {
                    return daysInSelectedMonthTwenEight;

                }
            case 3:
                return daysInSelectedMonthTrtOne;
            case 4:
                return daysInSelectedMonthTrt;
            case 5:
                return daysInSelectedMonthTrtOne;
            case 6:
                return daysInSelectedMonthTrt;
            case 7:
                return daysInSelectedMonthTrtOne;
            case 8:
                return daysInSelectedMonthTrtOne;
            case 9:
                return daysInSelectedMonthTrt;
            case 10:
                return daysInSelectedMonthTrtOne;
            case 11:
                return daysInSelectedMonthTrt;
            case 12:
                return daysInSelectedMonthTrtOne;
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class HolidayHolder extends RecyclerView.ViewHolder{

        RecyclerView recyclerView;
        TextView tvId;
        TextView tvHolidayName;
        boolean isHolidayRed;
        boolean isHolidayImage;
        TextView tvDayOfWeek;
        boolean isFasting;
        ImageView ivFasting;
        TextView tvDate;

        public HolidayHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvDayOfWeek = itemView.findViewById(R.id.tvDayOfWeek);
            tvHolidayName = itemView.findViewById(R.id.tvHolidayName);
            ivFasting = itemView.findViewById(R.id.ivFasting);
            recyclerView = itemView.findViewById(R.id.recyclerviewMain);
        }
    }
}



