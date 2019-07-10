package com.codecool.dao.sql;

import com.codecool.dao.IArtifactDao;
import com.codecool.model.Artifact;
import com.codecool.model.Student;

import java.util.List;

public class ArtifactSQL implements IArtifactDao {
    private BasicConnectionPool connectionPool;

    public ArtifactSQL(BasicConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public void addArtifact(Artifact artifact) {

    }

    @Override
    public void updateArtifact(Artifact artifact) {

    }

    @Override
    public void deleteArtifact(Artifact artifact) {

    }

    @Override
    public List<Artifact> getAllArtifacts() {
        return null;
    }

    @Override
    public Artifact getArtifact(int id) {
        return null;
    }

    @Override
    public void buyArtifact(Student student, int artifactId) {

    }

    @Override
    public void useArtifact(Student student, int artifactId) {

    }
}
