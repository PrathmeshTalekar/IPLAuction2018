package com.spit.iplauction2018.iplauction2018;

public class Player {
    private String name;
    private String bidprice;
    private String points;
    private String price;
    private String type;
    private int sold;
    private String ownedBy;
    private String ownerName;
    long timestamp;

    public Player(){}

    public Player(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public int getSold() {
        return sold;
    }

    public void setSold(int sold) {
        this.sold = sold;
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

    public String getOwnedBy() {
        return ownedBy;
    }

    public void setOwnedBy(String ownedBy) {
        this.ownedBy = ownedBy;
    }
}
