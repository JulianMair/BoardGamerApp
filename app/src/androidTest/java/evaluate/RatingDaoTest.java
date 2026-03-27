package evaluate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.iu.boardgame.feature_evaluate.data.MeetingRating;
import de.iu.boardgame.feature_evaluate.data.RatingDao;
import de.iu.boardgame.feature_evaluate.data.RatingWithUser;
import de.iu.boardgame.feature_termine.data.AppDatabase;
import de.iu.boardgame.feature_termine.data.Meeting;
import de.iu.boardgame.feature_termine.data.MeetingDao;
import de.iu.boardgame.feature_user.data.User;
import de.iu.boardgame.feature_user.data.UserDao;
import testutil.LiveDataTestUtil;

@RunWith(AndroidJUnit4.class)
public class RatingDaoTest {

    private AppDatabase database;
    private RatingDao ratingDao;
    private MeetingDao meetingDao;
    private UserDao userDao;

    @Before
    public void setup() {
        Context context = ApplicationProvider.getApplicationContext();
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                .allowMainThreadQueries()
                .build();
        ratingDao = database.ratingDao();
        meetingDao = database.meetingDao();
        userDao = database.userDao();
    }

    @After
    public void tearDown() {
        database.close();
    }

    @Test
    public void insertRating_returnsGeneratedId() {
        long userId = insertTestUser("Anna");
        int meetingId = insertTestMeeting();

        long id = ratingDao.insert(new MeetingRating(
                meetingId, userId, 4f, 5f, 4f, "gut", LocalDateTime.now()
        ));

        assertTrue(id > 0);
    }

    @Test
    public void getRatingsForMeeting_filtersAndOrdersDesc() throws Exception {
        long userId = insertTestUser("Ben");
        int targetMeetingId = insertTestMeeting();
        int otherMeetingId = insertTestMeeting();
        LocalDateTime base = LocalDateTime.now();

        ratingDao.insert(new MeetingRating(targetMeetingId, userId, 2f, 2f, 2f, "old", base));
        ratingDao.insert(new MeetingRating(targetMeetingId, userId, 5f, 5f, 5f, "new", base.plusMinutes(2)));
        ratingDao.insert(new MeetingRating(otherMeetingId, userId, 1f, 1f, 1f, "other", base.plusMinutes(1)));

        List<MeetingRating> ratings = LiveDataTestUtil.getOrAwaitValue(
                ratingDao.getRatingsForMeeting(targetMeetingId),
                value -> value != null && value.size() == 2
        );

        assertEquals(2, ratings.size());
        assertEquals("new", ratings.get(0).getComment());
        assertEquals("old", ratings.get(1).getComment());
    }

    @Test
    public void getRatingsForMeetingWithUser_returnsUsernameFromJoin() throws Exception {
        long userId = insertTestUser("Clara");
        int meetingId = insertTestMeeting();

        ratingDao.insert(new MeetingRating(
                meetingId, userId, 4f, 4f, 4f, "join", LocalDateTime.now()
        ));

        List<RatingWithUser> ratings = LiveDataTestUtil.getOrAwaitValue(
                ratingDao.getRatingsForMeetingWithUser(meetingId),
                value -> value != null && value.size() == 1
        );

        assertEquals(1, ratings.size());
        assertEquals("Clara", ratings.get(0).getUsername());
        assertEquals("join", ratings.get(0).getComment());
    }

    @Test
    public void deleteRating_removesEntry() throws Exception {
        long userId = insertTestUser("David");
        int meetingId = insertTestMeeting();

        ratingDao.insert(new MeetingRating(
                meetingId, userId, 3f, 3f, 3f, "to-delete", LocalDateTime.now()
        ));
        List<MeetingRating> beforeDelete = LiveDataTestUtil.getOrAwaitValue(
                ratingDao.getRatingsForMeeting(meetingId),
                value -> value != null && value.size() == 1
        );

        ratingDao.delete(beforeDelete.get(0));

        List<MeetingRating> afterDelete = LiveDataTestUtil.getOrAwaitValue(
                ratingDao.getRatingsForMeeting(meetingId),
                value -> value != null && value.isEmpty()
        );
        assertTrue(afterDelete.isEmpty());
    }

    private long insertTestUser(String name) {
        return userDao.insert(new User(
                name,
                name.toLowerCase() + "@example.com",
                "0123456789",
                "Teststrasse 1",
                true
        ));
    }

    private int insertTestMeeting() {
        meetingDao.create(new Meeting(
                "Rating-Meeting",
                System.currentTimeMillis() + TimeUnit.HOURS.toMillis(1),
                "Test",
                1L,
                "open"
        ));
        List<Meeting> meetings = meetingDao.getAll();
        return meetings.get(meetings.size() - 1).getMeeting_id();
    }
}
