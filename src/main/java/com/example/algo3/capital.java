package com.example.algo3;

public class capital {

    public String name;
    public double y;
    public double x;
    public Edge next;

    public capital(String name, double y, double x) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.next = null;
    }



    public void add(Edge edge){
        Edge last = this.next;
        if (last == null){
            this.next = edge;
        } else {
            while (last.next != null) {
                last = last.next;
            }
            last.next = edge;
        }
    }
}
