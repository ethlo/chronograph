package com.ethlo.ascii;

public class TableTheme
{
    public static final TableTheme NONE = new TableTheme();

    public static final TableTheme STRONG = new TableTheme()
            .setStringColor(AnsiColor.GRAY)
            .setNumericColor(AnsiColor.GREEN)
            .setVerticalSeparator("=")
            .setHorizontalSeparator("|")
            .setVerticalSpacerColor(AnsiColor.RED)
            .setHorisontalSpacerColor(AnsiColor.RED)
            .setCellBackground(AnsiBackgroundColor.BLACK);

    public static final TableTheme MINIMAL = new TableTheme()
            .setStringColor(AnsiColor.GRAY)
            .setNumericColor(AnsiColor.GREEN)
            .setVerticalSeparator(" ")
            .setHorizontalSeparator(" ")
            .setVerticalSpacerColor(AnsiColor.GRAY)
            .setHorisontalSpacerColor(AnsiColor.GRAY)
            .setCellBackground(AnsiBackgroundColor.BLACK);


    private AnsiColor stringColor = AnsiColor.NONE;
    private AnsiColor numericColor = AnsiColor.NONE;
    private AnsiColor horisontalSpacerColor = AnsiColor.NONE;
    private AnsiColor verticalSpacerColor = AnsiColor.NONE;
    private AnsiBackgroundColor cellBackground = AnsiBackgroundColor.NONE;
    private String horizontalSeparator = "-";
    private String verticalSeparator = "|";
    private String padding = " ";

    public String getPadding()
    {
        return padding;
    }

    public String getHorizontalSeparator()
    {
        return horizontalSeparator;
    }

    public AnsiColor getHorizontalSpacerColor()
    {
        return horisontalSpacerColor;
    }

    public AnsiColor getVerticalSpacerColor()
    {
        return verticalSpacerColor;
    }

    public AnsiColor getStringColor()
    {
        return stringColor;
    }

    public AnsiColor getNumericColor()
    {
        return numericColor;
    }

    public String getVerticalSeparator()
    {
        return verticalSeparator;
    }

    public AnsiBackgroundColor getCellBackground()
    {
        return cellBackground;
    }

    public TableTheme setStringColor(final AnsiColor stringColor)
    {
        this.stringColor = stringColor;
        return this;
    }

    public TableTheme setNumericColor(final AnsiColor numericColor)
    {
        this.numericColor = numericColor;
        return this;
    }

    public TableTheme setHorisontalSpacerColor(final AnsiColor horisontalSpacerColor)
    {
        this.horisontalSpacerColor = horisontalSpacerColor;
        return this;
    }

    public TableTheme setVerticalSpacerColor(final AnsiColor verticalSpacerColor)
    {
        this.verticalSpacerColor = verticalSpacerColor;
        return this;
    }

    public TableTheme setCellBackground(final AnsiBackgroundColor cellBackground)
    {
        this.cellBackground = cellBackground;
        return this;
    }

    public TableTheme setHorizontalSeparator(final String sep)
    {
        this.horizontalSeparator = sep;
        return this;
    }

    public TableTheme setVerticalSeparator(final String sep)
    {
        this.verticalSeparator = sep;
        return this;
    }

    public TableTheme setPadding(final String padding)
    {
        this.padding = padding;
        return this;
    }
}
