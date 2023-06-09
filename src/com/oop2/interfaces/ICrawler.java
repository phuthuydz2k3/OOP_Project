package com.oop2.interfaces;

import java.util.List;

import com.oop2.models.Model;

public interface ICrawler {
    public List<Model> crawlPages(String page);
    public void writeModel(String fileName, List<Model> models);
}
