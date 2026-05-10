package server.TripToN.global.util;

public final class BcryptPasswordEncoder {

    private static final org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder ENCODER =
            new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();

    private BcryptPasswordEncoder() {
    }

    public static String encode(String rawPassword) {
        return ENCODER.encode(rawPassword);
    }

    public static boolean matches(String rawPassword, String encodedPassword) {
        return ENCODER.matches(rawPassword, encodedPassword);
    }
}
