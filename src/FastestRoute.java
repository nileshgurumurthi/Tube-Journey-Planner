public class FastestRoute extends RouteType {

    private int changeLineTime = 5;
    private int changeRouteTime = 2;

    public FastestRoute(String routePreference) {
        super(routePreference);
    }

    @Override
    public int timeToStation(AdjStation fromStation, AdjStation toStation) {
        if (fromStation.getAdjacentId() != startingId){
            if (fromStation.getLineId() != toStation.getLineId()){
                return toStation.getTime() + changeLineTime;
            }
            else if (fromStation.getRouteId() != toStation.getRouteId()){
                return toStation.getTime() + changeRouteTime;
            }
        }
        return toStation.getTime();
    }
}
