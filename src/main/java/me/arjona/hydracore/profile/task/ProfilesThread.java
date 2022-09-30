package me.arjona.hydracore.profile.task;

import me.arjona.hydracore.Core;
import me.arjona.hydracore.utilities.TaskUtil;

import java.time.Duration;

/*
    This project is Currently
    worked by Arjona, the best dev
    in his house.
    Arjona#0643
    https://discord.pandacommunity.org/development
*/
public class ProfilesThread extends Thread {

    @Override
    public void run() {
        while (true) {
            TaskUtil.runAsync(() -> Core.get().getProfileManager().saveAll());
            try {
                Thread.sleep(Duration.ofMinutes(5).toMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
