package io.github.niestrat99.chatsplus.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ErrorHandler {

    private static List<String> errorMessages = new ArrayList<>(Arrays.asList(
            "AAAAAAAAAA!!!",
            "Oh fiddlesticks, what now!?",
            "Hello darkness, my old friend!",
            "I see errors of red... Several problems too...",
            "Oh, good heavens!",
            "*chuckles* I'm in danger!",
            "Oh no! Cringe!",
            "Well, if it isn't the consequences of my actions!",
            "Womp, womp!",
            "I say gentlemen, I do believe we're in quite a spot of bother!",
            "What if I just turn around and see BAT-"
    ));

    public static String errorSplash() {
        Random rnd = new Random();
        return errorMessages.get(rnd.nextInt(errorMessages.size()));
    }
}
