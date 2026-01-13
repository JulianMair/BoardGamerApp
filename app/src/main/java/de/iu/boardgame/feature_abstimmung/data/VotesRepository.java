package de.iu.boardgame.feature_abstimmung.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import de.iu.boardgame.feature_termine.data.AppDatabase;

public class VotesRepository {

    private static volatile VotesRepository INSTANCE;

    private final VoteDao voteDao;

    private VotesRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        voteDao = db.voteDao();
    }

    public static VotesRepository getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized (VotesRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new VotesRepository(application);
                }
            }
        }
        return INSTANCE;
    }

    public LiveData<List<GameVoteInfo>> getGamesWithVotes(long meetingId, long userId) {
        return voteDao.getGamesWithVotesLive(meetingId, userId);
    }

    public LiveData<Integer> getMyVoteCount(long meetingId, long userId) {
        return voteDao.countVotesByUserLive(meetingId, userId);
    }

    public LiveData<GameVoteInfo> getTopVotedGame(long meetingId) {
        return voteDao.getTopVotedGame(meetingId);
    }

    public void insertVote(Vote vote) {
        AppDatabase.databaseWriteExecutor.execute(() -> voteDao.insertVote(vote));
    }

    public void deleteVote(long meetingId, long userId, long gameId) {
        AppDatabase.databaseWriteExecutor.execute(() -> voteDao.deleteVote(meetingId, userId, gameId));
    }

    public int countVotesByUserSync(long meetingId, long userId) {
        // für Toggle-Logik (Limit 3) im Hintergrund
        return voteDao.countVotesByUser(meetingId, userId);
    }
}
