package hu.elte.szhor.utils;

import org.json.JSONObject;

public class Statistics {

    public static int numberOfSources = 1;
    public static int numberOfNodes;
    public static int numberOfEdges;
    public static int numberOfRobots;
    public static int numberOfMoves;
    public static int numberOfCrashes;

    public static String getJsonString() {
        var json = new JSONObject();
        json.put("numberOfSources", numberOfSources);
        json.put("numberOfNodes",   numberOfNodes);
        json.put("numberOfEdges",   numberOfEdges);
        json.put("numberOfRobots",  numberOfRobots);
        json.put("numberOfMoves",   numberOfMoves);
        json.put("numberOfCrashes", numberOfCrashes);
        return json.toString();
    }
}
