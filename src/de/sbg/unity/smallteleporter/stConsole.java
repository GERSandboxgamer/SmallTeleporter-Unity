package de.sbg.unity.smallteleporter;


public class stConsole {
    
    private final SmallTeleporter plugin;

    public stConsole(SmallTeleporter plugin) {
        this.plugin = plugin;
    }

    public void sendInfo(Object msg) {
        System.out.println("[" + plugin.getDescription("name") + "-Info] " + msg);
    }
    public void sendInfo(String ClassName, Object msg) {
        System.out.println("[" + plugin.getDescription("name") + "-Info] [" + ClassName + "] " + msg);
    }
        
    public void sendErr(Object msg) {
        System.err.println("[" + plugin.getDescription("name") + "-ERROR] " + msg);
    }
    public void sendErr(String ClassName, Object msg) {
        System.err.println("[" + plugin.getDescription("name") + "-ERROR] [" + ClassName + "] " + msg);
    }
    
    public void sendWarning(Object msg) {
        System.out.println("[" + plugin.getDescription("name") + "-Warning] " + msg);
    }
    public void sendWarning(String ClassName, Object msg) {
        System.out.println("[" + plugin.getDescription("name") + "-Warning] [" + ClassName + "] " + msg);
    }
    
    public void sendDebug(Object msg) {
        System.out.println("[" + plugin.getDescription("name") + "-Debug] " + msg);
    }
    public void sendDebug(String ClassName, Object msg) {
        System.out.println("[" + plugin.getDescription("name") + "-Debug] [" + ClassName + "] " + msg);
    }
    
}
