package de.iu.boardgame.feature_termine.helpers;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

/**
 * Ein Hilfs-Fragment, das den Standard-Android-Kalender als Popup anzeigt.
 * Es erbt von DialogFragment, damit es sich um seinen eigenen Lebenszyklus (Drehen des Handys etc.) kümmert.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    // Variable für den Listener (wird hier aktuell nicht direkt genutzt, da wir getActivity() casten)
    private DatePickerDialog.OnDateSetListener listener;

    /**
     * WICHTIG: Das Interface
     */
    public interface DatePickerListener {
        void onDateSelected(int year, int month, int day);
    }

    /**
     * Wird aufgerufen, sobald sich der Dialog öffnet.
     * Konfiguration von Aussehen und Startwerte.
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Nutzt die aktuelle Systemzeit als Startwert für den Kalender
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Erstellt den eigentlichen Android-Dialog
        // Dieses Fragment hier kümmert sich darum, wenn der User "OK" klickt (OnDateSetListener)
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);

        // Zugriff auf das interne Picker-Widget, um Regeln festzulegen
        DatePicker picker = dialog.getDatePicker();

        // Verhindert, dass man Daten in der Vergangenheit auswählen kann
        // (Systemzeit - 1 Sekunde, damit "Heute" noch klickbar ist)
        picker.setMinDate(System.currentTimeMillis() -1000);

        return dialog;
    }

    /**
     * Diese Methode wird automatisch aufgerufen, wenn der User im Kalender auf "OK" klickt.
     * @return das Ausgewählte Datums
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {

        // Sicherheitscheck: Gibt es die Activity noch? (Könnte bei App-Absturz/Rotation null sein)
        if (getActivity() == null) {
            return;
        }
        // Kommunikation zurück zur Activity (Callback):
        // 1. Prüfen ob das Interface 'DatePickerListener' implementiert ist
        if (getActivity() instanceof DatePickerListener) {
            // 2.Casten von der Activity in das Interface
            // Rückgabe von Daten (Jahr, Monat, Tag) an den Screen .
            ((DatePickerListener) getActivity()).onDateSelected(year, month, day);
        }
    }
}
