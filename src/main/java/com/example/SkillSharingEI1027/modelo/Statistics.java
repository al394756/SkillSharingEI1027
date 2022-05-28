package com.example.SkillSharingEI1027.modelo;

public class Statistics {
    private String key;
    private float count;

    public Statistics(String key, float count) {
        this.key = key;
        this.count = count;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public float getCount() {
        return count;
    }

    public void setCount(float count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "Statistics{" +
                "key='" + key + '\'' +
                ", count=" + count +
                '}';
    }

    public String twoDecimals(){
        return String.format("%.2f", count);
    }
}
