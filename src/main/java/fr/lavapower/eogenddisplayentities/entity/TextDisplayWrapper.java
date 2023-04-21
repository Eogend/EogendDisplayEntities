package fr.lavapower.eogenddisplayentities.entity;

import fr.lavapower.eogenddisplayentities.manager.EntityManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Display;
import org.bukkit.entity.TextDisplay;
import org.bukkit.util.Transformation;

public class TextDisplayWrapper extends DisplayWrapper {
    private final TextDisplay entity;

    public TextDisplayWrapper(TextDisplay entity) {
        this.entity = entity;
    }

    public void modify(EntityManager manager, CommandSender sender, String name, String param, String[] args) {
        switch (param) {
            case "position", "world" -> super.modify(manager, sender, name, param, args);
            case "text" -> {
                if (args.length < 1)
                    sender.sendMessage("Texte invalide");
                else {
                    entity.setText(String.join(" ", args));
                    sender.sendMessage("Texte défini !");
                }
            }
            default -> sender.sendMessage("Paramètres possibles : position, rotation, scale, glow, world, text");
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
        return "text";
    }

    @Override
    public Location getLocation() {
        return entity.getLocation();
    }
}
