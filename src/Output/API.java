package Output;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class API {

    public ArrayList<String> fetchDelays() throws Exception{
        ArrayList<String> delays = new ArrayList<>();
        StringBuilder informationString = fetchData("https://api.tfl.gov.uk/Line/Mode/tube/Disruption");
        JSONParser parse = new JSONParser();
        JSONArray dataObject = (JSONArray) parse.parse(String.valueOf(informationString));
        for (int i = 0; i < dataObject.size(); i++){
            JSONObject delay = (JSONObject) dataObject.get(i);
            String delayName = (String) delay.get("description");
            delays.add(delayName);
        }
        return delays;
    }

    public StringBuilder fetchData(String urlName) throws Exception{
        StringBuilder informationString = new StringBuilder();
        URL url = new URL (urlName);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();
        int responseCode = conn.getResponseCode();
        if (responseCode != 200){
            throw new Exception("Failed");
        }
        else{
            Scanner scanner = new Scanner(url.openStream());
            while (scanner.hasNext()) {
                informationString.append(scanner.nextLine());
            }
            scanner.close();
        }
        return informationString;
    }
}
