package com.mekcone.excrud.model.project.components;

public class ApiDocument {
    private String title;
    private String description;
    private String createKeyword;
    private String retrieveKeyword;
    private String retrieveListKeyword;
    private String updateKeyword;
    private String deleteKeyword;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreateKeyword() {
        return createKeyword;
    }

    public void setCreateKeyword(String createKeyword) {
        this.createKeyword = createKeyword;
    }

    public String getRetrieveKeyword() {
        return retrieveKeyword;
    }

    public void setRetrieveKeyword(String retrieveKeyword) {
        this.retrieveKeyword = retrieveKeyword;
    }

    public String getRetrieveListKeyword() {
        return retrieveListKeyword;
    }

    public void setRetrieveListKeyword(String retrieveListKeyword) {
        this.retrieveListKeyword = retrieveListKeyword;
    }

    public String getUpdateKeyword() {
        return updateKeyword;
    }

    public void setUpdateKeyword(String updateKeyword) {
        this.updateKeyword = updateKeyword;
    }

    public String getDeleteKeyword() {
        return deleteKeyword;
    }

    public void setDeleteKeyword(String deleteKeyword) {
        this.deleteKeyword = deleteKeyword;
    }
}
