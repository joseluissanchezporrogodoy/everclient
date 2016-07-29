package com.example.joseluissanchez_porrogodoy.everclient.util;

import android.app.Activity;


import com.evernote.client.android.EvernoteSession;

import com.example.joseluissanchez_porrogodoy.everclient.activity.MainActivity;

/**
 * @author rwondratschek
 */
public final class Util {

    private Util() {
        // no op
    }

    public static void logout(Activity activity) {
        EvernoteSession.getInstance().logOut();
        MainActivity.launch(activity);
        activity.finish();
    }
}
