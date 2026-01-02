package de.iu.boardgame.feature_termine.helpers;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import java.util.Calendar;

/**
 * Ein wiederverwendbarer Dialog zur Auswahl einer Uhrzeit.
 * Erbt von DialogFragment, damit er sich wie ein normales Android-Popup verhält
 * (z.B. bleibt er offen, wenn man das Handy dreht).
 */
public class TimePickerFragment extends DialogFragment
                                implements TimePickerDialog.OnTimeSetListener{

    // Interne Referenz (wird hier nicht zwingend gebraucht, da wir via Activity kommunizieren)
    private TimePickerDialog.OnTimeSetListener listener;

    /**
     * DAS INTERFACE:
     * Jede Activity (Screen), die diesen TimePicker öffnen will, MUSS dieses Interface implementieren.
     * Das garantiert uns, dass die Activity eine Methode 'onTimeSelected' hat,
     * an die wir die gewählte Zeit zurückschicken können.
     */
    public interface TimePickerListener {
        void onTimeSelected(int hour, int minute);
    }

    /**
     * Baut den Dialog zusammen, bevor er angezeigt wird.
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Wir holen uns die aktuelle Zeit vom System, damit der Picker nicht bei 00:00 startet,
        // sondern bei der jetzigen Uhrzeit
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Erstellt den Android-Standard TimePicker.
        // 'DateFormat.is24HourFormat': Prüft, ob der User am Handy 12h (AM/PM) oder 24h eingestellt hat
        // und passt den Dialog automatisch daran an.
        // TODO: Auf 24h ändern
        return new TimePickerDialog(getActivity(), this, hour, minute, true);
    }

    /**
     * Callback: Wird aufgerufen, wenn der User auf "OK" drückt.
     * @param view Der Picker selbst (wird meist ignoriert)
     * @param hourOfDay Die gewählte Stunde (0-23)
     * @param minute Die gewählte Minute
     */
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Weiterleitung an die Activity:

        // 1. Sicherheitscheck: Implementiert die Activity unser Interface?
        if (getActivity() instanceof TimePickerListener) {
            // 2. Casten der Activity in das Interface und Aufruf der Methode.
            // So landen die Daten (Stunde, Minute) wieder im Haupt-Screen.
            ((TimePickerListener) getActivity()).onTimeSelected(hourOfDay, minute);
        }
    }
}

