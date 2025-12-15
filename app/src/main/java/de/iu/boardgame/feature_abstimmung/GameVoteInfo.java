package de.iu.boardgame.feature_abstimmung;

import androidx.annotation.NonNull;

public class GameVoteInfo {
    public long id;

    @NonNull public String name;
    public int durationMinutes;
    @NonNull public String category;

    public int voteCount;
    public int votedByMe; // 0/1

    public boolean isVotedByMe() {
        return votedByMe > 0;
    }
}
