package com.cs446.covidtracer.ui.tester;

// Model for Self Test module
// This holds the information required for the self-test module
// This model is updated by the controller (SelfCheckFragment.java) and displayed by the view (self_check_fragment.xml)
public class SelfCheck {
    private boolean hasSevereSymptons; // severe sickness
    private boolean hasCovidSymptoms; // has covid symptoms
    private int age;

    public boolean isAtRiskAge() {
        return atRiskAge;
    }

    public void setAtRiskAge(boolean atRiskAge) {
        this.atRiskAge = atRiskAge;
    }

    private boolean atRiskAge;
    private boolean isInAtRiskGroup;
    private boolean testCompleted;
    private boolean needsTest;

    public SelfCheck(){
        this.needsTest = false;
        this.testCompleted = false;
        this.age = -1;
    }

    public String selfTestReport(){
        String report = "COVID-19 Self-Test Result\n\n";

        if(hasSevereSymptons) {
            report = report + "\nYou responded YES to having severe symptoms (tough time breathing, chest pain, confusion, or losing consciousness)." +
                    " Please visit a healthcare provider ASAP. \n\n";
            return report;
        } else {
            report  = report + "\nYou responded NO to having severe symptoms (tough time breathing, chest pain, confusion, or losing consciousness).\n\n";
        }

        if(hasCovidSymptoms) {
            report = report + "\nYou responded YES to having known COVID symptoms (cough, pink eye, fever, chills, shortness of breath, sore throat)." +
                    "\nWe recommend you to get an official COVID-19 test. Assessment centers can be found here: https://covid-19.ontario.ca/assessment-centre-locations/ \n";
            return report;
        } else {
            report  = report + "\nYou responded NO to having known COVID symptoms (cough, pink eye, fever, chills, shortness of breath, sore throat).\n\n";
        }

        if(age > 0) {
            report = report + "\nYou entered age: " + age + ".\n\n";
            if(atRiskAge){
                report = report + "\nYour age puts you at high-risk. Please self-isolate and practice social distancing as much as possible.\n\n";
            }
        }

        if(isInAtRiskGroup){
            report = report + "\nYou responded YES to being in an at-risk group." +
                    " We recommend you to get an official COVID-19 test. Assessment centers can be found here: https://covid-19.ontario.ca/assessment-centre-locations/." +
                    "Please practice social distancing as much as possible.\n ";
            return report;
        } else {
            report  = report + " \nYou responded NO to being in an at-risk group.\n";
        }

        report = report + " \nYou do not need to take further action. Please continue to monitor your symptoms and follow public health guidelines.";


        return report;

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
