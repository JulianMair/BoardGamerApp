package evaluate;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.iu.boardgame.R;
import de.iu.boardgame.feature_evaluate.data.MeetingRating;
import de.iu.boardgame.feature_evaluate.data.RatingDao;
import de.iu.boardgame.feature_evaluate.ui.RatingAtivity;
import de.iu.boardgame.feature_termine.data.AppDatabase;
import de.iu.boardgame.feature_termine.data.Meeting;
import de.iu.boardgame.feature_termine.data.MeetingDao;
import de.iu.boardgame.feature_user.data.User;
import de.iu.boardgame.feature_user.data.UserDao;
import de.iu.boardgame.feature_user.helpers.SessionManager;
import testutil.LiveDataTestUtil;

@RunWith(AndroidJUnit4.class)
public class RatingBusinessRuleTest {

    private AppDatabase database;
    private RatingDao ratingDao;
    private MeetingDao meetingDao;
    private UserDao userDao;
    private Context context;

    private int meetingId;
    private long userId;

    @Before
    public void setup() throws Exception {
        context = ApplicationProvider.getApplicationContext();
        database = AppDatabase.getDatabase(context);
        ratingDao = database.ratingDao();
        meetingDao = database.meetingDao();
        userDao = database.userDao();

        userId = userDao.insert(new User(
                "BusinessTestUser",
                "business@test.local",
                "000",
                "Test",
                true
        ));
        meetingDao.create(new Meeting(
                "Business Rule Meeting",
                System.currentTimeMillis() + TimeUnit.HOURS.toMillis(1),
                "Test",
                userId,
                "open"
        ));
        List<Meeting> meetings = meetingDao.getAll();
        meetingId = meetings.get(meetings.size() - 1).getMeeting_id();

        SessionManager.setCurrentUserId(context, userId);

        // Sicherstellen, dass der Startzustand leer ist.
        List<MeetingRating> ratings = LiveDataTestUtil.getOrAwaitValue(
                ratingDao.getRatingsForMeeting(meetingId),
                value -> value != null
        );
        if (!ratings.isEmpty()) {
            for (MeetingRating rating : ratings) {
                ratingDao.delete(rating);
            }
        }
    }

    @After
    public void tearDown() {
        meetingDao.deleteById(meetingId);
        userDao.deleteById(userId);
        SessionManager.clearCurrentUserId(context);
    }

    @Test
    public void saveRating_withoutComment_doesNotPersistRating() throws Exception {
        Intent intent = new Intent(context, RatingAtivity.class);
        intent.putExtra("meeting_id", meetingId);

        try (ActivityScenario<RatingAtivity> ignored = ActivityScenario.launch(intent)) {
            onView(withId(R.id.btnSaveRating)).perform(click());
        }

        // Warte kurz, damit ein fehlerhaft asynchrones Insert sichtbar würde.
        Thread.sleep(800);

        List<MeetingRating> ratingsAfter = LiveDataTestUtil.getOrAwaitValue(
                ratingDao.getRatingsForMeeting(meetingId),
                value -> value != null
        );

        assertTrue(ratingsAfter.isEmpty());
    }

    @Test
    public void saveRating_withComment_persistsRating() throws Exception {
        Intent intent = new Intent(context, RatingAtivity.class);
        intent.putExtra("meeting_id", meetingId);

        try (ActivityScenario<RatingAtivity> ignored = ActivityScenario.launch(intent)) {
            onView(withId(R.id.editComment)).perform(replaceText("Guter Abend"), closeSoftKeyboard());
            onView(withId(R.id.btnSaveRating)).perform(click());
        }

        List<MeetingRating> ratingsAfter = LiveDataTestUtil.getOrAwaitValue(
                ratingDao.getRatingsForMeeting(meetingId),
                value -> value != null && value.size() == 1
        );

        assertEquals(1, ratingsAfter.size());
        assertEquals("Guter Abend", ratingsAfter.get(0).getComment());
        assertEquals(userId, ratingsAfter.get(0).getUserId());
        assertEquals(meetingId, ratingsAfter.get(0).getMeetingId());
        assertTrue(ratingsAfter.get(0).getTimestamp().isBefore(LocalDateTime.now().plusSeconds(1)));
    }
}
