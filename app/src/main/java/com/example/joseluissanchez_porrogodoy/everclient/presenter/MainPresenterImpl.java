package com.example.joseluissanchez_porrogodoy.everclient.presenter;

import com.evernote.edam.notestore.NoteList;
import com.evernote.edam.type.Note;
import com.example.joseluissanchez_porrogodoy.everclient.view.MainView;
import com.example.joseluissanchez_porrogodoy.everclient.interactor.NotesInteractor;
import com.example.joseluissanchez_porrogodoy.everclient.interactor.NotesInteractorImpl;

/**
 * Created by joseluissanchez-porrogodoy on 2/8/16.
 */
public class MainPresenterImpl implements MainPresenter, NotesInteractor.OnFinishedListener {
    public static final int SORT_EDIT = 2;
    private MainView mainView;
    private NotesInteractor findNotesInteractor;

    public MainPresenterImpl(MainView mainView) {
        this.mainView = mainView;
        findNotesInteractor = new NotesInteractorImpl();
    }

    @Override
    public void onOptionSortSelected(int sortMode) {
        findNotesInteractor.findItems(this,sortMode);
    }

    @Override
    public void onResume() {
        findNotesInteractor.findItems(this,SORT_EDIT);
    }

    @Override
    public void onDestroy() {
        mainView = null;
    }

    @Override
    public void onItemClicked(Note note) {
        findNotesInteractor.getDetail(this,note);
    }

    @Override
    public void onAddNoteResult() {
        findNotesInteractor.findItems(this,SORT_EDIT);
    }

    @Override
    public void onNotesListLoadFinished(NoteList noteList) {
        if (mainView != null) {
            mainView.setItems(noteList);

        }
    }

    @Override
    public void onNoteDetailFinished(String title, String content) {
        if (mainView != null) {
            mainView.goToDetail(title,content);

        }
    }

}
