package com.codecool.dao;

import com.codecool.model.Artifact;
import com.codecool.model.Quest;
import com.codecool.model.Student;

import java.util.List;

public interface IArtifactDao {
    public void addArtifact(Artifact artifact);
    public void updateArtifact(Artifact artifact);
    public void deleteArtifact(int artifactId);
    public List<Artifact> getAllArtifacts();
    public Artifact getArtifact(int id);
    Artifact getArtifact(String artifactName);
    public void buyArtifact(int studentId, int artifactId);
    public void useArtifact(Student student, int artifactId);
    List<Artifact> getAllArtifactsByStudentId(int studentId, boolean state);
}
