package de.sbg.unity.smallteleporter.database;

import de.sbg.unity.smallteleporter.Objects.Teleporter;
import de.sbg.unity.smallteleporter.SmallTeleporter;
import de.sbg.unity.smallteleporter.stConsole;
import net.risingworld.api.World;
import net.risingworld.api.database.Database;
import net.risingworld.api.utils.Quaternion;
import net.risingworld.api.utils.Vector3f;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class stDatabase {

    private final SmallTeleporter plugin;
    private final Database Database;
    private final Connection conn;
    private PreparedStatement pstmt;
    private final stConsole Console;

    public stDatabase(SmallTeleporter plugin, stConsole Console) throws SQLException {
        this.Console = Console;
        this.plugin = plugin;
        this.Database = plugin.getSQLiteConnection(plugin.getPath() + "/database/" + plugin.getDescription("name") + "-" + World.getName() + ".db");
        conn = Database.getConnection();
        iniTeleports();
    }

    private void iniTeleports() throws SQLException {
        Database.execute("CREATE TABLE IF NOT EXISTS Tele ("
                + "ID INTEGER PRIMARY KEY NOT NULL, " //AUTOINCREMENT
                + "Teleporter TXT,"
                + "PosX FLOAT,"
                + "PosY FLOAT,"
                + "PosZ FLOAT,"
                + "RotX FLOAT,"
                + "RotY FLOAT,"
                + "RotZ FLOAT,"
                + "RotW FLOAT,"
                + "More TXT "
                + "); ");
        loadAllTeleporters();
    }

    public void loadAllTeleporters() throws SQLException {
        String Name;
        Vector3f Pos;
        float f1, f2, f3, r1, r2, r3, r4;
        Quaternion Rot;
        int id;
        Console.sendInfo("DB", "Load Teleporters from Database....");
        try (ResultSet result = Database.executeQuery("SELECT * FROM 'Tele'")) {
            while (result.next()) {
                id = result.getInt("ID");
                f1 = result.getFloat("PosX");
                f2 = result.getFloat("PosY");
                f3 = result.getFloat("PosZ");
                r1 = result.getFloat("RotX");
                r2 = result.getFloat("RotY");
                r3 = result.getFloat("RotZ");
                r4 = result.getFloat("RotW");
                Name = result.getString("Teleporter");
                Pos = new Vector3f(f1, f2, f3);
                Rot = new Quaternion(r1, r2, r3, r4);
                Console.sendInfo("DB", "- "  + Name);
                Teleporter tel = new Teleporter(id, Name, Pos, Rot);
                plugin.getTeleporterList().add(tel);
            }
        }
        Console.sendInfo("DB", "Done!");

    }

    public void close() throws SQLException {
        conn.close();
        Database.close();
    }

    public void saveAllTeleporters() throws SQLException {
        if (!plugin.getTeleporterList().isEmpty()) {
            Console.sendInfo("Database", "Save teleporters to Database....");
            Console.sendInfo("Database", "--------------------------------");
            for (Teleporter tel : plugin.getTeleporterList()) {
                Console.sendInfo("Database", "- " + tel.getName());
                pstmt = conn.prepareStatement("UPDATE Tele SET Teleporter=?, PosX=?, PosY=?, PosZ=?, RotX=?, RotY=?, RotZ=?, RotW=? WHERE ID=" + tel.getID());
                pstmt.setString(1, tel.getName());
                pstmt.setFloat(2, tel.getPosition().x);
                pstmt.setFloat(3, tel.getPosition().y);
                pstmt.setFloat(4, tel.getPosition().z);
                pstmt.setFloat(5, tel.getRotation().x);
                pstmt.setFloat(6, tel.getRotation().y);
                pstmt.setFloat(7, tel.getRotation().z);
                pstmt.setFloat(8, tel.getRotation().w);
                pstmt.executeUpdate();
                pstmt.close();
            }
            Console.sendInfo("Database", "--------------------------------");
            Console.sendInfo("Database", "Done!");
        }
    }

    public int setNewTeleporter(String name, Vector3f pos, Quaternion rot) throws SQLException {
        pstmt = conn.prepareStatement("INSERT INTO Tele (Teleporter, PosX, PosY, PosZ, RotX, RotY, RotZ, RotW) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
        pstmt.setString(1, name);
        pstmt.setFloat(2, pos.x);
        pstmt.setFloat(3, pos.y);
        pstmt.setFloat(4, pos.z);
        pstmt.setFloat(5, rot.x);
        pstmt.setFloat(6, rot.y);
        pstmt.setFloat(7, rot.z);
        pstmt.setFloat(8, rot.w);
        pstmt.executeUpdate();
        pstmt.close();

        int id;
        try (ResultSet result = Database.executeQuery("SELECT * FROM 'Tele' WHERE Teleporter='" + name + "'")) {
            id = result.getInt("ID");
        }

        return id;
    }

    public void editTeleporter(String name, Vector3f pos, Quaternion rot) throws SQLException {
        pstmt = conn.prepareStatement("UPDATE Tele SET PosX=?, PosY=?, PosZ=?, RotX=?, RotY=?, RotZ=?, RotW=? WHERE Teleporter='" + name + "'");
        pstmt.setFloat(1, pos.x);
        pstmt.setFloat(2, pos.y);
        pstmt.setFloat(3, pos.z);
        pstmt.setFloat(4, rot.x);
        pstmt.setFloat(5, rot.y);
        pstmt.setFloat(6, rot.z);
        pstmt.setFloat(7, rot.w);
        pstmt.executeUpdate();
        pstmt.close();
    }

    public void removeTeleporter(String name) throws SQLException {
        pstmt = conn.prepareStatement("DELETE FROM Tele WHERE Teleporter='" + name + "'");
        pstmt.executeUpdate();
        pstmt.close();
    }

}
