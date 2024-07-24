package org.doug.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Paper {
    @JsonProperty
    private String doi;
    @JsonProperty
    private String title;
    @JsonProperty
    private String authors;
    @JsonProperty
    private String category;
    @JsonProperty("abstract")
    private String abstractText; // Renamed from abstract to abstractText
    @JsonProperty("author_corresponding")
    private String authorCorresponding;
    @JsonProperty("author_corresponding_institution")
    private String authorCorrespondingInstitution;
    @JsonProperty
    private String date;
    @JsonProperty
    private String version;
    @JsonProperty
    private String type;
    @JsonProperty
    private String license;
    @JsonProperty
    private String jatsxml;
    @JsonProperty
    private String published;
    @JsonProperty
    private String server;

    public Paper() {
    }

    // Getters and setters

    public String getDoi() {
        return doi;
    }

    public void setDoi(String doi) {
        this.doi = doi;
    }

    public String getTitle() {
        return title;
    }

    //    getter and setter for authors
    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    //    Category
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbstractText() {
        return abstractText;
    }

    public void setAbstractText(String abstractText) {
        this.abstractText = abstractText;
    }


    public String getAuthorCorresponding() {
        return authorCorresponding;
    }

    public void setAuthorCorresponding(String authorCorresponding) {
        this.authorCorresponding = authorCorresponding;
    }

    public String getAuthorCorrespondingInstitution() {
        return authorCorrespondingInstitution;
    }

    public void setAuthorCorrespondingInstitution(String authorCorrespondingInstitution) {
        this.authorCorrespondingInstitution = authorCorrespondingInstitution;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getJatsxml() {
        return jatsxml;
    }

    public void setJatsxml(String jatsxml) {
        this.jatsxml = jatsxml;
    }

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            return "Error serializing object: " + e.getMessage();

        }
    }
}