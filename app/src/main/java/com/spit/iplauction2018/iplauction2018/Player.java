package com.spit.iplauction2018.iplauction2018;

public class Player {
    private String name;
    private String bidprice;
    private String points;
    private String price;
    private String type;
    public Player(){}

    public Player(String name, String type) {
        this.name = name;
        this.type = type;
    }
    public Player(String name, String bidprice, String points, String price, String type){
        this.name=name;
        this.price=price;
        this.bidprice=bidprice;
        this.type=type;
        this.points=points;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBidprice() {
        return bidprice;
    }

    public void setBidprice(String bidprice) {
        this.bidprice = bidprice;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
