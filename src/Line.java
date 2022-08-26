import java.util.ArrayList;
import java.util.HashMap;

public class Line {

    private String name;

    private ArrayList<Integer> stations = new ArrayList<>();

    private HashMap<Integer,Route> routes = new HashMap<>();

    public Line(String name){
        this.name = name;
    }

    public void addStation(int newStation){
        if (!stations.contains(newStation)){
            stations.add(newStation);
        }
    }

    public void addRoute(int routeId,Route newRoute){
        routes.put(routeId,newRoute);
    }

    public String getName(){
        return name;
    }

    public ArrayList<Integer> getStations(){
        return stations;
    }

    public HashMap<Integer,Route> getRoutes(){
        return routes;
    }

    public boolean isStationPresent(int station){
        return stations.contains(station);
    }

    public int getNrRoutes (){
        return routes.size();
    }

}
