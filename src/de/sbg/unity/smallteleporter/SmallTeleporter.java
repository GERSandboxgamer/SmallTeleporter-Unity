package de.sbg.unity.smallteleporter;

import de.sbg.unity.smallteleporter.Objects.Teleporter;
import de.sbg.unity.smallteleporter.database.stDatabase;
import de.sbg.unity.smallteleporter.events.PlayerEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import net.risingworld.api.Plugin;
import net.risingworld.api.objects.Player;
import net.risingworld.api.utils.Quaternion;
import net.risingworld.api.utils.Vector3f;

public class SmallTeleporter extends Plugin {

    private List<Teleporter> TeleporterList;
    private stConsole Console;
    public stDatabase Database;
    private boolean update;

    @Override
    public void onEnable() {
        this.Console = new stConsole(this);
        Console.sendInfo("Enabled");
        this.TeleporterList = new ArrayList<>();
        try {
            this.Database = new stDatabase(this, Console);
        } catch (SQLException ex) {

        }
        Console.sendInfo("Check for Updates...");
        try {
            Update up = new Update(this, "http://gs.sandboxgamer.de/downloads/Plugins/risingworld/unity/SmallTeleporter/version.txt");
            update = up.hasUpdate();
        } catch (IOException | URISyntaxException ioex) {
            update = false;
        }
        registerEventListener(new PlayerEvent(this, update));
        
    }

    @Override
    public void onDisable() {
        try {
            Database.saveAllTeleporters();
            Database.close();
        } catch (SQLException ex) {
            Console.sendInfo("Can not save all Teleports to Database!");
        }
        Console.sendInfo("Disabled");
    }

    public Teleporter getTeleporterByName(String name) {
        for (Teleporter tel : TeleporterList) {
            if (tel.getName().equals(name)) {
                return tel;
            }
        }
        return null;
    }

    public Teleporter getTeleporterByID(int id) {
        for (Teleporter tel : TeleporterList) {
            if (tel.getID() == id) {
                return tel;
            }
        }
        return null;
    }

    public List<Teleporter> getTeleporterList() {
        return TeleporterList;
    }

    public boolean hasTeleporter(String name) {
        for (Teleporter tel : getTeleporterList()) {
            if (tel.getName().matches(name)) {
                return true;
            }
        }
        return false;
    }
    
    public void editTeleporter(String name, Vector3f pos, Quaternion rot) throws SQLException {
        Teleporter tel = getTeleporterByName(name);
        tel.setPosition(pos);
        tel.setRotation(rot);
        Database.editTeleporter(name, pos, rot);
    }

    public Teleporter addNewTeleporter(String name, Player player) throws SQLException {
        return addNewTeleporter(name, player.getPosition(), player.getRotation());
    }

    public Teleporter addNewTeleporter(String name, Vector3f pos, Quaternion rot) throws SQLException {
        int id = Database.setNewTeleporter(name, pos, rot);
        Teleporter tel = new Teleporter(id, name, pos, rot);
        getTeleporterList().add(tel);
        return tel;
    }

    public boolean removeTeleporter(String name) throws SQLException {
        if (hasTeleporter(name)) {
            Database.removeTeleporter(name);
            getTeleporterList().remove(getTeleporterByName(name));
            
            return true;
        }
        return false;
    }

    

}
