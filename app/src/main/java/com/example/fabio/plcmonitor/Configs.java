package com.example.fabio.plcmonitor;

public class Configs
{
    static String ip = "192.168.0.1";
    static int rack = 0;
    static int slot = 2;
    static int datablock = 22;

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
}