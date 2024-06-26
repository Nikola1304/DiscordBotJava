package net.cowtopia.dscjava.libs;

import java.util.concurrent.TimeUnit;

// ovde mi ne treba fabric pattern, samo cu sve resiti unutar klase
// ne znam ni sto pravim klasu uopste

public class MuteDuration {

    private int _duration;
    private TimeUnit _duration_unit;

    public MuteDuration(int duration, int duration_unit) {

        if(duration < 0)
            duration *= -1;

        if(duration_unit == 1) {
            _duration_unit = TimeUnit.SECONDS;
            setDuration(duration, 2419200);
        }
        else if(duration_unit == 3) {
            _duration_unit = TimeUnit.HOURS;
            setDuration(duration, 672);
        }
        else if(duration_unit == 4) {
            _duration_unit = TimeUnit.DAYS;
            setDuration(duration, 28);
        }
        else {
            _duration_unit = TimeUnit.MINUTES;
            setDuration(duration, 40320);
        }
    }

    public int getDuration() {
        return _duration;
    }

    public TimeUnit getDurationUnit() {
        return _duration_unit;
    }

    private void setDuration(int duration, int limit) {
        if(duration > limit) {
            _duration = limit;
        } else _duration = duration;
    }


}
