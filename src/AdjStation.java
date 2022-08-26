public class AdjStation {

    private int adjacentId;

    private int lineId;

    private int routeId;

    private int time;

    public AdjStation(int adjacentId,int lineId,int routeId,int time){
        this.adjacentId = adjacentId;
        this.lineId = lineId;
        this.routeId = routeId;
        this.time = time;
    }

    public boolean isEqual(AdjStation station){
        boolean isEqualId = station.getAdjacentId() == adjacentId;
        boolean isEqualLine = station.getLineId() == lineId;
        boolean isEqualRoute = station.getRouteId() == routeId;
        if (isEqualId && isEqualLine && isEqualRoute){
            return true;
        }
        return false;
    }

    public int getAdjacentId(){
        return adjacentId;
    }

    public int getLineId(){
        return lineId;
    }

    public int getRouteId(){
        return routeId;
    }

    public int getTime(){
        return time;
    }

}
