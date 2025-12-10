package de.iu.boardgame.feature_termine.helpers;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment {
    int selectedYear;
    int selecteDay;
    int selectedMonth;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Use the current date as the default date in the picker.
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it.
        return new DatePickerDialog(
                getActivity(),
                (DatePickerDialog.OnDateSetListener) getActivity(),
                year,
                month,
                day);
    }
    public void onDateSet(DatePicker view, int year, int month, int day) {
        this.selecteDay = day;
        this.selectedMonth = month;
        this.selectedYear = year;
    }

    public void setDay(int day){
        this.selecteDay = day;
    }

    public void setMonth(int month){
        this.selectedMonth = month;
    }

    public void setYear(int year){
        this.selectedYear = year;
    }

    public int getDay () {
        return selecteDay;
    }

    public int getMonth() {
        return selectedMonth;
    }

    public int getYear(){
        return selectedYear;
    }


}
