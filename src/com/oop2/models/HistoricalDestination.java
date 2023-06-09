package com.oop2.models;

import java.util.List;
import java.util.Set;

public class HistoricalDestination extends Model{
    private String historicalDestinationCode;
    private Set<String> historicalFiguresLinked;
    public HistoricalDestination(String name, List<String> description, String historicalDestinationCode, Set<String> historicalFiguresLinked)
    {
        super(name, description);
        setHistoricalDestinationCode(historicalDestinationCode);
        setHistoricalFiguresLinked(historicalFiguresLinked);
    }

    public void setHistoricalFiguresLinked(Set<String> historicalFiguresLinked)
    {
        this.historicalFiguresLinked = historicalFiguresLinked;
    }

    public void setHistoricalDestinationCode(String historicalDestinationCode)
    {
        this.historicalDestinationCode = historicalDestinationCode;
    }

    @Override
    public String toString() {
        return  "\n{ \"Id\":\"" + this.id + "\", "
                + "\n\"Địa danh\":\"" + this.name + "\", "
                + "\n\"Code\":\"" + this.historicalDestinationCode + "\", "
                + "\n\"Miêu tả\":\"" + this.description + "\", "
                + "\n\"Nhân vật liên quan code\":\"" + this.historicalFiguresLinked + "\" }" + "\n";
    }
}
