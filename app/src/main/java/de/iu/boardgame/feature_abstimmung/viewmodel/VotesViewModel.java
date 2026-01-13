package de.iu.boardgame.feature_abstimmung.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import de.iu.boardgame.feature_abstimmung.data.GameVoteInfo;
import de.iu.boardgame.feature_abstimmung.data.Vote;
import de.iu.boardgame.feature_abstimmung.data.VotesRepository;

public class VotesViewModel extends AndroidViewModel {

    private final VotesRepository repository;

    public VotesViewModel(@NonNull Application application) {
        super(application);
        repository = VotesRepository.getInstance(application);
    }

    public LiveData<List<GameVoteInfo>> getGames(long meetingId, long userId) {
        return repository.getGamesWithVotes(meetingId, userId);
    }

    public LiveData<Integer> getMyCount(long meetingId, long userId) {
        return repository.getMyVoteCount(meetingId, userId);
    }

    public LiveData<GameVoteInfo> getTopVotedGame(long meetingId) {
        return repository.getTopVotedGame(meetingId);
    }

    /**
     * Toggle mit 3er-Limit. Callback, damit Activity Toast zeigen kann.
     */
    public void toggleVote(long meetingId, long userId, GameVoteInfo game, Runnable onLimitReached) {
        VotesRepository repo = repository;

        // Hintergrund: prüfen + insert/delete
        de.iu.boardgame.feature_termine.data.AppDatabase.databaseWriteExecutor.execute(() -> {
            boolean alreadyVoted = game.isVotedByMe();
            if (alreadyVoted) {
                repo.deleteVote(meetingId, userId, game.id);
            } else {
                int myCount = repo.countVotesByUserSync(meetingId, userId);
                if (myCount >= 3) {
                    if (onLimitReached != null) onLimitReached.run();
                    return;
                }
                repo.insertVote(new Vote(meetingId, userId, game.id));
            }
        });
    }
}
