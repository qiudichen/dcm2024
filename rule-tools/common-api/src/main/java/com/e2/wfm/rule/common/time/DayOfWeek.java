package com.e2.wfm.rule.common.time;

import java.time.LocalDate;

public enum DayOfWeek {
    SUN("Sunday", 1, java.time.DayOfWeek.SUNDAY, DayPattern.SUN),
    MON("Monday", 2, java.time.DayOfWeek.MONDAY, DayPattern.MON),
    TUE("Tuesday", 3, java.time.DayOfWeek.TUESDAY, DayPattern.TUE),
    WED("Wednesday", 4, java.time.DayOfWeek.WEDNESDAY, DayPattern.WED),
    THU("Thursday", 5, java.time.DayOfWeek.THURSDAY, DayPattern.THU),
    FRI("Friday", 6, java.time.DayOfWeek.FRIDAY, DayPattern.FRI),
    SAT("Saturday", 7, java.time.DayOfWeek.SATURDAY, DayPattern.SAT);

    private final String code;
    private final String shortCode;
    private final java.time.DayOfWeek javaDayOfWeek;
    private final DayPattern dayPattern;

    public static DayOfWeek valueOf(LocalDate date) {
        return valueOf(date.getDayOfWeek());
    }

    public static DayOfWeek valueOf(java.time.DayOfWeek javaDayOfWeek) {
        DayOfWeek[] var1 = values();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            DayOfWeek dow = var1[var3];
            if (dow.javaDayOfWeek == javaDayOfWeek) {
                return dow;
            }
        }

        throw new IllegalArgumentException("The javaDayOfWeek (" + javaDayOfWeek + ") is not supported.");
    }

    DayOfWeek(String code, int javaCalendarValue, java.time.DayOfWeek javaDayOfWeek, DayPattern dayPattern) {
        this.code = code;
        this.shortCode = code.substring(0, 3).toUpperCase();
        this.javaDayOfWeek = javaDayOfWeek;
        this.dayPattern = dayPattern;
    }

    public String getCode() {
        return this.code;
    }

    public int getLiteralValue() {
        return this.ordinal();
    }

    public DayPattern getDayPattern() {
        return this.dayPattern;
    }

    public java.time.DayOfWeek getJavaDayOfWeek() {
        return this.javaDayOfWeek;
    }


    public DayOfWeek determineNextDayOfWeek() {
        switch (this) {
            case MON:
                return TUE;
            case TUE:
                return WED;
            case WED:
                return THU;
            case THU:
                return FRI;
            case FRI:
                return SAT;
            case SAT:
                return SUN;
            case SUN:
                return MON;
            default:
                throw new IllegalArgumentException("The dayOfWeek (" + this + ") is not supported.");
        }
    }

    @Override
    public String toString() {
        return this.shortCode;
    }
}
