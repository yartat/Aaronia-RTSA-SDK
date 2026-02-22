package com.aaronia.rtsa;

/**
 * Information about a configuration item in the device config tree.
 */
public class ConfigInfo {

    private final String name;
    private final String title;
    private final ConfigType type;
    private final double minValue;
    private final double maxValue;
    private final double stepValue;
    private final String unit;
    private final String options;
    private final long disabledOptions;

    public ConfigInfo(String name, String title, ConfigType type,
                      double minValue, double maxValue, double stepValue,
                      String unit, String options, long disabledOptions) {
        this.name = name;
        this.title = title;
        this.type = type;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.stepValue = stepValue;
        this.unit = unit;
        this.options = options;
        this.disabledOptions = disabledOptions;
    }

    public String getName()          { return name; }
    public String getTitle()         { return title; }
    public ConfigType getType()      { return type; }
    public double getMinValue()      { return minValue; }
    public double getMaxValue()      { return maxValue; }
    public double getStepValue()     { return stepValue; }
    public String getUnit()          { return unit; }
    public String getOptions()       { return options; }
    public long getDisabledOptions() { return disabledOptions; }

    @Override
    public String toString() {
        return "ConfigInfo{name=" + name + ", title=" + title +
               ", type=" + type + ", unit=" + unit +
               ", options=" + options + "}";
    }
}
