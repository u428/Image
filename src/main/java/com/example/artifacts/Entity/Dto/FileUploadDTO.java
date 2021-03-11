package com.example.artifacts.Entity.Dto;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public class FileUploadDTO {


    private String name;
    private double costs;
    private String description;
    private MultipartFile files;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCosts() {
        return costs;
    }

    public void setCosts(double costs) {
        this.costs = costs;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MultipartFile getFiles() {
        return files;
    }

    public void setFiles(MultipartFile files) {
        this.files = files;
    }
}
