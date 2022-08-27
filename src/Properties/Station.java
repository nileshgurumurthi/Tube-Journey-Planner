package Properties;

import Properties.AdjStation;

import java.util.ArrayList;

public class Station {

    private String name;
    private ArrayList<AdjStation> adjacentStations = new ArrayList<>();

    public Station(String name){
        this.name = name;
    }

    public void addAdjacentStation (AdjStation newAdjacentStation){
        adjacentStations.add(newAdjacentStation);
    }

    public String getName(){
        return name;
    }

    public ArrayList<AdjStation> getAdjacentStations(){
        return adjacentStations;
    }


}
