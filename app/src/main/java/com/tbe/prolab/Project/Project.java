package com.tbe.prolab.Project;

/**
 * Created by badetitou on 13/03/15.
 */
public class Project {

    private String name;
    private String punchline;
    private String description;

    public Project(String name, String punchline, String description) {
        this.name = name;
        this.punchline = punchline;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public String getPunchline() {
        return punchline;
    }
}
