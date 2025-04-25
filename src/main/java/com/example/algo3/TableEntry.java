package com.example.algo3;

import java.util.ArrayList;
import java.util.List;

public class TableEntry {

    public capital header;
    boolean Known;
    double dist;
    double price;
    double time;
    public capital path;


    static TableEntry getEntry(TableEntry[] Table,capital v){
        for (TableEntry tableEntry : Table) {
            if (tableEntry.header.name.equals(v.name)) {
                return tableEntry;
            }
        }
        return null;
    }
}
