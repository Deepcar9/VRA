// Copyright � 2015 Darren McNeely. All Rights Reserved.

package com.example.darren.new_design;

public class Exercise_description {


    String Name;
    String Description;
    String Intro_Video;


    public Exercise_description(String name, String Description, String Intro_Video) {

        this.Name = name;
        this.Description = Description;
        this.Intro_Video = Intro_Video;
    }


    public String getName() {
        return Name;
    }

    public String getDescription() {
        return Description;
    }

    public String getIntro_Video() {
        return Intro_Video;
    }
}
