package message;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import de.iu.boardgame.feature_send_message.data.Message;
import de.iu.boardgame.feature_send_message.data.MessageDao;
import de.iu.boardgame.feature_send_message.data.MessageWithUser;
import de.iu.boardgame.feature_termine.data.AppDatabase;
import de.iu.boardgame.feature_termine.data.Meeting;
import de.iu.boardgame.feature_termine.data.MeetingDao;
import de.iu.boardgame.feature_user.data.User;
import de.iu.boardgame.feature_user.data.UserDao;

@RunWith(AndroidJUnit4.class)
public class MessageDaoTest {

    private AppDatabase database;
    private MessageDao messageDao;
    private MeetingDao meetingDao;
    private UserDao userDao;

    @Before
    public void setup() {
        Context context = ApplicationProvider.getApplicationContext();

        database = Room.inMemoryDatabaseBuilder(
                context,
                AppDatabase.class
        ).allowMainThreadQueries().build();

        messageDao = database.messageDao();
        meetingDao = database.meetingDao();
        userDao = database.userDao();
    }

    @After
    public void tearDown() {
        database.close();
    }

    @Test
    public void insertMessage_savesCorrectData() throws Exception {
        long userId = insertTestUser("Anna");
        int meetingId = insertTestMeeting();
        LocalDateTime now = LocalDateTime.now();
        Message message = new Message(userId, "Hallo Test", meetingId, now);

        messageDao.insert(message);

        List<Message> messages = getOrAwaitValue(
                messageDao.getMessagesForMeeting(meetingId),
                value -> value != null && value.size() == 1
        );

        assertEquals(1, messages.size());
        assertEquals(userId, messages.get(0).getSenderUserId());
        assertEquals("Hallo Test", messages.get(0).getText());
        assertEquals(meetingId, messages.get(0).getMeetingId());
    }

    @Test
    public void insert_returnsGeneratedId() {
        long userId = insertTestUser("Ben");
        int meetingId = insertTestMeeting();

        long insertedId = messageDao.insert(new Message(userId, "id-check", meetingId, LocalDateTime.now()));

        assertTrue(insertedId > 0);
    }

    @Test
    public void getMessagesForMeeting_filtersByMeetingId() throws Exception {
        long userId = insertTestUser("Clara");
        int targetMeetingId = insertTestMeeting();
        int otherMeetingId = insertTestMeeting();

        messageDao.insert(new Message(userId, "target", targetMeetingId, LocalDateTime.now()));
        messageDao.insert(new Message(userId, "other", otherMeetingId, LocalDateTime.now().plusMinutes(1)));

        List<Message> targetMessages = getOrAwaitValue(
                messageDao.getMessagesForMeeting(targetMeetingId),
                value -> value != null && value.size() == 1
        );

        assertEquals(1, targetMessages.size());
        assertEquals("target", targetMessages.get(0).getText());
        assertEquals(targetMeetingId, targetMessages.get(0).getMeetingId());
    }

    @Test
    public void getMessagesForMeeting_ordersByTimestampAscending() throws Exception {
        long userId = insertTestUser("David");
        int meetingId = insertTestMeeting();
        LocalDateTime base = LocalDateTime.now();

        messageDao.insert(new Message(userId, "later", meetingId, base.plusMinutes(2)));
        messageDao.insert(new Message(userId, "first", meetingId, base));
        messageDao.insert(new Message(userId, "middle", meetingId, base.plusMinutes(1)));

        List<Message> messages = getOrAwaitValue(
                messageDao.getMessagesForMeeting(meetingId),
                value -> value != null && value.size() == 3
        );

        assertEquals(3, messages.size());
        assertEquals("first", messages.get(0).getText());
        assertEquals("middle", messages.get(1).getText());
        assertEquals("later", messages.get(2).getText());
    }

    @Test
    public void getMessagesForMeetingWithUser_returnsUsernameFromJoin() throws Exception {
        long userId = insertTestUser("Eva");
        int meetingId = insertTestMeeting();

        messageDao.insert(new Message(userId, "join-check", meetingId, LocalDateTime.now()));

        List<MessageWithUser> messages = getOrAwaitValue(
                messageDao.getMessagesForMeetingWithUser(meetingId),
                value -> value != null && value.size() == 1
        );

        assertEquals(1, messages.size());
        assertEquals("Eva", messages.get(0).getUsername());
        assertEquals("join-check", messages.get(0).getText());
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
        Meeting meeting = new Meeting(
                "Test Meeting",
                System.currentTimeMillis() + TimeUnit.HOURS.toMillis(1),
                "Test Location",
                1L,
                "open"
        );
        meetingDao.create(meeting);

        List<Meeting> meetings = meetingDao.getAll();
        assertNotNull(meetings);
        return meetings.get(meetings.size() - 1).getMeeting_id();
    }

    private interface ValueCondition<T> {
        boolean matches(T value);
    }

    private static <T> T getOrAwaitValue(LiveData<T> liveData, ValueCondition<T> condition) throws Exception {
        final Object[] data = new Object[1];
        final CountDownLatch latch = new CountDownLatch(1);
        final Observer<T> observer = new Observer<T>() {
            @Override
            public void onChanged(T value) {
                if (condition.matches(value)) {
                    data[0] = value;
                    latch.countDown();
                    liveData.removeObserver(this);
                }
            }
        };

        InstrumentationRegistry.getInstrumentation().runOnMainSync(
                () -> liveData.observeForever(observer)
        );

        if (!latch.await(4, TimeUnit.SECONDS)) {
            InstrumentationRegistry.getInstrumentation().runOnMainSync(
                    () -> liveData.removeObserver(observer)
            );
            throw new TimeoutException("LiveData value was never set.");
        }

        //noinspection unchecked
        return (T) data[0];
    }
}
