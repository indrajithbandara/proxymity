package com.object0r.tools.proxymity.helpers;

/**
 * The type Console colors.
 */
public class ConsoleColors
{
    /**
     * The constant RESET.
     */
    public static final String RESET = "\u001B[0m";
    /**
     * The constant BLACK.
     */
    public static final String BLACK = "\u001B[30m";
    /**
     * The constant RED.
     */
    public static final String RED = "\u001B[31m";
    /**
     * The constant GREEN.
     */
    public static final String GREEN = "\u001B[32m";
    /**
     * The constant YELLOW.
     */
    public static final String YELLOW = "\u001B[33m";
    /**
     * The constant BLUE.
     */
    public static final String BLUE = "\u001B[34m";
    /**
     * The constant PURPLE.
     */
    public static final String PURPLE = "\u001B[35m";
    /**
     * The constant CYAN.
     */
    public static final String CYAN = "\u001B[36m";
    /**
     * The constant WHITE.
     */
    public static final String WHITE = "\u001B[37m";


    /**
     * Print color.
     *
     * @param text  the text
     * @param color the color
     */
    public static void printColor(String text, String color)
    {
        System.out.println(color+text+ConsoleColors.RESET);
    }

    /**
     * Print red.
     *
     * @param text the text
     */
    public static void printRed(String text)
    {
        printColor(text, ConsoleColors.RED);
    }

    /**
     * Print blue.
     *
     * @param text the text
     */
    public static void printBlue(String text)
    {
        printColor(text, ConsoleColors.BLUE);
    }

    /**
     * Print cyan.
     *
     * @param text the text
     */
    public static void printCyan(String text)
    {
        printColor(text, ConsoleColors.CYAN);
    }

    /**
     * Print green.
     *
     * @param text the text
     */
    public static void printGreen(String text)
    {
        printColor(text, ConsoleColors.GREEN);
    }
}

