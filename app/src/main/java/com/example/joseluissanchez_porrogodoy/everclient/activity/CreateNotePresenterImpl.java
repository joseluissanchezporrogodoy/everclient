package com.example.joseluissanchez_porrogodoy.everclient.activity;

/**
 * Created by joseluissanchez-porrogodoy on 2/8/16.
 */
public class CreateNotePresenterImpl implements CreateNotePresenter, NotesInteractor.OnAddFinishedListener {
    private NotesInteractor findNotesInteractor;
    private CreateNoteView createNoteView;

    public CreateNotePresenterImpl(CreateNoteView createNoteView){
        this.createNoteView=createNoteView;
        findNotesInteractor= new NotesInteractorImpl();
    }
    @Override
    public void onNoteAddlFinished() {
        if(createNoteView !=null){
            createNoteView.addNote();
        }
    }

    @Override
    public void onAddButtonCliked(String title, String content) {
        findNotesInteractor.addNote(this, title, content);
    }
}
