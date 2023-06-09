package com.oop2.models;

import java.util.*;
import com.oop2.util.Config;

public abstract class Model
{
    protected int id;
    protected String name;
    protected List<String> description;

    public Model(String ten, List<String> description)
    {
        setName(ten);
        setDescription(description);
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setName(String name)
    {
        this.name = name.equals("") ? Config.nullRepresentation : name;
    }

    public void setDescription(List<String> description)
    {
        if (description == null)
        {
            description = new ArrayList<>();
            description.add(Config.nullRepresentation);
        }
        this.description = description;
    }
}

