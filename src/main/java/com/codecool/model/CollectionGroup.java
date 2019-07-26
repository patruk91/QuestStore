package com.codecool.model;

public class CollectionGroup {
    private int collectionId;
    private int coinsCollected;
    private Artifact artifact;
    private String nameOfCollection;

    public CollectionGroup(int collectionId, int coinsCollected, Artifact artifact, String nameOfCollection) {
        this.collectionId = collectionId;
        this.coinsCollected = coinsCollected;
        this.artifact = artifact;
        this.nameOfCollection = nameOfCollection;
    }

    public int getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(int collectionId) {
        this.collectionId = collectionId;
    }

    public int getCoinsCollected() {
        return coinsCollected;
    }

    public void setCoinsCollected(int coinsCollected) {
        this.coinsCollected = coinsCollected;
    }

    public Artifact getArtifact() {
        return artifact;
    }

    public void setArtifact(Artifact artifact) {
        this.artifact = artifact;
    }

    public String getNameOfCollection() {
        return nameOfCollection;
    }

    public void setNameOfCollection(String nameOfCollection) {
        this.nameOfCollection = nameOfCollection;
    }
}
