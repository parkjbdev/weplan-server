package softwar7.global.constant;

public enum LoginConstant {

    MEMBER_SESSION("MemberSession"),
    ACCESS_TOKEN("AccessToken"),
    REFRESH_TOKEN("RefreshToken");

    public final String value;

    LoginConstant(final String value) {
        this.value = value;
    }
}
