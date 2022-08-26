public class LeastStops extends RouteType {

    private int changeStopTime = 1;

    public LeastStops(String routePreference) {
        super(routePreference);
    }

    @Override
    public int timeToStation(AdjStation fromStation, AdjStation toStation) {
        return changeStopTime;
    }

}
