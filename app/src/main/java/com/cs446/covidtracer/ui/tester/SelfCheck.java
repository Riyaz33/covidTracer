package com.cs446.covidtracer.ui.tester;

// Model for Self Test module
public class SelfCheck {
    private boolean hasSevereSymptons; // severe sickness
    private boolean hasCovidSymptoms; // has covid symptoms
    private int age;
    private boolean isInAtRiskGroup;
    private boolean testCompleted;
    private boolean needsTest;

    public SelfCheck(){
        this.needsTest = false;
        this.testCompleted = false;
    }

    public boolean isHasSevereSymptons() {
        return hasSevereSymptons;
    }

    public void setHasSevereSymptons(boolean hasSevereSymptons) {
        this.hasSevereSymptons = hasSevereSymptons;
    }

    public boolean isHasCovidSymptoms() {
        return hasCovidSymptoms;
    }

    public void setHasCovidSymptoms(boolean hasCovidSymptoms) {
        this.hasCovidSymptoms = hasCovidSymptoms;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isInAtRiskGroup() {
        return isInAtRiskGroup;
    }

    public void setInAtRiskGroup(boolean inAtRiskGroup) {
        isInAtRiskGroup = inAtRiskGroup;
    }

    public boolean isTestCompleted() {
        return testCompleted;
    }

    public void setTestCompleted(boolean testCompleted) {
        this.testCompleted = testCompleted;
    }

    public boolean isNeedsTest() {
        return needsTest;
    }

    public void setNeedsTest(boolean needsTest) {
        this.needsTest = needsTest;
    }


}
