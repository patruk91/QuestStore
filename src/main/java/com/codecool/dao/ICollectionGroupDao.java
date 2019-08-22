package com.codecool.dao;

import com.codecool.model.CollectionGroup;
import com.codecool.model.Student;

import java.util.List;

public interface ICollectionGroupDao {
    List<CollectionGroup> getAllCollection();
    void donateToCollection(int amount, int collectionGroupId, int studentId);
    void createCollection(CollectionGroup collection);
    void removeCollection(CollectionGroup collection);
}
