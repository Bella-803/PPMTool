package com.learnspringboot.ppmtool.exceptions;

public class ProjectIdExcepionResponse {

    private String projectIdentifier;

    public ProjectIdExcepionResponse(String projectIdentifier) {
        this.projectIdentifier = projectIdentifier;
    }

    public String getProjectIdentifier() {
        return projectIdentifier;
    }

    public void setProjectIdentifier(String projectIdentifier) {
        this.projectIdentifier = projectIdentifier;
    }
}
