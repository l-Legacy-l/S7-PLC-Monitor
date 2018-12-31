package com.example.fabio.plcmonitor;

public class Configs
{
    static String ip = "192.168.0.10";
    static int rack = 0;
    static int slot = 2;
    static int datablock = 5;
    static boolean isWriteAccess = false;
    static boolean isAdmin = false;

    public static String getIp() {
        return ip;
    }

    public static void setIp(String ip) {
        Configs.ip = ip;
    }

    public static int getRack() {
        return rack;
    }

    public static void setRack(int rack) {
        Configs.rack = rack;
    }

    public static int getSlot() {
        return slot;
    }

    public static void setSlot(int slot) {
        Configs.slot = slot;
    }
    public static int getDatablock() {
        return datablock;
    }

    public static void setDatablock(int datablock) {
        Configs.datablock = datablock;
    }

    public static void setIsWriteAccess(boolean writeAccess){Configs.isWriteAccess = writeAccess;}

    public static boolean getIsWriteAccess() {return isWriteAccess;}

    public static boolean getIsAdmin() {
        return isAdmin;
    }

    public static void setIsAdmin(boolean isAdmin) {
        Configs.isAdmin = isAdmin;
    }
}

