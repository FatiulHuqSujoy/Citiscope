package com.example.sujoy.citi1.technical_classes;

/**
 * Created by Sujoy on 4/20/2017.
 */

public class OptionCode {
    String filter, option, code;

    public OptionCode(String fil, String op, String c){
        filter = fil;
        option = op;
        code = c;
    }

    public Boolean match(String fil, String op){
        if(filter.equals(fil) && option.equals(op))
            return true;
        return false;
    }

    public String getCode() {
        return code;
    }
}









