package com.codecool.dao;

import com.codecool.model.CollectionGroup;
import com.codecool.model.Student;

import java.util.List;

public interface ICollectionGroupDao {
    List<CollectionGroup> getAllCollection();
    void donateToCollection(int amount, CollectionGroup collectionGroup, Student student);
    void createCollection(CollectionGroup collection);
    void removeCollection(CollectionGroup collection);
}
