package com.oop2.models;

import java.util.Set;
import java.util.List;

public class HistoricalEvent extends Model {
    private String time;
    private String location;
    private String battleResult;
    private Set<String> historicalFiguresLinked;
    private Set<String> historicalDestinationsLinked;

    public HistoricalEvent(String name, List<String> description, String time, String location, String battleResult
            , Set<String> historicalFiguresLinked, Set<String> historicalDestinationsLinked) {
        super(name, description);
        setTime(time);
        setLocation(location);
        setBattleResult(battleResult);
        setHistoricalFiguresLinked(historicalFiguresLinked);
        setHistoricalDestinationsLinked(historicalDestinationsLinked);
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setBattleResult(String battleResult) {
        this.battleResult = battleResult;
    }

    public void setHistoricalFiguresLinked(Set<String> historicalFiguresLinked) {
        this.historicalFiguresLinked = historicalFiguresLinked;
    }

    public void setHistoricalDestinationsLinked(Set<String> historicalDestinationsLinked) {
        this.historicalDestinationsLinked = historicalDestinationsLinked;
    }

    @Override
    public String toString() {
        return  "\n{ \"Id\":\"" + this.id + "\", "
                + "\n\"Tên\":\"" + this.name + "\", "
                + "\n\"Thời gian\":\"" + this.time + "\", "
                + "\n\"Địa điểm\":\"" + this.location + "\", "
                + "\n\"kết quả\":\"" + this.battleResult + "\", "
                + "\n\"Nhân vật liên quan code\":\"" + this.historicalFiguresLinked + "\", "
                + "\n\"Địa điểm liên quan code\":\"" + this.historicalDestinationsLinked + "\" }" + "\n";
    }
}
