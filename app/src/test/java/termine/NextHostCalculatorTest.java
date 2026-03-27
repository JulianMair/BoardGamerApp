package termine;

import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

import de.iu.boardgame.feature_termine.data.Meeting;
import de.iu.boardgame.feature_termine.logic.NextHostCalculator;
import de.iu.boardgame.feature_user.data.User;

import static org.junit.Assert.*;

public class NextHostCalculatorTest {

    @Test
    public void testNoUsers_returnsNull() {
        List<User> users = new ArrayList<>();
        List<Meeting> meetings = new ArrayList<>();

        User result = NextHostCalculator.calculateNextHostId(meetings, users);

        assertNull(result);
    }

    @Test
    public void testNoMeetings_returnsRandomValidHost() {
        List<User> users = new ArrayList<>();

        User u1 = new User(
                "Max Mustermann",
                "max@test.de",
                "123456789",
                "Musterstraße 1",
                true
        );
        u1.id = 1L;

        User u2 = new User(
                "Anna Schmidt",
                "anna@test.de",
                "987654321",
                "Beispielweg 5",
                true
        );
        u2.id = 2L;

        User u3 = new User(
                "Tom Beispiel",
                "tom@test.de",
                "111222333",
                "Teststraße 9",
                false   // darf kein Host sein
        );
        u3.id = 3L;


        users.add(u1);
        users.add(u2);
        users.add(u3);

        User result = NextHostCalculator.calculateNextHostId(null, users);

        assertNotNull(result);
        assertTrue(result.id == 1 || result.id == 2);
    }

    @Test
    public void testLeastHostedUser_isReturned() {
        List<User> users = new ArrayList<>();

        User u1 = new User(
                "Max Mustermann",
                "max@test.de",
                "123456789",
                "Musterstraße 1",
                true
        );
        u1.id = 1L;

        User u2 = new User(
                "Anna Schmidt",
                "anna@test.de",
                "987654321",
                "Beispielweg 5",
                true
        );
        u2.id = 2L;

        User u3 = new User(
                "Tom Beispiel",
                "tom@test.de",
                "111222333",
                "Teststraße 9",
                false   // darf kein Host sein
        );
        u3.id = 3L;


        users.add(u1);
        users.add(u2);
        users.add(u3);


        List<Meeting> meetings = new ArrayList<>();

        Meeting m1 = new Meeting(
                "Spieleabend Januar",
                System.currentTimeMillis(),
                "München",
                1L,
                "closed"
        );

        Meeting m2 = new Meeting(
                "Spieleabend Februar",
                System.currentTimeMillis(),
                "Berlin",
                2L,
                "closed"
        );

        Meeting m3 = new Meeting(
                "Spieleabend März",
                System.currentTimeMillis(),
                "Hamburg",
                1L,
                "planned"
        );


        meetings.add(m1);
        meetings.add(m2);
        meetings.add(m3);

        User result = NextHostCalculator.calculateNextHostId(meetings, users);

        // User 2 hat 0 Abende ausgerichtet -> sollte gewählt werden
        assertEquals(2, result.id);
    }
}
