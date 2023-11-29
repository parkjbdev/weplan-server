package softwar7.global.constant;

public enum TimeConstant {

    ONE_HOUR(1000 * 60 * 60),
    ONE_MONTH(1000L * 60 * 60 * 24 * 30 * 30),
    ONE_DAY(1),
    THIRTY_DAY(30);

    public final long value;

    TimeConstant(final long value) {
        this.value = value;
    }
}
