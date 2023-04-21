package fr.lavapower.eogenddisplayentities.entity;

import fr.lavapower.eogenddisplayentities.manager.EntityManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Display;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;

public class ItemDisplayWrapper extends DisplayWrapper {
    private final ItemDisplay entity;

    public ItemDisplayWrapper(ItemDisplay entity) {
        this.entity = entity;
    }

    public void get(CommandSender sender, String param) {
        if(param == null) {
            sender.sendMessage("Paramètres possibles : position, rotation, scale, glow, world, item");
            return;
        }

        switch (param) {
            case "position", "world", "rotation", "scale", "glow" -> super.get(sender, param);
            case "item" -> sender.sendMessage("Item : " + (entity.getItemStack() == null ? "None" : entity.getItemStack().getType().name()));
            default -> sender.sendMessage("Paramètres possibles : position, rotation, scale, glow, world, item");
        }
    }

    public void modify(EntityManager manager, CommandSender sender, String name, String param, String[] args) {
        if(param == null) {
            sender.sendMessage("Paramètres possibles : position, rotation, scale, glow, world, block");
            return;
        }

        switch (param) {
            case "position", "world", "rotation", "scale", "glow" -> super.modify(manager, sender, name, param, args);
            case "item" -> {
                if (args.length < 1)
                    sender.sendMessage("Item invalide");
                else {
                    Material material = Material.getMaterial(args[0]);
                    if (material == null)
                        sender.sendMessage("Item invalide");
                    else if (!material.isItem())
                        sender.sendMessage("Item invalide");
                    else {
                        entity.setItemStack(new ItemStack(material));
                        sender.sendMessage("Item défini !");
                    }
                }
            }
            default -> sender.sendMessage("Paramètres possibles : position, rotation, scale, glow, world, item");
        }
    }

    @Override
    public Transformation getTransformation() {
        return entity.getTransformation();
    }

    @Override
    public Display getEntity() {
        return entity;
    }

    @Override
    public World getWorld() {
        return entity.getWorld();
    }

    @Override
    public String getType() {
        return "item";
    }

    @Override
    public Location getLocation() {
        return entity.getLocation();
    }
}
