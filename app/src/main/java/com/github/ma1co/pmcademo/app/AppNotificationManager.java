package com.github.ma1co.pmcademo.app;

import java.util.ArrayList;

public class AppNotificationManager {
    public interface NotificationListener {
        void onNotify(String message);
    }

    private static final AppNotificationManager instance = new AppNotificationManager();

    public static AppNotificationManager getInstance() {
        return instance;
    }

    private ArrayList<NotificationListener> listeners = new ArrayList<NotificationListener>();

    private AppNotificationManager() {}

    public void notify(String message) {
        for (NotificationListener listener : listeners)
            listener.onNotify(message);
    }

    public void addListener(NotificationListener listener) {
        listeners.add(listener);
    }

    public void removeListener(NotificationListener listener) {
        listeners.remove(listener);
    }
}
