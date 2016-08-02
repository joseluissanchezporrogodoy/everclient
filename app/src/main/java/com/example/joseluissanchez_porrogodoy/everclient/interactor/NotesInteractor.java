package com.example.joseluissanchez_porrogodoy.everclient.interactor;

import com.evernote.edam.notestore.NoteList;
import com.evernote.edam.type.Note;

import java.util.List;

/**
 * Created by joseluissanchez-porrogodoy on 2/8/16.
 */
public interface NotesInteractor {
    interface OnFinishedListener {
        void onNotesListLoadFinished(NoteList noteList);
        void onNoteDetailFinished(String title, String content);

    }
    interface OnAddFinishedListener {

        void onNoteAddlFinished();
    }

    void findItems(OnFinishedListener listener,int type);
    void getDetail(OnFinishedListener listener, Note note);
    void addNote(OnAddFinishedListener listener,String title, String content);
}
