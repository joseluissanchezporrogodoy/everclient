package com.example.joseluissanchez_porrogodoy.everclient.aplication;

import android.app.Application;

import com.evernote.client.android.EvernoteSession;

/**
 * Created by joseluissanchez-porrogodoy on 28/7/16.
 */
public class Everclient extends Application{

    ///APIKEYS
    private static final String CONSUMER_KEY = "joselspg-1366";
    private static final String CONSUMER_SECRET = "801288a3ff2aa2ae";
    private static final EvernoteSession.EvernoteService EVERNOTE_SERVICE = EvernoteSession.EvernoteService.SANDBOX;
    /*
     * Set this to true if you want to allow linked notebooks for accounts that
     * can only access a single notebook.
     */
    private static final boolean SUPPORT_APP_LINKED_NOTEBOOKS = true;

    @Override
    public void onCreate() {
        super.onCreate();

        String consumerKey;
        consumerKey = CONSUMER_KEY;
        String consumerSecret;
        consumerSecret = CONSUMER_SECRET;
        //Set up the Evernote singleton session, use EvernoteSession.getInstance() later
        new EvernoteSession.Builder(this)
                .setEvernoteService(EVERNOTE_SERVICE)
                .setSupportAppLinkedNotebooks(SUPPORT_APP_LINKED_NOTEBOOKS)
                .setForceAuthenticationInThirdPartyApp(true)
                .build(consumerKey, consumerSecret)
                .asSingleton();

        registerActivityLifecycleCallbacks(new LoginChecker());
    }
}
