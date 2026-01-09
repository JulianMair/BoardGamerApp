package de.iu.boardgame.feature_termine.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import de.iu.boardgame.feature_termine.data.Meeting;
import de.iu.boardgame.feature_user.data.User;

public class NextHostCalculator {

    /**
     * Berechnet, wer als nächstes dran ist.
     * Logik: Wer hat am wenigsten Spieleabende ausgerichtet?
     *
     * @param meetings Die Liste aller bisherigen Meetings
     * @param users Eine Liste aller aktiven Gruppenmitglieder-IDs
     * @return Den Users, der dran ist.
     */
    public static User calculateNextHostId(List<Meeting> meetings, List<User> users) {
        Random random = new Random();
        int randomInt=0;

        // Wenn es keine Nutzer gibt return null
        if(users == null || users.isEmpty()){
            return null;
        }

        // Erstellen einer Liste mit den Usern die als Host in frage kommen
        List<User> validHost = new ArrayList<>();
        for(User user : users){
            if(user.isHostPossible){
                validHost.add(user);
            }
        }

        // Wenn es keinen User gibt, der als Host in Frage kommt
        if(validHost.isEmpty()){
            return null;
        }

        // Fall A es gibt keine Meetings
        // -> ein random User wird ermittelt
        if(meetings == null || meetings.isEmpty()) {
            randomInt = random.nextInt(validHost.size());
            return validHost.get(randomInt);
        }

        // Fall B es gibt bereits Meetings
        // Map aufbauen: ID -> Anzahl
        Map<Long, Integer> hostCounts = new HashMap<>();
        // // Alle User mit 0 initialisieren
        for (User user : validHost) {
            hostCounts.put(user.id, 0);
        }

        // Meetings zählen
        for (Meeting m : meetings) {
            long hostId = m.getHost_id();
            if (hostCounts.containsKey(hostId)) {
                int currentCount = hostCounts.get(hostId);
                hostCounts.put(hostId, currentCount + 1);
            }
        }

        // Map in eine Liste umwandeln
        List<Map.Entry<Long, Integer>> sortedValues = new ArrayList<>(hostCounts.entrySet());
        // Liste sortieren
        sortedValues.sort(Map.Entry.comparingByValue());
        int minValue = sortedValues.get(0).getValue();

        // Alle User mit dem kleinsten Wert sammeln
        List<Long> canidates = new ArrayList<>();
        for(Map.Entry<Long, Integer> entry: sortedValues){
            if(entry.getValue() == minValue){
                canidates.add(entry.getKey());
            }
            else {
                break;
            }
        }

        //Zufällig einen Kanidaten auswählen
        randomInt = random.nextInt(canidates.size());

        // Das passende User-Objekt zur ID finden und zurückgeben
        for (User user : users) {
            if (user.id == canidates.get(randomInt)) {
                return user;
            }
        }
        return null; // Sollte nie ereicht werden
    }


}
