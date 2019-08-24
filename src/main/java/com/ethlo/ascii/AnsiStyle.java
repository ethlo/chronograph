package com.ethlo.ascii;

public enum AnsiStyle
{
    BOLD("\u001B[1m");

    private String s;

    AnsiStyle(String s)
    {
        this.s = s;
    }

    public String value()
    {
        return s;
    }
}
