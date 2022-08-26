public abstract class RouteType {

    protected int startingId;

    protected String routePreference;

    public RouteType (String routePreference){
        this.routePreference = routePreference;
    }

    public abstract int timeToStation(AdjStation fromStation, AdjStation toStation);

    public void setStartingId (int startingId){
        this.startingId = startingId;
    }

    public String getRoutePreference(){
        return routePreference;
    }

    public int getStartingId(){
        return startingId;
    }

}
