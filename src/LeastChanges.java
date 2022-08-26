public class LeastChanges extends RouteType {

    private int changeoverTime = 1;
    private int normalTime = 0;

    public LeastChanges(String routePreference) {
        super(routePreference);
    }

    @Override
    public int timeToStation(AdjStation fromStation, AdjStation toStation) {
        if (fromStation.getAdjacentId() != startingId){
            if (fromStation.getLineId() != toStation.getLineId() || fromStation.getRouteId() != toStation.getRouteId()){
                return changeoverTime;
            }
        }
        return normalTime;
    }
}
