package com.ethlo.time;

import java.util.Optional;

import com.ethlo.Beta;
import com.ethlo.ascii.TableTheme;

@Beta
public class ChronographContext
{
    private static final ThreadLocal<Chronograph> instances = ThreadLocal.withInitial(Chronograph::create);
    private static OutputConfig outputConfig;

    private static TableTheme theme;

    public static Chronograph get()
    {
        return instances.get();
    }

    public static void remove()
    {
        instances.remove();
    }

    public static String prettyPrint()
    {
        return get().prettyPrint(getConfig(), getTheme());
    }

    private static OutputConfig getConfig()
    {
        return Optional.ofNullable(outputConfig).orElse(OutputConfig.DEFAULT);
    }

    public static void setOutputConfig(final OutputConfig outputConfig)
    {
        ChronographContext.outputConfig = outputConfig;
    }

    public static TableTheme getTheme()
    {
        return Optional.ofNullable(theme).orElse(TableTheme.DEFAULT);
    }

    public static void setTheme(final TableTheme theme)
    {
        ChronographContext.theme = theme;
    }
}
