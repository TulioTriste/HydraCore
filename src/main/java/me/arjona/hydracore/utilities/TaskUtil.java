package me.arjona.hydracore.utilities;

import lombok.experimental.UtilityClass;
import me.arjona.hydracore.Core;
import org.bukkit.plugin.IllegalPluginAccessException;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

@UtilityClass
public class TaskUtil {
    
    private final JavaPlugin plugin = Core.get();

    public void run(Runnable runnable) {
        plugin.getServer().getScheduler().runTask(plugin, runnable);
    }

    public void runAsync(Runnable runnable) {
        try {
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, runnable);
        } catch (IllegalStateException e) {
            plugin.getServer().getScheduler().runTask(plugin, runnable);
        } catch (IllegalPluginAccessException e) {
            new Thread(runnable).start();
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public void runTimer(Runnable runnable, long delay, long timer) {
        plugin.getServer().getScheduler().runTaskTimer(plugin, runnable, delay, timer);
    }

    public int runTimer(BukkitRunnable runnable, long delay, long timer) {
        runnable.runTaskTimer(plugin, delay, timer);
        return runnable.getTaskId();
    }

    public void runLater(Runnable runnable, long delay) {
        plugin.getServer().getScheduler().runTaskLater(plugin, runnable, delay);
    }

    public void runLaterAsync(Runnable runnable, long delay) {
        try {
            plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, runnable, delay);
        } catch (IllegalStateException e) {
            plugin.getServer().getScheduler().runTaskLater(plugin, runnable, delay);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public void runTimerAsync(Runnable runnable, long delay, long timer) {
        try {
            plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, runnable, delay, timer);
        } catch (IllegalStateException e) {
            plugin.getServer().getScheduler().runTaskTimer(plugin, runnable, delay, timer);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public void runTimerAsync(BukkitRunnable runnable, long delay, long timer) {
        runnable.runTaskTimerAsynchronously(plugin, delay, timer);
    }

}
