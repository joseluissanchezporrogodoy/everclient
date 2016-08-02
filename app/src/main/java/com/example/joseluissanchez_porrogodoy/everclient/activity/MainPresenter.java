package com.example.joseluissanchez_porrogodoy.everclient.activity;

import com.evernote.edam.type.Note;

/**
 * Created by joseluissanchez-porrogodoy on 2/8/16.
 */
public interface MainPresenter {
    void onOptionSortSelected(int sortMode);
    void onResume();
    void onDestroy();
    void onItemClicked(Note note);
    void onAddNoteResult();
}
