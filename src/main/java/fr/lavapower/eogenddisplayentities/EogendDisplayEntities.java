package fr.lavapower.eogenddisplayentities;

import fr.lavapower.eogenddisplayentities.command.EDECommand;
import fr.lavapower.eogenddisplayentities.manager.EntityManager;
import fr.lavapower.eogenddisplayentities.storage.EntityStorage;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

public class EogendDisplayEntities extends JavaPlugin {
    private EntityStorage storage;
    private EntityManager manager;

    @Override
    public void onEnable() {
        registerCommand("eogenddisplayentities", new EDECommand(this));

        storage = new EntityStorage(this);
        manager = new EntityManager(this);

        Bukkit.getScheduler().runTaskLater(this, manager::init, 20L);
    }

    public EntityStorage getStorage() {
        return storage;
    }

    public EntityManager getManager() {
        return manager;
    }

    private <T extends CommandExecutor & TabCompleter> void registerCommand(String name, T command)
    {
        PluginCommand pluginCommand = getCommand(name);
        pluginCommand.setExecutor(command);
        pluginCommand.setTabCompleter(command);
    }
}
