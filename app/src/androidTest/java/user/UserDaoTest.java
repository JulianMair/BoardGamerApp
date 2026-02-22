package user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import de.iu.boardgame.feature_termine.data.AppDatabase;
import de.iu.boardgame.feature_user.data.User;
import de.iu.boardgame.feature_user.data.UserDao;
import testutil.LiveDataTestUtil;

@RunWith(AndroidJUnit4.class)
public class UserDaoTest {

    private AppDatabase database;
    private UserDao userDao;

    @Before
    public void setup() {
        Context context = ApplicationProvider.getApplicationContext();
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                .allowMainThreadQueries()
                .build();
        userDao = database.userDao();
    }

    @After
    public void tearDown() {
        database.close();
    }

    @Test
    public void insertUser_returnsIdAndCanBeLoaded() throws Exception {
        long id = userDao.insert(new User("Anna", "anna@example.com", "111", "A", true));

        User loaded = LiveDataTestUtil.getOrAwaitValue(
                userDao.getById(id),
                value -> value != null
        );

        assertTrue(id > 0);
        assertNotNull(loaded);
        assertEquals("Anna", loaded.name);
    }

    @Test
    public void getAll_ordersByName() {
        userDao.insert(new User("Clara", "c@example.com", "111", "A", true));
        userDao.insert(new User("Ben", "b@example.com", "222", "B", false));
        userDao.insert(new User("Anna", "a@example.com", "333", "C", true));

        List<User> users = userDao.getAll();

        assertEquals(3, users.size());
        assertEquals("Anna", users.get(0).name);
        assertEquals("Ben", users.get(1).name);
        assertEquals("Clara", users.get(2).name);
    }

    @Test
    public void updateUser_persistsChangedFields() {
        long id = userDao.insert(new User("David", "d@example.com", "444", "D", false));
        User user = userDao.getUserByIdSync(id);
        user.name = "Daniel";

        int updated = userDao.update(user);
        User loaded = userDao.getUserByIdSync(id);

        assertEquals(1, updated);
        assertEquals("Daniel", loaded.name);
    }

    @Test
    public void deleteById_removesUser() {
        long id = userDao.insert(new User("Eva", "e@example.com", "555", "E", true));
        userDao.deleteById(id);

        User deleted = userDao.getUserByIdSync(id);
        assertNull(deleted);
    }
}
