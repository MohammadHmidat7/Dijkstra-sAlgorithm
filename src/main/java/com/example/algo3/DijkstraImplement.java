package com.example.algo3;

import java.util.ArrayList;

public class DijkstraImplement {

    public static TableEntry[] initializeTable(capital start, capital[] Graph) {

        TableEntry[] Table = new TableEntry[Graph.length];
        for (int i = 0; i < Graph.length; i++) {
            TableEntry e = new TableEntry();
            e.header = Graph[i];
            Table[i] = e;
        }

        for (int i = 0; i < Graph.length; i++) {
            Table[i].Known = false;
            Table[i].dist = Integer.MAX_VALUE;
            Table[i].price = Integer.MAX_VALUE;
            Table[i].time = Integer.MAX_VALUE;
            Table[i].path = null;
        }

        TableEntry Entry = TableEntry.getEntry(Table, start);
        if (Entry != null) {
            Entry.price = 0;
            Entry.time = 0;
            Entry.dist = 0;
        }

        return Table;
    }

    public static TableEntry[] Dijkstra(TableEntry[] Table, String mode) {

        for (; ; ) {
            capital v = null, w;

            // Smallest unKnown Distance capital
            double min = Double.MAX_VALUE;
            for (TableEntry tableEntry : Table) {
                if (mode.equals("Distance")) {
                    if (!tableEntry.Known && tableEntry.dist < min ) {//&& tableEntry.header.next != null
                        min = tableEntry.dist;
                        v = tableEntry.header;
                    }
                } else if (mode.equals("Cost")) {
                    if (!tableEntry.Known && tableEntry.price < min) {// && tableEntry.header.next != null
//                        System.out.println(1);
                        min = tableEntry.price;
                        v = tableEntry.header;
//                        System.out.println(min + " " + v.name);
                    }
                } else if (mode.equals("Time")) {
                    if (!tableEntry.Known && tableEntry.time < min) {//&& tableEntry.header.next != null
                        min = tableEntry.time;
                        v = tableEntry.header;

                    }
                }
            }

            if (v == null) {
                break;
            }

            TableEntry vEntry = TableEntry.getEntry(Table, v);
            if (vEntry != null) {
                vEntry.Known = true;
            } else {
                break;
            }

            Edge edge = v.next;


            while (edge != null) {


                w = edge.target;
                TableEntry wEntry = TableEntry.getEntry(Table, w);

                if (wEntry != null && !wEntry.Known) {
                    double newDist = vEntry.dist + haversine(vEntry.header.y, vEntry.header.x, w.y, w.x); //vEntry.price+ edge.price;
                    double newPrice = vEntry.price + edge.price;
                    double newTime = vEntry.time + edge.time;
                    if (mode.equals("Distance")) {
                        if (newDist < wEntry.dist) {
                            wEntry.dist = newDist;
                            wEntry.price = newPrice;
                            wEntry.time = newTime;
                            wEntry.path = v;
                        }
                    } else if (mode.equals("Cost")) {
                        if (newPrice < wEntry.price) {
                            wEntry.price = newPrice;
                            wEntry.dist = newDist;
                            wEntry.time = newTime;
                            wEntry.path = v;
//                            System.out.println(wEntry.header.name + " " + wEntry.path.name);
                        }
                    } else {
                        if (newTime < wEntry.time) {
                            wEntry.time = newTime;
                            wEntry.dist = newDist;
                            wEntry.price = newPrice;
                            wEntry.path = v;
//                            System.out.println(wEntry.header.name + " " + wEntry.path.name);
                        }
                    }

                }

                edge = edge.next;
            }

            boolean stop = true;

        }


        return Table;
    }

    static capital getCapital(capital[] Graph, String name) {
        for (capital capital : Graph) {
            if (capital.name.equals(name)) {
                return capital;
            }
        }
        return null;
    }

    static double haversine(double lat1, double lon1,
                            double lat2, double lon2) {
        // distance between latitudes and longitudes
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        // convert to radians
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        // apply formulae
        double a = Math.pow(Math.sin(dLat / 2), 2) +
                Math.pow(Math.sin(dLon / 2), 2) *
                        Math.cos(lat1) *
                        Math.cos(lat2);
        double rad = 6371;
        double c = 2 * Math.asin(Math.sqrt(a));
        return rad * c;
    }
}
