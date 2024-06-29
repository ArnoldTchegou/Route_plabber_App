package fr.u_paris.gla.project.User_interface;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public final class Factory{
    public static Map<String, Color> createLineColorMap(){
        Map<String, Color> lineColorMap = new HashMap<>();
        lineColorMap.put("3", Color.GREEN);
        lineColorMap.put("4", Color.YELLOW);
        lineColorMap.put("5", new Color(255, 127, 0));
        lineColorMap.put("6", new Color(139, 69, 19));
        lineColorMap.put("7", Color.MAGENTA);
        lineColorMap.put("8", Color.PINK);
        lineColorMap.put("9", new Color(148, 0, 211));
        lineColorMap.put("10", new Color(0, 255, 255));
        lineColorMap.put("11", new Color(255, 0, 255));
        lineColorMap.put("12", new Color(0, 100, 0));
        lineColorMap.put("13", new Color(210, 105, 30));
        lineColorMap.put("3B", Color.CYAN);
        lineColorMap.put("7B", Color.LIGHT_GRAY);
        lineColorMap.put("14", Color.RED);
        return lineColorMap;
    }
}