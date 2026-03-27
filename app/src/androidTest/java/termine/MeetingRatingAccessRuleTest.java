package termine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import de.iu.boardgame.R;
import de.iu.boardgame.feature_termine.data.AppDatabase;
import de.iu.boardgame.feature_termine.data.Meeting;
import de.iu.boardgame.feature_termine.data.MeetingDao;
import de.iu.boardgame.feature_termine.ui.MeetingDetailActivity;
import de.iu.boardgame.feature_user.data.User;
import de.iu.boardgame.feature_user.data.UserDao;
import de.iu.boardgame.feature_user.helpers.SessionManager;

@RunWith(AndroidJUnit4.class)
public class MeetingRatingAccessRuleTest {

    private Context context;
    private AppDatabase database;
    private MeetingDao meetingDao;
    private UserDao userDao;

    private final List<Integer> createdMeetingIds = new ArrayList<>();
    private final List<Long> createdUserIds = new ArrayList<>();

    @Before
    public void setup() {
        context = ApplicationProvider.getApplicationContext();
        database = AppDatabase.getDatabase(context);
        meetingDao = database.meetingDao();
        userDao = database.userDao();
    }

    @After
    public void tearDown() {
        for (Integer meetingId : createdMeetingIds) {
            meetingDao.deleteById(meetingId);
        }
        for (Long userId : createdUserIds) {
            userDao.deleteById(userId);
        }
        SessionManager.clearCurrentUserId(context);
    }

    @Test
    public void futureMeeting_hidesRateButton() throws Exception {
        long hostId = createUser("HostFuture");
        long guestId = createUser("GuestFuture");
        SessionManager.setCurrentUserId(context, guestId);
        int meetingId = createMeetingWithTimestamp(
                System.currentTimeMillis() + TimeUnit.DAYS.toMillis(2),
                hostId
        );

        Intent intent = new Intent(context, MeetingDetailActivity.class);
        intent.putExtra("MEETING_ID", meetingId);

        try (ActivityScenario<MeetingDetailActivity> scenario = ActivityScenario.launch(intent)) {
            int visibility = waitForRateButtonVisibility(scenario, false);
            assertNotEquals(View.VISIBLE, visibility);
        }
    }

    @Test
    public void pastMeeting_showsRateButton() throws Exception {
        long hostId = createUser("HostPast");
        long guestId = createUser("GuestPast");
        SessionManager.setCurrentUserId(context, guestId);
        int meetingId = createMeetingWithTimestamp(
                System.currentTimeMillis() - TimeUnit.DAYS.toMillis(2),
                hostId
        );

        Intent intent = new Intent(context, MeetingDetailActivity.class);
        intent.putExtra("MEETING_ID", meetingId);

        try (ActivityScenario<MeetingDetailActivity> scenario = ActivityScenario.launch(intent)) {
            int visibility = waitForRateButtonVisibility(scenario, true);
            assertEquals(View.VISIBLE, visibility);
        }
    }

    private long createUser(String name) {
        long id = userDao.insert(new User(
                name,
                name.toLowerCase() + "@example.com",
                "000",
                "Test",
                true
        ));
        createdUserIds.add(id);
        return id;
    }

    private int createMeetingWithTimestamp(long timestamp, long hostId) {
        Set<Integer> before = new HashSet<>();
        for (Meeting meeting : meetingDao.getAll()) {
            before.add(meeting.getMeeting_id());
        }

        meetingDao.create(new Meeting(
                "Rating Access Rule",
                timestamp,
                "Test Location",
                hostId,
                "open"
        ));

        int insertedId = -1;
        for (Meeting meeting : meetingDao.getAll()) {
            if (!before.contains(meeting.getMeeting_id())) {
                insertedId = meeting.getMeeting_id();
                break;
            }
        }
        createdMeetingIds.add(insertedId);
        return insertedId;
    }

    private int waitForRateButtonVisibility(ActivityScenario<MeetingDetailActivity> scenario, boolean expectVisible)
            throws InterruptedException {
        long timeoutMs = 5_000L;
        long start = System.currentTimeMillis();
        int[] currentVisibility = new int[]{View.GONE};

        while (System.currentTimeMillis() - start < timeoutMs) {
            scenario.onActivity(activity -> {
                View rateButton = activity.findViewById(R.id.btnRate);
                currentVisibility[0] = rateButton.getVisibility();
            });
            if (expectVisible && currentVisibility[0] == View.VISIBLE) {
                return currentVisibility[0];
            }
            if (!expectVisible && currentVisibility[0] != View.VISIBLE) {
                return currentVisibility[0];
            }
            Thread.sleep(100);
        }
        return currentVisibility[0];
    }
}
