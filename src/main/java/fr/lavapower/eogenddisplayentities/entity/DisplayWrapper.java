package fr.lavapower.eogenddisplayentities.entity;

import fr.lavapower.eogenddisplayentities.manager.EntityManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Display;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public abstract class DisplayWrapper {
    public void get(CommandSender sender, String param) {
        switch (param) {
            case "rotation" -> {
                AxisAngle4f leftRotation = new AxisAngle4f(getTransformation().getLeftRotation());
                sender.sendMessage("Rotation : " + Math.toDegrees(leftRotation.angle) + "(" + leftRotation.x + ", " + leftRotation.y + ", " + leftRotation.z + ")");
            }
            case "glow" -> sender.sendMessage("Glow : " + getEntity().isGlowing());
            case "scale" -> {
                Vector3f scale = getTransformation().getScale();
                sender.sendMessage("Scale : (" + scale.x + ", " + scale.y + ", " + scale.z + ")");
            }
            case "position" -> {
                Location location = getLocation();
                sender.sendMessage("Position : (" + location.getX() + ", " + location.getY() + ", " + location.getZ() + ")");
            }
            case "world" -> sender.sendMessage("Monde : " + getWorld().getName());
        }
    }

    public void modify(EntityManager manager, CommandSender sender, String name, String param, String[] args) {
        switch (param) {
            case "rotation" -> {
                if(args.length < 4) {
                    sender.sendMessage("Rotation invalide");
                }
                else {
                    try {
                        float angle = (float)Math.toRadians(Double.parseDouble(args[0]));
                        float x = Float.parseFloat(args[1]);
                        float y = Float.parseFloat(args[2]);
                        float z = Float.parseFloat(args[3]);
                        Transformation transformation = getTransformation();
                        Transformation newTransformation = new Transformation(
                                transformation.getTranslation(),
                                new Quaternionf(new AxisAngle4f(angle, x, y, z)),
                                transformation.getScale(),
                                transformation.getRightRotation()
                        );
                        getEntity().setTransformation(newTransformation);
                        sender.sendMessage("Entité tournée !");
                    }
                    catch (NumberFormatException ignored)
                    {
                        sender.sendMessage("Rotation invalide");
                    }
                }
            }
            case "glow" -> {
                if(args.length < 1)
                    sender.sendMessage("Glow invalide");
                else {
                    boolean glow = Boolean.parseBoolean(args[0]);
                    getEntity().setGlowing(glow);
                    sender.sendMessage(glow ? "Glow activé." : "Glow désactivé.");
                }
            }
            case "scale" -> {
                if(args.length < 3)
                    sender.sendMessage("Scale invalide");
                else {
                    try {
                        float x = Float.parseFloat(args[0]);
                        float y = Float.parseFloat(args[1]);
                        float z = Float.parseFloat(args[2]);
                        Transformation transformation = getTransformation();
                        Transformation newTransformation = new Transformation(
                                transformation.getTranslation(),
                                transformation.getLeftRotation(),
                                new Vector3f(x, y, z),
                                transformation.getRightRotation()
                        );
                        getEntity().setTransformation(newTransformation);
                        sender.sendMessage("Entité scale !");
                    } catch (NumberFormatException ignored) {
                        sender.sendMessage("Scale invalide");
                    }
                }
            }
            case "position" -> {
                if (args.length < 3)
                    sender.sendMessage("Position invalide.");
                else {
                    try {
                        double x = Double.parseDouble(args[0]);
                        double y = Double.parseDouble(args[1]);
                        double z = Double.parseDouble(args[2]);
                        getEntity().teleport(new Location(getWorld(), x, y, z));
                        manager.moveEntity(name, this);
                        sender.sendMessage("Entité bougée !");
                    } catch (NumberFormatException ignored) {
                        sender.sendMessage("Position invalide");
                    }
                }
            }
            case "world" -> {
                if (args.length < 1)
                    sender.sendMessage("Monde invalide");
                else {
                    var world = Bukkit.getWorld(args[0]);
                    if (world == null)
                        sender.sendMessage("Monde invalide");
                    else {
                        getEntity().teleport(new Location(world, getLocation().getX(), getLocation().getY(), getLocation().getZ()));
                        manager.moveEntity(name, this);
                        sender.sendMessage("Entité bougée !");
                    }
                }
            }
        }
    }

    public  abstract Transformation getTransformation();
    public abstract Display getEntity();
    public abstract World getWorld();
    public abstract String getType();
    public abstract Location getLocation();
}
