package de.sbg.unity.smallteleporter.events;

import de.sbg.unity.smallteleporter.Objects.Teleporter;
import de.sbg.unity.smallteleporter.SmallTeleporter;
import de.sbg.unity.smallteleporter.TextFormat;
import java.sql.SQLException;
import net.risingworld.api.events.EventMethod;
import net.risingworld.api.events.Listener;
import net.risingworld.api.events.player.PlayerCommandEvent;
import net.risingworld.api.events.player.PlayerConnectEvent;
import net.risingworld.api.objects.Player;

public class PlayerEvent implements Listener {

    private final SmallTeleporter plugin;
    private final boolean Update;
    private final TextFormat TextFormat;

    public PlayerEvent(SmallTeleporter plugin, boolean update) {
        this.plugin = plugin;
        this.Update = update;
        this.TextFormat = new TextFormat();
    }

    @EventMethod
    public void onPlayerCommandEvent(PlayerCommandEvent event) {
        String[] cmd = event.getCommand().split(" ");
        Player player = event.getPlayer();

        if (cmd.length == 1) {
            if (cmd[0].toLowerCase().matches("/tpl")) {
                player.sendTextMessage(TextFormat.Color("orange", "====Teleporter-Liste===="));
                for (Teleporter tel : plugin.getTeleporterList()) {
                    player.sendTextMessage(TextFormat.Color("orange", "- " + tel.getID() + ": " + tel.getName()));
                }
                player.sendTextMessage(TextFormat.Color("orange", "========================"));
            }
        }

        if (cmd.length >= 2) {
            if (cmd[0].toLowerCase().matches("/tp")) {
                if (!cmd[1].toLowerCase().equals("help")) {
                    if (toNumber(cmd[1]) >= 0) {
                        Teleporter tel = plugin.getTeleporterByID(toNumber(cmd[1]));
                        if (tel != null) {
                            player.setPosition(tel.getPosition());
                            player.setRotation(tel.getRotation());
                            player.sendTextMessage(TextFormat.Color("green", "Du wurdest zum Teleporter '" + tel.getName() + "' teleportiert!"));
                        } else {
                            player.sendTextMessage(TextFormat.Color("red", "Teleporter '" + cmd[1] + "' existiert nicht!"));
                        }
                    } else {

                        if (plugin.hasTeleporter(cmd[1])) {
                            Teleporter tel = plugin.getTeleporterByName(cmd[1]);
                            player.setPosition(tel.getPosition());
                            player.setRotation(tel.getRotation());
                            player.sendTextMessage(TextFormat.Color("green", "Du wurdest zum Teleporter '" + cmd[1] + "' teleportiert!"));
                        } else {
                            player.sendTextMessage(TextFormat.Color("red", "Teleporter '" + cmd[1] + "' existiert nicht!"));
                        }
                    }
                } else {
                    player.sendTextMessage(TextFormat.Color("orange", "------- Teleporter Help -------"));
                    player.sendTextMessage(TextFormat.Color("orange", "/tp [Name/Nr] - Teleportiert dich zum Teleporter"));
                    player.sendTextMessage(TextFormat.Color("orange", "/tpl - Liste aller Teleporter"));
                    player.sendTextMessage(TextFormat.Color("orange", "/tpr [Name] - Löscht einen Teleport"));
                    player.sendTextMessage(TextFormat.Color("orange", "/tps [Name] - Setzt einen neuen Teleport oder bearbeitet ihn"));
                    player.sendTextMessage(TextFormat.Color("orange", "Hinweis: Zum umbenennen bitte Teleporter löschen und neu erstellen"));
                    player.sendTextMessage(TextFormat.Color("orange", "-------------------------------"));
                }
            }

            if (cmd[0].toLowerCase().matches("/tps")) {
                if (plugin.hasTeleporter(cmd[1])) {
                    try {
                        plugin.Database.editTeleporter(cmd[1], player.getPosition(), player.getRotation());
                        player.sendTextMessage(TextFormat.Color("green", "Teleporter '" + cmd[1] + "' erfogreich geändert!"));
                    } catch (SQLException ex) {
                        player.sendTextMessage(TextFormat.Color("red", "[DB] Teleporter konnte nicht bearbeitet werden!"));
                    }

                } else {
                    try {
                        if (plugin.addNewTeleporter(cmd[1], player) != null) {
                            player.sendTextMessage(TextFormat.Color("green", "Teleporter '" + cmd[1] + "' erfogreich erstellt!"));
                        } else {
                            player.sendTextMessage(TextFormat.Color("red", "Teleporter '" + cmd[1] + "' konnte nicht erstellt werden!"));
                        }
                    } catch (SQLException ex) {
                        player.sendTextMessage(TextFormat.Color("red", "[DB] Teleporter konnte nicht erstellt werden!"));
                    }
                }
            }
            if (cmd[0].toLowerCase().matches("/tpr")) {
                if (plugin.hasTeleporter(cmd[1])) {
                    try {
                        plugin.removeTeleporter(cmd[1]);
                        player.sendTextMessage(TextFormat.Color("green", "Teleporter '" + cmd[1] + "' erfogreich gelöscht!"));
                    } catch (SQLException ex) {
                        player.sendTextMessage(TextFormat.Color("red", "[DB] Teleporter konnte nicht gelöscht werden!"));
                    }

                }
            }

        }
    }

    private int toNumber(String nr) {
        int i;
        try {
            i = Integer.parseInt(nr);
        } catch (NumberFormatException ex) {
            return -1;
        }
        return i;
    }

}
