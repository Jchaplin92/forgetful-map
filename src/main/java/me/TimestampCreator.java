package me;

import java.time.Instant;

public class TimestampCreator {
    public static long getCurrentTimestamp() {
        return Instant.now().toEpochMilli();
    }

}
