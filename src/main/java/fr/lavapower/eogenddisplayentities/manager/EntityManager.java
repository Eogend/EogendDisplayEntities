package fr.lavapower.eogenddisplayentities.manager;

import fr.lavapower.eogenddisplayentities.EogendDisplayEntities;
import fr.lavapower.eogenddisplayentities.entity.BlockDisplayWrapper;
import fr.lavapower.eogenddisplayentities.entity.DisplayWrapper;
import fr.lavapower.eogenddisplayentities.entity.ItemDisplayWrapper;
import fr.lavapower.eogenddisplayentities.entity.TextDisplayWrapper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class EntityManager {
    private final EogendDisplayEntities plugin;
    private final Map<String, BlockDisplayWrapper> blockDisplayWrappers = new HashMap<>();
    private final Map<String, ItemDisplayWrapper> itemDisplayWrappers = new HashMap<>();
    private final Map<String, TextDisplayWrapper> textDisplayWrappers = new HashMap<>();

    public EntityManager(EogendDisplayEntities plugin) {
        this.plugin = plugin;
    }

    public void moveEntity(String name, DisplayWrapper wrapper) {
        plugin.getStorage().removeEntity(wrapper);
        plugin.getStorage().saveNameForEntity(name, wrapper);
    }

    public boolean modifyEntity(CommandSender sender, String type, String name, String param, String[] args) {
        switch (type) {
            case "block" -> {
                if (!blockDisplayWrappers.containsKey(name))
                    return false;
                blockDisplayWrappers.get(name).modify(this, sender, name, param, args);
                return true;
            }
            case "item" -> {
                if (!itemDisplayWrappers.containsKey(name))
                    return false;
                itemDisplayWrappers.get(name).modify(this, sender, name, param, args);
                return true;
            }
            case "text" -> {
                if (!textDisplayWrappers.containsKey(name))
                    return false;
                textDisplayWrappers.get(name).modify(this, sender, name, param, args);
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    public List<String> getNames() {
        List<String> names = new ArrayList<>();
        names.addAll(blockDisplayWrappers.keySet());
        names.addAll(itemDisplayWrappers.keySet());
        names.addAll(textDisplayWrappers.keySet());
        return names;
    }

    public Map<String, String> getNamesWithType() {
        Map<String, String> namesMap = new HashMap<>();
        namesMap.putAll(blockDisplayWrappers.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> "block")));
        namesMap.putAll(itemDisplayWrappers.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> "item")));
        namesMap.putAll(textDisplayWrappers.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> "text")));
        return namesMap;
    }

    public boolean removeEntity(String name) {
        if(blockDisplayWrappers.containsKey(name))
        {
            var wrapper = blockDisplayWrappers.get(name);
            wrapper.getEntity().remove();
            blockDisplayWrappers.remove(name);
            plugin.getStorage().removeEntity(wrapper);
            return true;
        }
        if(itemDisplayWrappers.containsKey(name))
        {
            var wrapper = itemDisplayWrappers.get(name);
            wrapper.getEntity().remove();
            itemDisplayWrappers.remove(name);
            plugin.getStorage().removeEntity(wrapper);
            return true;
        }
        if(textDisplayWrappers.containsKey(name))
        {
            var wrapper = textDisplayWrappers.get(name);
            wrapper.getEntity().remove();
            textDisplayWrappers.remove(name);
            plugin.getStorage().removeEntity(wrapper);
            return true;
        }
        return false;
    }

    public boolean createBlockEntity(String name, Location location) {
        if(blockDisplayWrappers.containsKey(name) || itemDisplayWrappers.containsKey(name) || textDisplayWrappers.containsKey(name))
            return false;

        var entity = (BlockDisplay) location.getWorld().spawnEntity(location, EntityType.BLOCK_DISPLAY);
        var wrapper = new BlockDisplayWrapper(entity);

        if(plugin.getStorage().getNameForEntity(wrapper) != null)
        {
            entity.remove();
            return false;
        }

        plugin.getStorage().saveNameForEntity(name, wrapper);
        blockDisplayWrappers.put(name, wrapper);
        return true;
    }

    public boolean createItemEntity(String name, Location location) {
        if(blockDisplayWrappers.containsKey(name) || itemDisplayWrappers.containsKey(name) || textDisplayWrappers.containsKey(name))
            return false;

        var entity = (ItemDisplay) location.getWorld().spawnEntity(location, EntityType.ITEM_DISPLAY);
        var wrapper = new ItemDisplayWrapper(entity);

        if(plugin.getStorage().getNameForEntity(wrapper) != null)
        {
            entity.remove();
            return false;
        }

        plugin.getStorage().saveNameForEntity(name, wrapper);
        itemDisplayWrappers.put(name, wrapper);
        return true;
    }

    public boolean createTextEntity(String name, Location location) {
        if(blockDisplayWrappers.containsKey(name) || itemDisplayWrappers.containsKey(name) || textDisplayWrappers.containsKey(name))
            return false;

        var entity = (TextDisplay) location.getWorld().spawnEntity(location, EntityType.TEXT_DISPLAY);
        var wrapper = new TextDisplayWrapper(entity);

        if(plugin.getStorage().getNameForEntity(wrapper) != null)
        {
            entity.remove();
            return false;
        }

        plugin.getStorage().saveNameForEntity(name, wrapper);
        textDisplayWrappers.put(name, wrapper);
        return true;
    }

    public void init() {
        for (World world: Bukkit.getWorlds()) {
            for(Entity entity: world.getEntities()) {
                if(entity instanceof BlockDisplay e)
                {
                    var wrapper = new BlockDisplayWrapper(e);
                    var name = plugin.getStorage().getNameForEntity(wrapper);
                    if(name == null)
                        entity.remove();
                    else
                        blockDisplayWrappers.put(name, wrapper);
                }
                else if(entity instanceof ItemDisplay e) {
                    var wrapper = new ItemDisplayWrapper(e);
                    var name = plugin.getStorage().getNameForEntity(wrapper);
                    if(name == null)
                        entity.remove();
                    else
                        itemDisplayWrappers.put(name, wrapper);
                }
                else if(entity instanceof TextDisplay e) {
                    var wrapper = new TextDisplayWrapper(e);
                    var name = plugin.getStorage().getNameForEntity(wrapper);
                    if(name == null)
                        entity.remove();
                    else
                        textDisplayWrappers.put(name, wrapper);
                }
            }
        }
    }


}
