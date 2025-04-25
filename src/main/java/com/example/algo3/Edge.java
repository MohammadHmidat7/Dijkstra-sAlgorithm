package com.example.algo3;

public class Edge {

    public capital target;
    public double price;
    public double time;
    public Edge next ;

    public Edge(capital target, double price, int time) {
        this.target = target;
        this.price = price;
        this.time = time;
        this.next = null;
    }
    public Edge(capital target, double price) {
        this.target = target;
        this.price = price;
        this.next = null;
    }
}
