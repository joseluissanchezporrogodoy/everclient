package com.example.joseluissanchez_porrogodoy.everclient.view;

import com.evernote.edam.notestore.NoteList;

/**
 * Created by joseluissanchez-porrogodoy on 2/8/16.
 */
public interface MainView {
    //void findNotes(int sortMode);
    void setItems(NoteList noteList);
    void goToDetail(String title, String content);

}
