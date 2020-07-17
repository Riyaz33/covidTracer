package com.cs446.covidtracer.ui.updates;

/*
 * Summary class holds the COVID related information for a province
 */
public class Summary {
    private String name;
    private String abbreviation;
    private int tests;
    private int recovered;
    private int activeCases;
    private int deaths;
    private int total;

    public Summary(String name, String abbreviation, int tests, int recovered, int activeCases, int deaths, int total) {
        this.name = name; // Name of the province
        this.abbreviation = abbreviation; // Abbreviation of the province
        this.tests = tests;
        this.recovered = recovered;
        this.activeCases = activeCases;
        this.deaths = deaths;
        this.total = total;
    }

    public String getName() {
        return name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public int getTests() {
        return tests;
    }

    public int getRecovered() {
        return recovered;
    }

    public int getActiveCases() {
        return activeCases;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getTotal() {
        return total;
    }
}
