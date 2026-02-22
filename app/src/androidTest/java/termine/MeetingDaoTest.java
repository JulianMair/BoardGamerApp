package termine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.concurrent.TimeUnit;

import de.iu.boardgame.feature_termine.data.AppDatabase;
import de.iu.boardgame.feature_termine.data.Meeting;
import de.iu.boardgame.feature_termine.data.MeetingDao;
import testutil.LiveDataTestUtil;

@RunWith(AndroidJUnit4.class)
public class MeetingDaoTest {

    private AppDatabase database;
    private MeetingDao meetingDao;

    @Before
    public void setup() {
        Context context = ApplicationProvider.getApplicationContext();
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                .allowMainThreadQueries()
                .build();
        meetingDao = database.meetingDao();
    }

    @After
    public void tearDown() {
        database.close();
    }

    @Test
    public void createMeeting_increasesCount() {
        meetingDao.create(new Meeting("A", System.currentTimeMillis(), "Loc", 1L, "open"));
        assertEquals(1, meetingDao.countMeetings());
    }

    @Test
    public void getAll_returnsSortedByTimestamp() {
        long now = System.currentTimeMillis();
        meetingDao.create(new Meeting("later", now + 3_000, "Loc", 1L, "open"));
        meetingDao.create(new Meeting("first", now + 1_000, "Loc", 1L, "open"));
        meetingDao.create(new Meeting("middle", now + 2_000, "Loc", 1L, "open"));

        List<Meeting> meetings = meetingDao.getAll();

        assertEquals(3, meetings.size());
        assertEquals("first", meetings.get(0).getTitle());
        assertEquals("middle", meetings.get(1).getTitle());
        assertEquals("later", meetings.get(2).getTitle());
    }

    @Test
    public void getById_returnsInsertedMeeting() throws Exception {
        meetingDao.create(new Meeting("Detail", System.currentTimeMillis(), "Berlin", 1L, "open"));
        int meetingId = latestMeetingId();

        Meeting meeting = LiveDataTestUtil.getOrAwaitValue(
                meetingDao.getById(meetingId),
                value -> value != null
        );

        assertNotNull(meeting);
        assertEquals("Detail", meeting.getTitle());
    }

    @Test
    public void getRelevantMeetings_filtersClosedAndOld() throws Exception {
        long now = System.currentTimeMillis();
        long historyLimit = now - TimeUnit.DAYS.toMillis(3);
        meetingDao.create(new Meeting("open_old", now - TimeUnit.DAYS.toMillis(10), "Loc", 1L, "open"));
        meetingDao.create(new Meeting("closed_old", now - TimeUnit.DAYS.toMillis(10), "Loc", 1L, "closed"));
        meetingDao.create(new Meeting("closed_recent", now - TimeUnit.DAYS.toMillis(1), "Loc", 1L, "closed"));

        List<Meeting> relevant = LiveDataTestUtil.getOrAwaitValue(
                meetingDao.getRelevantMeetings(historyLimit),
                value -> value != null && value.size() == 2
        );

        assertEquals(2, relevant.size());
        assertEquals("open_old", relevant.get(0).getTitle());
        assertEquals("closed_recent", relevant.get(1).getTitle());
    }

    private int latestMeetingId() {
        List<Meeting> meetings = meetingDao.getAll();
        return meetings.get(meetings.size() - 1).getMeeting_id();
    }
}
