package com.codecool.dao;

import com.codecool.model.CollectionGroup;

import java.util.List;

public interface ICollectionGroupDao {
    List<CollectionGroup> getAllCollection();
    void donateToCollection(int amount, int idCollection);
    void createCollection(CollectionGroup collection);
    void removeCollection(CollectionGroup collection);
}
