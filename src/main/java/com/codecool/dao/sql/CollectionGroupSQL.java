package com.codecool.dao.sql;

import com.codecool.dao.ICollectionGroupDao;
import com.codecool.model.CollectionGroup;

import java.util.List;

public class CollectionGroupSQL implements ICollectionGroupDao {
    private BasicConnectionPool connectionPool;

    public CollectionGroupSQL(BasicConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public List<CollectionGroup> getAllCollection() {
        return null;
    }

    @Override
    public void donateToCollection(int amount, int idCollection) {

    }

    @Override
    public void createCollection(CollectionGroup collection) {
        
    }

    @Override
    public void removeCollection(CollectionGroup collection) {

    }
}
