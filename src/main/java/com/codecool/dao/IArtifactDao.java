package com.codecool.dao;

import com.codecool.model.Artifact;
import com.codecool.model.Quest;
import com.codecool.model.Student;

import java.util.List;

public interface IArtifactDao {
    public void addArtifact(Artifact artifact);
    public void updateArtifact(Artifact artifact);
    public void deleteArtifact(Artifact artifact);
    public List<Artifact> getAllArtifacts();
    public Artifact getArtifact(int id);
    Artifact getArtifact(String artifactName);
    public void buyArtifact(Student student, int artifactId);
    public void useArtifact(Student student, int artifactId);
    List<Artifact> getAllArtifactsByStudentId(int studentId, boolean state);
}
