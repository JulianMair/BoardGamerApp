package testutil;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.test.platform.app.InstrumentationRegistry;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public final class LiveDataTestUtil {

    private LiveDataTestUtil() {
    }

    public interface ValueCondition<T> {
        boolean matches(T value);
    }

    public static <T> T getOrAwaitValue(LiveData<T> liveData, ValueCondition<T> condition) throws Exception {
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
