package com.example.joseluissanchez_porrogodoy.everclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.evernote.edam.notestore.NoteList;
import com.evernote.edam.type.Note;
import com.example.joseluissanchez_porrogodoy.everclient.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joseluissanchez-porrogodoy on 1/8/16.
 */

public class NoteListAdapter extends ArrayAdapter<Note>{

    private Context context;
    private List<Note> notes;
    private static final int RESOURCE = R.layout.item;

    public NoteListAdapter(Context context, List<Note> notes) {
        super(context,RESOURCE,notes);
        this.context= context;
        this.notes= notes;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LinearLayout view;
        ViewHolder holder;

        if (convertView == null){
            view = new LinearLayout(context);

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(RESOURCE, view, true);

            holder = new ViewHolder();
            holder.text1 = (TextView) view.findViewById(R.id.text);

            view.setTag(holder);

        } else {
            view = (LinearLayout) convertView;
            holder = (ViewHolder) view.getTag();
        }

        //Rellenamos la vista con los datos
        Note note = notes.get(position);
        holder.text1.setText(note.getTitle());


        return view;
    }

    static class ViewHolder {
        TextView text1;

    }
}
