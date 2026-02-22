package games;

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

import de.iu.boardgame.feature_spiele.data.Game;
import de.iu.boardgame.feature_spiele.data.GameDao;
import de.iu.boardgame.feature_termine.data.AppDatabase;
import testutil.LiveDataTestUtil;

@RunWith(AndroidJUnit4.class)
public class GameDaoTest {

    private AppDatabase database;
    private GameDao gameDao;

    @Before
    public void setup() {
        Context context = ApplicationProvider.getApplicationContext();
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                .allowMainThreadQueries()
                .build();
        gameDao = database.gameDao();
    }

    @After
    public void tearDown() {
        database.close();
    }

    @Test
    public void insertGame_returnsIdAndCanBeLoaded() throws Exception {
        long id = gameDao.insert(new Game("Catan", 90, "Strategie"));

        Game loaded = LiveDataTestUtil.getOrAwaitValue(
                gameDao.getById(id),
                value -> value != null
        );

        assertTrue(id > 0);
        assertNotNull(loaded);
        assertEquals("Catan", loaded.gameTitle);
    }

    @Test
    public void getAll_ordersByTitle() {
        gameDao.insert(new Game("Terraforming Mars", 120, "Strategie"));
        gameDao.insert(new Game("Azul", 45, "Familie"));
        gameDao.insert(new Game("Codenames", 30, "Party"));

        List<Game> games = gameDao.getAll();

        assertEquals(3, games.size());
        assertEquals("Azul", games.get(0).gameTitle);
        assertEquals("Codenames", games.get(1).gameTitle);
        assertEquals("Terraforming Mars", games.get(2).gameTitle);
    }

    @Test
    public void updateGame_persistsChanges() {
        long id = gameDao.insert(new Game("Old", 10, "X"));
        Game game = gameDao.getAll().get(0);
        game.gameTitle = "New";
        game.gameDuration = 99;

        int updated = gameDao.update(game);
        Game loaded = gameDao.getAll().get(0);

        assertEquals(1, updated);
        assertEquals(id, loaded.id);
        assertEquals("New", loaded.gameTitle);
        assertEquals(99, loaded.gameDuration);
    }

    @Test
    public void deleteById_removesGame() throws Exception {
        long id = gameDao.insert(new Game("DeleteMe", 20, "Test"));
        gameDao.deleteById(id);

        Game deleted = LiveDataTestUtil.getOrAwaitValue(
                gameDao.getById(id),
                value -> true
        );
        assertNull(deleted);
    }
}
