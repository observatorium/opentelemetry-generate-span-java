package io.observatorium.opentelemetry.generate;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Pause {
    private static final int WAIT = 100; // ms

    public static void forSomeTime() {
        forDuration(TimeUnit.MILLISECONDS, new Random().nextInt(WAIT));
    }

    public static void forDuration(TimeUnit timeUnit, int amount) {
        long miliseconds = TimeUnit.MILLISECONDS.convert(amount, timeUnit);
        try {
            Thread.sleep(miliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
