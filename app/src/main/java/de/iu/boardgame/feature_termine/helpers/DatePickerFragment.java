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
     * WICHTIG: Das Interface (Der Vertrag).
     * Jede Activity, die diesen DatePicker nutzen will, MUSS dieses Interface implementieren.
     * So weiß das Fragment: "Egal wer mich aufruft, er hat auf jeden Fall die Methode 'onDateSelected'".
     */
    public interface DatePickerListener {
        void onDateSelected(int year, int month, int day);
    }

    /**
     * Wird aufgerufen, sobald sich der Dialog öffnet.
     * Hier konfigurieren wir das Aussehen und die Startwerte.
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // // Wir nutzen die aktuelle Systemzeit als Startwert für den Kalender
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Erstellt den eigentlichen Android-Dialog
        // 'this' bedeutet: Dieses Fragment hier kümmert sich darum, wenn der User "OK" klickt (OnDateSetListener)
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
     * Hier schicken wir die gewählten Daten zurück an die Activity.
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {

        // Sicherheitscheck: Gibt es die Activity noch? (Könnte bei App-Absturz/Rotation null sein)
        if (getActivity() == null) {
            return;
        }
        // Kommunikation zurück zur Activity (Callback):
        // 1. Wir prüfen: Hat die Activity unser Interface 'DatePickerListener' implementiert?
        if (getActivity() instanceof DatePickerListener) {
            // (Hinweis: Dein Log-Text war hier etwas verwirrend, eigentlich ist Activity hier NICHT null)
            // 2. Wir 'casten' die Activity in das Interface und rufen die Methode auf.
            // Damit übergeben wir die Daten (Jahr, Monat, Tag) an den Screen zurück.
            ((DatePickerListener) getActivity()).onDateSelected(year, month, day);
        }
    }
}
