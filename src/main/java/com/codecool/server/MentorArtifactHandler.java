package com.codecool.server;

import com.codecool.dao.IArtifactDao;
import com.codecool.dao.IMentorDao;
import com.codecool.dao.ISessionDao;
import com.codecool.server.helper.CommonHelper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class MentorArtifactHandler implements HttpHandler {
    private IArtifactDao artifactDao;
    private ISessionDao sessionDao;
    private CommonHelper commonHelper;
    private IMentorDao mentorDao;

    public MentorArtifactHandler(IArtifactDao artifactDao, ISessionDao sessionDao, CommonHelper commonHelper, IMentorDao mentorDao) {
        this.artifactDao = artifactDao;
        this.sessionDao = sessionDao;
        this.commonHelper = commonHelper;
        this.mentorDao = mentorDao;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

    }
}
