package scalabc.connector.app;

import scalabc.connector.platform.io.SBCIO;

public interface BCTJava extends AppInterface {

    public static BCTJava getInstance() {
        return new BCTJava() {
            @Override
            public Services services() {
                return BouncyCrypToolApp.services();
            }

            @Override
            public SBCIO io() {
                return BCTJava.super.io();
            }
        };
    }

}