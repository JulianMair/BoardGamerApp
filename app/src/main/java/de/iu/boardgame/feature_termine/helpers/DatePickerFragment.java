package de.iu.boardgame.feature_termine.helpers;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private DatePickerDialog.OnDateSetListener listener;

    public interface DatePickerListener {
        void onDateSelected(int year, int month, int day);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Use the current date as the default date in the picker.
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it.
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
        DatePicker picker = dialog.getDatePicker();

        picker.setMinDate(System.currentTimeMillis() -1000);

        return dialog;
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {

        android.util.Log.d("DEBUG_DATE", "Fragment: onDateSet wurde aufgerufen!");

        if (getActivity() == null) {
            return;
        }
        // prüfen, ob die Activity das Interface implementiert
        if (getActivity() instanceof DatePickerListener) {
            android.util.Log.e("DEBUG_DATE", "Fragment: getActivity() ist NULL!");
            ((DatePickerListener) getActivity()).onDateSelected(year, month, day);
        }
    }
}
