package com.e2.wfm.rule.common.time;

import java.util.EnumSet;
import java.util.Set;

public enum DayPattern {
    NONE,
    SAT {
        public DayOfWeek getDayOfWeek() {
            return DayOfWeek.SAT;
        }
    },
    FRI {
        public DayOfWeek getDayOfWeek() {
            return DayOfWeek.FRI;
        }
    },
    FRI_SAT,
    THU {
        public DayOfWeek getDayOfWeek() {
            return DayOfWeek.THU;
        }
    },
    THU_SAT,
    THU_FRI,
    THU_FRI_SAT,
    WED {
        public DayOfWeek getDayOfWeek() {
            return DayOfWeek.WED;
        }
    },
    WED_SAT,
    WED_FRI,
    WED_FRI_SAT,
    WED_THU,
    WED_THU_SAT,
    WED_THU_FRI,
    WED_THU_FRI_SAT,
    TUE {
        public DayOfWeek getDayOfWeek() {
            return DayOfWeek.TUE;
        }
    },
    TUE_SAT,
    TUE_FRI,
    TUE_FRI_SAT,
    TUE_THU,
    TUE_THU_SAT,
    TUE_THU_FRI,
    TUE_THU_FRI_SAT,
    TUE_WED,
    TUE_WED_SAT,
    TUE_WED_FRI,
    TUE_WED_FRI_SAT,
    TUE_WED_THU,
    TUE_WED_THU_SAT,
    TUE_WED_THU_FRI,
    TUE_WED_THU_FRI_SAT,
    MON {
        public DayOfWeek getDayOfWeek() {
            return DayOfWeek.MON;
        }
    },
    MON_SAT,
    MON_FRI,
    MON_FRI_SAT,
    MON_THU,
    MON_THU_SAT,
    MON_THU_FRI,
    MON_THU_FRI_SAT,
    MON_WED,
    MON_WED_SAT,
    MON_WED_FRI,
    MON_WED_FRI_SAT,
    MON_WED_THU,
    MON_WED_THU_SAT,
    MON_WED_THU_FRI,
    MON_WED_THU_FRI_SAT,
    MON_TUE,
    MON_TUE_SAT,
    MON_TUE_FRI,
    MON_TUE_FRI_SAT,
    MON_TUE_THU,
    MON_TUE_THU_SAT,
    MON_TUE_THU_FRI,
    MON_TUE_THU_FRI_SAT,
    MON_TUE_WED,
    MON_TUE_WED_SAT,
    MON_TUE_WED_FRI,
    MON_TUE_WED_FRI_SAT,
    MON_TUE_WED_THU,
    MON_TUE_WED_THU_SAT,
    MON_TUE_WED_THU_FRI,
    MON_TUE_WED_THU_FRI_SAT,
    SUN() {
        public DayOfWeek getDayOfWeek() {
            return DayOfWeek.SUN;
        }
    },
    SUN_SAT,
    SUN_FRI,
    SUN_FRI_SAT,
    SUN_THU,
    SUN_THU_SAT,
    SUN_THU_FRI,
    SUN_THU_FRI_SAT,
    SUN_WED,
    SUN_WED_SAT,
    SUN_WED_FRI,
    SUN_WED_FRI_SAT,
    SUN_WED_THU,
    SUN_WED_THU_SAT,
    SUN_WED_THU_FRI,
    SUN_WED_THU_FRI_SAT,
    SUN_TUE,
    SUN_TUE_SAT,
    SUN_TUE_FRI,
    SUN_TUE_FRI_SAT,
    SUN_TUE_THU,
    SUN_TUE_THU_SAT,
    SUN_TUE_THU_FRI,
    SUN_TUE_THU_FRI_SAT,
    SUN_TUE_WED,
    SUN_TUE_WED_SAT,
    SUN_TUE_WED_FRI,
    SUN_TUE_WED_FRI_SAT,
    SUN_TUE_WED_THU,
    SUN_TUE_WED_THU_SAT,
    SUN_TUE_WED_THU_FRI,
    SUN_TUE_WED_THU_FRI_SAT,
    SUN_MON,
    SUN_MON_SAT,
    SUN_MON_FRI,
    SUN_MON_FRI_SAT,
    SUN_MON_THU,
    SUN_MON_THU_SAT,
    SUN_MON_THU_FRI,
    SUN_MON_THU_FRI_SAT,
    SUN_MON_WED,
    SUN_MON_WED_SAT,
    SUN_MON_WED_FRI,
    SUN_MON_WED_FRI_SAT,
    SUN_MON_WED_THU,
    SUN_MON_WED_THU_SAT,
    SUN_MON_WED_THU_FRI,
    SUN_MON_WED_THU_FRI_SAT,
    SUN_MON_TUE,
    SUN_MON_TUE_SAT,
    SUN_MON_TUE_FRI,
    SUN_MON_TUE_FRI_SAT,
    SUN_MON_TUE_THU,
    SUN_MON_TUE_THU_SAT,
    SUN_MON_TUE_THU_FRI,
    SUN_MON_TUE_THU_FRI_SAT,
    SUN_MON_TUE_WED,
    SUN_MON_TUE_WED_SAT,
    SUN_MON_TUE_WED_FRI,
    SUN_MON_TUE_WED_FRI_SAT,
    SUN_MON_TUE_WED_THU,
    SUN_MON_TUE_WED_THU_SAT,
    SUN_MON_TUE_WED_THU_FRI,
    SUN_MON_TUE_WED_THU_FRI_SAT;

    public static final DayPattern ALL = SUN_MON_TUE_WED_THU_FRI_SAT;
    private final short numDays;


    DayPattern() {
        int dayCount = (this.ordinal() & 85) + (this.ordinal() >> 1 & 85);
        dayCount = (dayCount & 51) + (dayCount >> 2 & 51);
        dayCount = (dayCount & 15) + (dayCount >> 4 & 15);
        this.numDays = (short)dayCount;
    }

    public short getNumDays() {
        return this.numDays;
    }

    public DayOfWeek getDayOfWeek() {
        Set<DayOfWeek> daysOfWeek = this.getDaysOfWeek();
        return daysOfWeek != null && daysOfWeek.size() == 1 ? daysOfWeek.iterator().next() : null;
    }

    public Set<DayOfWeek> getDaysOfWeek() {
        EnumSet<DayOfWeek> result = EnumSet.noneOf(DayOfWeek.class);
        DayOfWeek[] var2 = DayOfWeek.values();
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            DayOfWeek dayOfWeek = var2[var4];
            if (this.hasDayOfWeek(dayOfWeek)) {
                result.add(dayOfWeek);
            }
        }

        return result;
    }

    public Set<java.time.DayOfWeek> getJavaDaysOfWeek() {
        EnumSet<java.time.DayOfWeek> result = EnumSet.noneOf(java.time.DayOfWeek.class);
        java.time.DayOfWeek[] var2 = java.time.DayOfWeek.values();
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            java.time.DayOfWeek dayOfWeek = var2[var4];
            if (this.hasDayOfWeek(dayOfWeek)) {
                result.add(dayOfWeek);
            }
        }

        return result;
    }

    public DayPattern addPattern(DayPattern... pa) {
        int ordinal = this.ordinal();
        DayPattern[] var3 = pa;
        int var4 = pa.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            DayPattern p = var3[var5];
            ordinal |= p.ordinal();
        }

        return values()[ordinal];
    }

    public DayPattern removePattern(DayPattern... pa) {
        int ordinal = this.ordinal();
        DayPattern[] var3 = pa;
        int var4 = pa.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            DayPattern p = var3[var5];
            ordinal &= ~p.ordinal();
        }

        return values()[ordinal];
    }

    public static DayPattern patternFor(java.time.DayOfWeek... da) {
        int ordinal = 0;
        java.time.DayOfWeek[] var2 = da;
        int var3 = da.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            java.time.DayOfWeek d = var2[var4];
            ordinal |= 1 << 6 - javaDayOfWeek2thisDayOfWeekOrdinal(d);
        }

        return values()[ordinal];
    }

    public static DayPattern patternFor(DayOfWeek... da) {
        int ordinal = 0;
        DayOfWeek[] var2 = da;
        int var3 = da.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            DayOfWeek d = var2[var4];
            ordinal |= 1 << 6 - d.getLiteralValue();
        }

        return values()[ordinal];
    }

    public DayPattern addDay(java.time.DayOfWeek... da) {
        return this.addPattern(patternFor(da));
    }

    public boolean hasDayOfWeek(java.time.DayOfWeek d) {
        return (this.ordinal() & 1 << 6 - javaDayOfWeek2thisDayOfWeekOrdinal(d)) != 0;
    }

    private static int javaDayOfWeek2thisDayOfWeekOrdinal(java.time.DayOfWeek d) {
        return d == java.time.DayOfWeek.SUNDAY ? 0 : d.getValue();
    }

    public boolean hasDayOfWeek(DayOfWeek d) {
        return (this.ordinal() & 1 << 6 - d.getLiteralValue()) != 0;
    }

    public DayPattern commonDays(DayPattern p) {
        return values()[this.ordinal() & p.ordinal()];
    }
}
