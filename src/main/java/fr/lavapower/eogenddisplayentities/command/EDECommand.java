package fr.lavapower.eogenddisplayentities.command;

import fr.lavapower.eogenddisplayentities.EogendDisplayEntities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class EDECommand implements CommandExecutor, TabExecutor {
    private final EogendDisplayEntities plugin;

    public EDECommand(EogendDisplayEntities plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage("Seul un joueur peut utiliser cette commande");
            return true;
        }

        if(args.length >= 1) {
            switch (args[0]) {
                // CREATE PART
                case "create" -> {
                    if (args.length < 3)
                        sender.sendMessage("Usage : /" + label + " create <block|item|text> <name>");
                    else if (!args[1].equals("block") && !args[1].equals("item") && !args[1].equals("text"))
                        sender.sendMessage("Type d'entité inconnu : " + args[1]);
                    else {
                        boolean created = switch (args[1]) {
                            case "block" -> plugin.getManager().createBlockEntity(args[2], player.getLocation());
                            case "item" -> plugin.getManager().createItemEntity(args[2], player.getLocation());
                            case "text" -> plugin.getManager().createTextEntity(args[2], player.getLocation());
                            default -> false;
                        };
                        if (created)
                            sender.sendMessage("Entité créée !");
                        else
                            sender.sendMessage("Entité non créée : Nom déjà utilisée ou location déjà utilisée");
                    }
                }

                // LIST PART
                case "list" -> {
                    sender.sendMessage("Entités :");
                    for(var entry : plugin.getManager().getNamesWithType().entrySet())
                        sender.sendMessage("- "+entry.getKey()+" -> " + entry.getValue());
                }

                // REMOVE PART
                case "remove" -> {
                    if (args.length < 2)
                        sender.sendMessage("Usage : /" + label + " remove <name>");
                    else {
                        if (plugin.getManager().removeEntity(args[1]))
                            sender.sendMessage("Entité supprimée !");
                        else
                            sender.sendMessage("Entité non supprimée : Nom inconnu");
                    }
                }

                // MODIFY PART
                case "modify" -> {
                    if (args.length < 3)
                        sender.sendMessage("Usage : /" + label + " modify <type> <name> <param> <value>");
                    else {
                        if(args.length < 5) {
                            var temp = new String[5];
                            System.arraycopy(args, 0, temp, 0, args.length);
                            args = temp;
                        }

                        if (!plugin.getManager().modifyEntity(sender, args[1], args[2], args[3], Arrays.copyOfRange(args, 4, args.length)))
                            sender.sendMessage("Entité non modifié : Nom inconnu");
                    }
                }

                // GET PART
                case "get" -> {
                    if(args.length < 3)
                        sender.sendMessage("Usage : /" + label + " get <type> <name> <param>");
                    else {
                        if(args.length == 3) {
                            var temp = new String[4];
                            System.arraycopy(args, 0, temp, 0, args.length);
                            args = temp;
                        }

                        if (!plugin.getManager().getEntity(sender, args[1], args[2], args[3]))
                            sender.sendMessage("Entité non trouvée : Nom inconnu");
                    }
                }

                // OTHER PART
                default ->
                        sender.sendMessage("Usage : /" + label + " <create|remove|modify|get|list> [<type>] [<name>] [<param>] [<value>]");
            }
        }
        else
            sender.sendMessage("Usage : /"+label+" <create|remove|modify|get|list> [<type>] [<name>] [<param>] [<value>]");
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 1)
            return Stream.of("create", "remove", "list", "modify", "get").filter(x -> args[0].isEmpty() || x.toLowerCase().startsWith(args[0].toLowerCase())).toList();
        else if(args.length == 2 && (args[0].equals("create") || args[0].equals("modify") || args[0].equals("get")))
            return Stream.of("block", "item", "text").filter(x -> args[1].isEmpty() || x.toLowerCase().startsWith(args[1].toLowerCase())).toList();
        else if(args.length == 2 && args[0].equals("remove"))
            return plugin.getManager().getNames().stream().filter(x -> args[1].isEmpty() || x.toLowerCase().startsWith(args[1].toLowerCase())).toList();
        else if(args.length == 3 && (args[0].equals("modify") || args[0].equals("get"))) {
            if(args[1].equals("block"))
                return plugin.getManager().getBlockNames().stream().filter(x -> args[2].isEmpty() || x.toLowerCase().startsWith(args[2].toLowerCase())).toList();
            if(args[1].equals("item"))
                return plugin.getManager().getItemNames().stream().filter(x -> args[2].isEmpty() || x.toLowerCase().startsWith(args[2].toLowerCase())).toList();
            if(args[1].equals("text"))
                return plugin.getManager().getTextNames().stream().filter(x -> args[2].isEmpty() || x.toLowerCase().startsWith(args[2].toLowerCase())).toList();
            return plugin.getManager().getNames().stream().filter(x -> args[2].isEmpty() || x.toLowerCase().startsWith(args[2].toLowerCase())).toList();
        }
        else
            return null;
    }
}
