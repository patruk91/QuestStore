package com.codecool.model;

public class Artifact {
    private int id;
    private String name;
    private String description;
    private int price;
    private String imageLink;
    private ArtifactCategoryEnum category;

    public Artifact(int id, String name, String description, int price, String imageLink, ArtifactCategoryEnum category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageLink = imageLink;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public ArtifactCategoryEnum getCategory() {
        return category;
    }

    public void setCategory(ArtifactCategoryEnum category) {
        this.category = category;
    }
}
