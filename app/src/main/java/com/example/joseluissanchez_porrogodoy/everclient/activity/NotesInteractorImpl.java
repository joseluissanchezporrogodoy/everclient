package com.example.joseluissanchez_porrogodoy.everclient.activity;

import android.content.Intent;

import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.EvernoteUtil;
import com.evernote.client.android.asyncclient.EvernoteCallback;
import com.evernote.client.android.asyncclient.EvernoteNoteStoreClient;
import com.evernote.edam.notestore.NoteFilter;
import com.evernote.edam.notestore.NoteList;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.NoteSortOrder;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by joseluissanchez-porrogodoy on 2/8/16.
 */
public class NotesInteractorImpl implements NotesInteractor {
    public static final int SORT_ALPHABETICAL = 4;
    public static final int SORT_EDIT = 2;
    @Override
    public void findItems(final OnFinishedListener listener, int sortMode) {

        if (!EvernoteSession.getInstance().isLoggedIn()) {
            listener.onNotesListLoadFinished(null);
        }
        NoteFilter filter = new NoteFilter();
        if(sortMode == SORT_ALPHABETICAL)
            filter.setOrder(NoteSortOrder.TITLE.getValue());
        else
            filter.setOrder(NoteSortOrder.UPDATED.getValue());

        EvernoteNoteStoreClient noteStoreClient = EvernoteSession.getInstance().getEvernoteClientFactory().getNoteStoreClient();
        noteStoreClient.findNotesAsync(filter, 0, 100, new EvernoteCallback<NoteList>() {
            @Override
               public void onSuccess(NoteList noteList) {
                listener.onNotesListLoadFinished(noteList);
               }

            @Override
                public void onException(Exception e) {
                    ///mostrar mensaje de error
                }
        });


    }

    @Override
    public void getDetail(final OnFinishedListener listener, final Note note) {
        EvernoteNoteStoreClient noteStoreClient = EvernoteSession.getInstance().getEvernoteClientFactory().getNoteStoreClient();
        noteStoreClient.getNoteContentAsync(note.getGuid(), new EvernoteCallback<String>() {
            @Override
            public void onSuccess(String result) {
               listener.onNoteDetailFinished(note.getTitle(),getContentString(result));
            }

            @Override
            public void onException(Exception exception) {
                //mostrar mensaje de error
            }
        });
    }

    @Override
    public void addNote(final OnAddFinishedListener listener,String title, String content) {
        Note note= new Note();
        note.setTitle(title);
        String nBody = EvernoteUtil.NOTE_PREFIX+ content + EvernoteUtil.NOTE_SUFFIX;
        note.setContent(nBody);
        EvernoteNoteStoreClient noteStoreClient = EvernoteSession.getInstance().getEvernoteClientFactory().getNoteStoreClient();
        noteStoreClient.createNoteAsync(note, new EvernoteCallback<Note>() {
            @Override
            public void onSuccess(Note result) {
               listener.onNoteAddlFinished();
            }

            @Override
            public void onException(Exception exception) {
                //mostrar mensaje de error

            }
        });
    }

    public String getContentString(String contentXml){
        try{
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource src = new InputSource();
            src.setCharacterStream(new StringReader(contentXml));
            Document doc = builder.parse(src);
            String content = doc.getElementsByTagName("en-note").item(0).getTextContent();
            return content;
        }catch (Exception e){

        }
        return null;
    }
}
