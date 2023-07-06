package de.sbg.unity.smallteleporter.Objects;

import net.risingworld.api.utils.Quaternion;
import net.risingworld.api.utils.Vector3f;

/**
 *
 * @author pbronke
 */
public class Teleporter {

    private final String Name;
    private Vector3f Position;
    private Quaternion Rotation;
    private final int ID; 

    public Teleporter(int id, String name, Vector3f position, Quaternion Rotation) {
        this.Name = name;
        ID = id;
        this.Position = position;
        this.Rotation = Rotation;
    }

    public String getName() {
        return Name;
    }

    public Vector3f getPosition() {
        return Position;
    }

    public Quaternion getRotation() {
        return Rotation;
    }

    public int getID() {
        return ID;
    }
    
    public void setPosition(Vector3f Position) {
        this.Position = Position;
    }

    public void setRotation(Quaternion Rotation) {
        this.Rotation = Rotation;
    }
    
    

}
