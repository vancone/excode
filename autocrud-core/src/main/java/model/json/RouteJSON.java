package model.json;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class RouteJSON {
    private List<LinkedHashMap> routes = new ArrayList<>();

    public void addRouter(String path, String name, String component) {
        LinkedHashMap hashMap = new LinkedHashMap();
        hashMap.put("path", path);
        hashMap.put("name", name);
        hashMap.put("component", component);
        routes.add(hashMap);
    }
}
