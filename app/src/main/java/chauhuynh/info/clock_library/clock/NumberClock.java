package chauhuynh.info.clock_library.clock;

/**
 * Created by appro on 12/03/2018.
 */

public class NumberClock {
    public static final int FROM = 1;
    public static final int TO = 12;
    public static final int EVEN = 1;
    public static final int ODD = 2;

    private String number;
    private boolean enable;

    public void setNumber(String number) {
        this.number = number;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getNumber() {
        return number;
    }

    public boolean isEnable() {
        return enable;
    }
}
