package com.oop2.models;

import java.util.*;
import com.oop2.util.Config;

public class FestivalModel extends Model
{
    private String time;
    private String location;
    private String historicalFigureLinked;
    private String firstTimeHolding;
    private String locationCode;

    public FestivalModel(String name, String time, String location,
                         String historicalFigureLinked, String firstTimeHolding, List<String> description, String locationCode)
    {
        super(name, description);
        setTime(time);
        setLocation(location);
        setHistoricalFigureLinked(historicalFigureLinked);
        setFirstTimeHolding(firstTimeHolding);
        setLocationCode(locationCode);
    }

    public void setFirstTimeHolding(String firstTimeHolding)
    {
        this.firstTimeHolding = firstTimeHolding.equals("") ? Config.nullRepresentation : firstTimeHolding;
    }

    public void setHistoricalFigureLinked(String historicalFigureLinked)
    {
        this.historicalFigureLinked = historicalFigureLinked.equals("") ? Config.nullRepresentation : historicalFigureLinked;
    }

    public void setLocation(String location)
    {
        this.location = location.equals("") ? Config.nullRepresentation : location;
    }

    public void setTime(String time)
    {
        this.time = time.equals("") ? Config.nullRepresentation : time;
    }

    public void setLocationCode(String locationCode)
    {
        this.locationCode = locationCode;
    }


    @Override
    public String toString()
    {
        return  "{ \t\"Id\": \"" + this.id + "\", \n\t"
                + "\"Tên\": \"" + this.name + "\",\n\t"
                + "\"Thời Gian\": \"" + this.time + "\",\n\t"
                + "\"Địa điểm\": \"" + this.location + "\",\n\t"
                + "\"Nhân Vật Lịch Sử Liên Kết\": \"" + this.historicalFigureLinked + "\",\n\t"
                + "\"Lần Đầu Tổ Chức\": \"" + this.firstTimeHolding + "\",\n\t"
                + "\"Thông Tin Khác\": \"" + this.description.toString() + "\",\n\t"
                + "\"Địa điểm code\": \"" + this.locationCode + "\" }\n";
    }
}
