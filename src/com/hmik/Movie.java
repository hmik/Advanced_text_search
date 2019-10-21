package com.hmik;

public class Movie {
    private String name;
    private String category;
    private String summary;
    private String description;
    private String rank;

    public Movie() {
    }

    public String toString(){
        return String.format("%s - %s | %s <br> %s <br> %s", this.getName(), this.getCategory(), this.getRank(), this.getSummary(), this.getDescription());
    }

    private String getRank() {
        return rank;
    }

    String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    Movie(String name, String category, String summary, String description, String rank){
        this.name=name;
        this.category=category;
        this.summary=summary;
        this.description=description;
        this.rank = rank;
    }



}
