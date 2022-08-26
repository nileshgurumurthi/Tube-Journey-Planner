import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class ShortestPath {

    private ArrayList<ArrayList<ArrayList<Object>>> pred;
    private ArrayList<ArrayList<ArrayList<Object>>> time;
    private int startingId;
    private int destinationId;
    private static int currentPreference = 1;
    private static HashMap<Integer,RouteType> priority;
    RouteType chosenRoute;
    private Tube myTube;

    static{
        priority = new HashMap<>();
        priority.put(1,new FastestRoute("Fastest Route"));
        priority.put(2,new LeastStops("Fewest Stops"));
        priority.put(3,new LeastChanges("Fewest Changes"));
    }

    public ShortestPath(Tube myTube, int startingId,int destinationId) {
        this.myTube = myTube;
        this.startingId = startingId;
        this.destinationId = destinationId;
        this.chosenRoute = getPriority();
        pred = initArray(new AdjStation(-1,-1,-1,-1));
        time = initArray(Integer.MAX_VALUE);
    }


    public static void setPriority(int preference){
        currentPreference = preference;
    }

    public static RouteType getPriority(){
        return priority.get(currentPreference);
    }

    public ArrayList<ArrayList<ArrayList<Object>>> initArray(Object init){
        ArrayList<ArrayList<ArrayList<Object>>> array =  new ArrayList<>();
        for (int station = 0; station < myTube.getNrStations(); station++){
            array.add(new ArrayList<ArrayList<Object>>());
            for (int line = 0; line < myTube.getNrLines(); line++){
                array.get(station).add(new ArrayList<Object>());
                for (int route = 0; route < myTube.getLine(line).getNrRoutes(); route++){
                    array.get(station).get(line).add(init);
                }
            }
        }
        return array;
    }

    public void findShortestPath(){
        chosenRoute.setStartingId(startingId);
        for (int line = 0; line < time.get(startingId).size(); line ++){
            for (int route = 0; route < time.get(startingId).get(line).size(); route++){
                time.get(startingId).get(line).set(route,0);
            }
        }
        dijkstra();

    }

    public void dijkstra (){
        LinkedList<AdjStation> queue = new LinkedList<>();
        queue.add(new AdjStation(startingId,0,0,0));
        while (!queue.isEmpty()){
            AdjStation currentStation = queue.poll();
            int currentStationId = currentStation.getAdjacentId();
            for (AdjStation nextStation : myTube.getStation(currentStationId).getAdjacentStations()){
                if (isJourneyValid(currentStation,nextStation) && !(isFalseStart(currentStation,nextStation))){
                    if (isQuickestPath(currentStation,nextStation)){
                        queue.add(nextStation);
                    }
                }
            }
        }
    }

    public boolean isQuickestPath(AdjStation currentStation,AdjStation nextStation){
        int currentTime = (Integer) getValueFromArray(time,currentStation);
        int timeToStation = chosenRoute.timeToStation(currentStation,nextStation);
        int shortestTime = (Integer) getValueFromArray(time,nextStation);
        if (currentTime + timeToStation < shortestTime){
            setValueInArray(time,nextStation,currentTime + timeToStation);
            setValueInArray(pred,nextStation,currentStation);
            return true;
        }
        return false;
    }

    public boolean isJourneyValid(AdjStation currentStation, AdjStation nextStation){
        int currentStationLine = currentStation.getLineId();
        int currentStationRoute = currentStation.getRouteId();
        int nextStationLine = nextStation.getLineId();
        int nextStationRoute = nextStation.getRouteId();
        boolean isSameLine = currentStationLine == nextStationLine;
        boolean isSameRoute = currentStationRoute == nextStationRoute;
        if (!(isAtStart(currentStation))){
            if (myTube.isStationRemoved(currentStation) && !(isSameLine && isSameRoute)){
                return false;
            }
        }
        return true;

    }

    public boolean isFalseStart(AdjStation currentStation,AdjStation nextStation){
        if (isAtStart(currentStation)){
            AdjStation station = new AdjStation(startingId, nextStation.getLineId(), -1,-1);
            if (myTube.isStationRemoved(station)){
                return true;
            }
        }
        return false;
    }

    public boolean isAtStart(AdjStation currentStation){
        if (currentStation.getAdjacentId() == startingId){
            return true;
        }
        return false;
    }

    public AdjStation getStartingPoint(){
        AdjStation lastStation = new AdjStation(-1,-1,-1,-1);
        int minTime = Integer.MAX_VALUE;
        for (int line = 0; line < time.get(destinationId).size(); line++){
            for (int route = 0; route < time.get(destinationId).get(line).size(); route++){
                AdjStation currentStation = new AdjStation(destinationId,line,route,0);
                if (!(myTube.isStationRemoved(currentStation))){
                    if ((Integer) getValueFromArray(time,currentStation) < minTime){
                        minTime = (Integer) getValueFromArray(time,currentStation);
                        lastStation = currentStation;
                    }
                }
            }
        }
        lastStation = timeToStation(lastStation);
        return lastStation;
    }

    public AdjStation timeToStation (AdjStation station){
        AdjStation newStation = station;
        if (newStation.getAdjacentId() != -1 && !(isAtStart(newStation))){
            AdjStation previousStation = (AdjStation) getValueFromArray(pred, newStation);
            int previousStationId = previousStation.getAdjacentId();
            for (AdjStation adjacent : myTube.getStation(previousStationId).getAdjacentStations()){
                if (adjacent.isEqual(newStation)){
                    int stationId = newStation.getAdjacentId();
                    int lineId = newStation.getLineId();
                    int routeId = newStation.getRouteId();
                    int time = adjacent.getTime();
                    newStation = new AdjStation(stationId,lineId,routeId,time);
                    break;
                }
            }
        }
        return newStation;
    }

    public ArrayList<AdjStation> getRawPath(){
        ArrayList<AdjStation> path = new ArrayList<>();
        AdjStation currentStation = getStartingPoint();
        while (currentStation.getAdjacentId() != -1 && !(isAtStart(currentStation))){
            int stationId = currentStation.getAdjacentId();
            int lineId = currentStation.getLineId();
            int routeId = currentStation.getRouteId();
            int time = currentStation.getTime();
            path.add(new AdjStation(stationId, lineId, routeId, time));
            currentStation = (AdjStation) getValueFromArray(pred, currentStation);
        }
        return path;
    }

    public ArrayList<AdjStation> getShortestPath(){
        ArrayList<AdjStation> path = getRawPath();
        for (int i = 0; i < path.size(); i++){
            if (myTube.isStationRemoved(getRawPath().get(i))){
                path.remove(i);
            }
        }
        return path;
    }

    public int getNrStops(){
        return getShortestPath().size();
    }

    public int getTotalTime(){
        ArrayList<AdjStation> path = getRawPath();
        int time = getRawPath().get(path.size() - 1).getTime();
        for (int i = getRawPath().size() - 1; i > 0; i--){
            AdjStation currentStation = getRawPath().get(i);
            AdjStation nextStation = getRawPath().get(i-1);
            time = time + priority.get(1).timeToStation(currentStation,nextStation);
        }
        return time;

    }

    public int getNrChanges(){
       int currentLineId = getStartingPoint().getLineId();
       int currentRouteId = getStartingPoint().getRouteId();
       int nrChanges = 0;
       for (AdjStation station : getShortestPath()){
           if (!isAtStart(station)){
               int stationLineId = station.getLineId();
               int stationRouteId = station.getRouteId();
               if (stationLineId != currentLineId || stationRouteId != currentRouteId){
                   nrChanges ++;
                   currentLineId = stationLineId;
                   currentRouteId = stationRouteId;
               }
           }
       }
       return nrChanges;
    }


    public Object getValueFromArray(ArrayList<ArrayList<ArrayList<Object>>> array, AdjStation station){
        return array.get(station.getAdjacentId()).get(station.getLineId()).get(station.getRouteId());
    }

    public Object setValueInArray(ArrayList<ArrayList<ArrayList<Object>>> array, AdjStation station, Object value){
        return array.get(station.getAdjacentId()).get(station.getLineId()).set(station.getRouteId(),value);
    }

}
