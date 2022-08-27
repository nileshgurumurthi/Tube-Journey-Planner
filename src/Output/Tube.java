package Output;

import Properties.AdjStation;
import Properties.Line;
import Properties.Route;
import Properties.Station;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Tube {

    private HashMap<Integer, Station> stations = new HashMap<>();
    private HashMap<Integer, Line> lines = new HashMap<>();
    private ArrayList<AdjStation> removedStations = new ArrayList<>();

    public Tube() throws Exception{
        loadStations();
        loadLine("Bakerloo",0);
        loadLine("Central",1);
        loadLine("Circle",2);
        loadLine("District",3);
        loadLine("DLR",4);
        loadLine("Elizabeth",5);
        loadLine("Hammersmith & City",6);
        loadLine("Jubilee",7);
        loadLine("Northern",8);
        loadLine("Metropolitan",9);
        loadLine("Piccadilly",10);
        loadLine("Victoria",11);
        loadLine("Waterloo & City",12);
    }

    private void loadStations() throws Exception {
        File stationFile = new File("Lines/Tube Map Stations.csv");
        Scanner sc = new Scanner(stationFile);
        while (sc.hasNextLine()) {
            String [] nextLine = sc.nextLine().split(",");
            stations.put(Integer.parseInt(nextLine[0]), new Station(nextLine[1]));
        }
    }

    private void loadLine(String lineName, int lineId) throws Exception {
        Line newLine = new Line(lineName);
        lines.put(lineId,newLine);
        File lineFile = new File("Lines/" + lineName);
        File[] files = lineFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            loadRoute(files[i].getName(),lineId,i);
        }

    }

    private void loadRoute(String fileDirectory, int lineId, int routeId) throws Exception {
        File path = new File("Lines/"+ lines.get(lineId).getName()+"/"+fileDirectory);
        Route newRoute = new Route(fileDirectory.replace(".csv", ""));
        Scanner sc = new Scanner(path);
        while (sc.hasNextLine()) {
            String [] nextLine = sc.nextLine().split(",");
            int firstStation = Integer.parseInt(nextLine[2]);
            int secondStation = Integer.parseInt(nextLine[3]);
            int time = Integer.parseInt(nextLine[4]);
            lines.get(lineId).addStation(firstStation);
            lines.get(lineId).addStation(secondStation);
            makeAdjacent(firstStation,secondStation,lineId,routeId,time);

        }
        lines.get(lineId).addRoute(routeId,newRoute);

    }

    private void makeAdjacent(int firstStation, int secondStation, int lineId, int routeId, int time){
        stations.get(firstStation).addAdjacentStation(new AdjStation(secondStation,lineId,routeId,time));
        stations.get(secondStation).addAdjacentStation(new AdjStation(firstStation,lineId,routeId,time));

    }


    public void removeStation (int stationId, int lineId){
        removedStations.add(new AdjStation(stationId,lineId,-1,-1));
    }

    public boolean isStationRemoved(AdjStation station){
        int stationId = station.getAdjacentId();
        int lineId = station.getLineId();
        for (AdjStation deletedStation : removedStations){
            if (deletedStation.getAdjacentId() == stationId && deletedStation.getLineId() == lineId){
                return true;
            }
        }
        return false;
    }

    public int getNrStations(){
        return stations.size();
    }

    public int getNrLines(){
        return lines.size();
    }

    public Station getStation(int stationId){
        return stations.get(stationId);
    }

    public Line getLine(int lineId){
        return lines.get(lineId);
    }



}
