package fr.lavapower.eogenddisplayentities.entity;

import fr.lavapower.eogenddisplayentities.manager.EntityManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Display;
import org.bukkit.util.Transformation;

public class BlockDisplayWrapper extends DisplayWrapper {
    private final BlockDisplay entity;

    public BlockDisplayWrapper(BlockDisplay entity) {
        this.entity = entity;
    }

    public void get(CommandSender sender, String param) {
        if(param == null) {
            sender.sendMessage("Paramètres possibles : position, rotation, scale, glow, world, block");
            return;
        }

        switch (param) {
            case "position", "world", "rotation", "scale", "glow" -> super.get(sender, param);
            case "block" -> sender.sendMessage("Bloc : " + entity.getBlock().getMaterial().name());
            default -> sender.sendMessage("Paramètres possibles : position, rotation, scale, glow, world, block");
        }
    }

    public void modify(EntityManager manager, CommandSender sender, String name, String param, String[] args) {
        if(param == null) {
            sender.sendMessage("Paramètres possibles : position, rotation, scale, glow, world, block");
            return;
        }

        switch (param) {
            case "position", "world", "rotation", "scale", "glow" -> super.modify(manager, sender, name, param, args);
            case "block" -> {
                if (args.length < 1)
                    sender.sendMessage("Bloc invalide");
                else {
                    Material material = Material.getMaterial(args[0]);
                    if (material == null)
                        sender.sendMessage("Bloc invalide");
                    else if (!material.isBlock())
                        sender.sendMessage("Bloc invalide");
                    else {
                        entity.setBlock(material.createBlockData());
                        sender.sendMessage("Bloc défini !");
                    }
                }
            }
            default -> sender.sendMessage("Paramètres possibles : position, rotation, scale, glow, world, block");
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
        return "block";
    }

    @Override
    public Location getLocation() {
        return entity.getLocation();
    }
}
