//Name: Yash Hooda
//Date: 11/16/2022
//This project imports a flight plan and prints out the most efficient flight plan related to by cost and distance between
// 2 cities

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

//flight path class
public class FlightPlan {

    private static List<FlightPathStat> mainFlightPathStat = new ArrayList<>();

    private static String begin;

    private static String _destinationLoc;

    private static FlightPathStat state;

    private static boolean findCity = false;

    public static void main(String[] args) throws CloneNotSupportedException {

        HashMap cityMap = new HashMap();
        List<City> _visitedCity = new ArrayList<>();
        String inputFileName = args[0];
        File inputFile = new File(inputFileName);
        if (!(inputFile.exists())) {
            System.out.println("File does not exist");
            return;

        }
        BufferedReader reader = null;
        try {
            String readLine;
            reader = new BufferedReader(new FileReader(inputFileName));
            while ((readLine = reader.readLine()) != null) {
                if (readLine.contains("|")) {
                    String a[] = readLine.split("\\|");
                    City c;
                    if (!cityMap.containsKey(a[0])) {
                        c = new City();
                        cityMap.put(a[0], c);

                    }
                    c = (City) cityMap.get(a[0]);
                    c.name = a[0];
                    c.time.put(a[1], a[3]);
                    c.cost.put(a[1], a[2]);
                    if (!cityMap.containsKey(a[1])) {
                        c = new City();
                        cityMap.put(a[1], c);
                    }
                    c = (City) cityMap.get(a[1]);
                    c.name = a[1];
                    c.time.put(a[0], a[3]);
                    c.cost.put(a[0], a[2]);
                }

            }
            System.out.println(cityMap);
            reader.close();
            FileWriter writer = new FileWriter(args[1]);
            reader = new BufferedReader(new FileReader(inputFileName));
            int count = 1;
            int k = 0;
            while ((readLine = reader.readLine()) != null) {
                if (readLine.contains("|")) {
                    String a[] = readLine.split("\\|");
                    String output = "Flight" + String.valueOf(count) + ":";
                    output += a[0] + "," + a[1];
                    if ("T".equals(a[2])) {
                        output += "(Time)\n";
                    }
                    if ("C".equals(a[2])) {
                        output += "(Cost)\n";
                    }

                    FlightPathStat r = new FlightPathStat();
                    r.setFlightPath(a[0]);
                    begin = a[0];
                    _destinationLoc = a[1];
                    state = null;
                    findCity = false;
                    mainFlightPathStat.clear();
                    shortestFlightPath(a[0], a[1], cityMap, _visitedCity, r);
                    _visitedCity.clear();
                    if (mainFlightPathStat.size() > 0) {
                        if ("T".equals(a[2]))
                            Collections.sort(mainFlightPathStat, new TimeSorter());
                        if ("C".equals(a[2]))
                            Collections.sort(mainFlightPathStat, new CostSorter());
                        k++;

                        for (FlightPathStat r1 : mainFlightPathStat) {
                            output += "FlightPath" + String.valueOf(k) + ":" + r1.getFlightPath() + "." + "Time:"
                                    + String.valueOf(r1.getTime()) + " " + "Cost:" + String.valueOf(r1.getCost())
                                    + "\n";
                            k++;
                        }

                    } else {
                        output += "No Flight exist between " + begin + " and " + _destinationLoc + "\n";
                    }
                    k = 0;
                    count++;
                    writer.write(output + "\n\n");
                }
            }

            writer.flush();
            writer.close();
            System.out.println(mainFlightPathStat);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void shortestFlightPath(String start, String _destination, HashMap cityMap, List _visitedCity,
            FlightPathStat store) throws CloneNotSupportedException {
        City c = (City) cityMap.get(start);
        Set<String> s = c.time.keySet();
        _visitedCity.add(start);
        for (String city : s) {
            if (findCity) {
                store = state;
            }

            if (_visitedCity.contains(city))
                continue;
            String t = (String) c.time.get(city);
            String cost = (String) c.cost.get(city);
            state = (FlightPathStat) store.clone();
            store.setTime(store.getTime() + Integer.parseInt(t));
            store.setCost(store.getCost() + Integer.parseInt(cost));
            store.setFlightPath(store.getFlightPath() + "->" + city);
            if (_destination.equals(city)) {
                mainFlightPathStat.add(store);
                store = null;
                findCity = true;
                store = new FlightPathStat();
                _visitedCity.clear();

            } else {
                mainFlightPathStat.add(store);
                findCity = false;
                shortestFlightPath(city, _destination, cityMap, _visitedCity, store);
                if (!findCity) {
                    store = state;
                }

                if (findCity) {
                    store = new FlightPathStat();
                    store.setFlightPath(begin);
                    findCity = false;
                }
            }
        }
    }

}

class City {
    HashMap time = new HashMap();
    HashMap cost = new HashMap();
    String name;

    public City() {
        name = "";
    }

    @Override

    public String toString() {

        return time.toString() + " " + cost.toString();

    }

}

class FlightPathStat implements Cloneable {

    private int time;

    private int cost;

    private String path;

    FlightPathStat() {

        setTime(0);

        setCost(0);

        setFlightPath(new String());

    }

    @Override

    protected Object clone() throws CloneNotSupportedException {

        return super.clone();

    }

    @Override

    public String toString() {
        return "Time = " + getTime() + "Cost = " + getCost() + "FlightPath = " + getFlightPath();
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getFlightPath() {
        return path;
    }

    public void setFlightPath(String path) {
        this.path = path;
    }

}

class TimeSorter implements Comparator<FlightPathStat> {

    @Override

    public int compare(FlightPathStat o1, FlightPathStat o2) {
        return o1.getTime() - o2.getTime();
    }

}

class CostSorter implements Comparator<FlightPathStat> {
    @Override

    public int compare(FlightPathStat o1, FlightPathStat o2) {
        return o1.getCost() - o2.getCost();
    }

}