package com.aaronia.rtsa.native_;

import com.sun.jna.Structure;

/**
 * JNA mapping of AARTSAAPI_ConfigInfo.
 */
@Structure.FieldOrder({
    "cbsize", "name", "title", "type",
    "minValue", "maxValue", "stepValue",
    "unit", "options", "disabledOptions"
})
public class AARTSAAPI_ConfigInfo extends Structure {
    public long cbsize;
    public char[] name = new char[80];
    public char[] title = new char[120];
    public int type;
    public double minValue;
    public double maxValue;
    public double stepValue;
    public char[] unit = new char[10];
    public char[] options = new char[1000];
    public long disabledOptions;

    public AARTSAAPI_ConfigInfo() {
        super(ALIGN_DEFAULT);
        cbsize = size();
    }

    public String getName() {
        return new String(name).trim().replace("\0", "");
    }

    public String getTitle() {
        return new String(title).trim().replace("\0", "");
    }

    public String getUnit() {
        return new String(unit).trim().replace("\0", "");
    }

    public String getOptions() {
        return new String(options).trim().replace("\0", "");
    }

    public static class ByReference extends AARTSAAPI_ConfigInfo implements Structure.ByReference {}
}
