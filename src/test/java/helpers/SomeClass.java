package helpers;

/*
 * Created by maxim on 19.04.14.
 */

import resources.Resource;

public class SomeClass implements Resource{
    String first;
    int second;


    SomeClass(){
        this.first = "qq";
        this.second = 0;
    }

    public String getFirst() {
        return first;
    }

    public int getSecond() {
        return second;
    }
}