package com.ethlo.ascii;

public enum AnsiBackgroundColor
{
    NONE(""),
    BLACK("\u001B[40m"),
    RED("\u001B[41m"),
    GREEN("\u001B[42m"),
    YELLOW("\u001B[43m"),
    BLUE("\u001B[44m"),
    PURPLE("\u001B[45m"),
    CYAN("\u001B[46m"),
    GRAY("\u001B[47m");

    private String s;

    AnsiBackgroundColor(String s)
    {
        this.s = s;
    }

    public String value()
    {
        return s;
    }
}
