package ch.epfl.sweng.tutosaurus.findTutor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ch.epfl.sweng.tutosaurus.PublicProfileActivity;
import ch.epfl.sweng.tutosaurus.R;
import ch.epfl.sweng.tutosaurus.model.User;

/**
 * Created by albertochiappa on 21/10/16.
 */

public class TutorAdapter extends ArrayAdapter<User> {
    Context context;
    int layoutResourceId;
    ArrayList<User> tutorList = null;

    public TutorAdapter(Context context, int layoutResourceId, ArrayList<User> tutorList){
        super(context,layoutResourceId,tutorList);
        this.layoutResourceId=layoutResourceId;
        this.context=context;
        this.tutorList=tutorList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View row=convertView;
        TutorHolder holder;
        if(row==null){
            LayoutInflater inflater=((Activity) context).getLayoutInflater();
            row=inflater.inflate(layoutResourceId, parent, false);

            holder=new TutorHolder();
            holder.profilePicture=(ImageView)row.findViewById(R.id.profilePicture);
            holder.profileName=(TextView) row.findViewById(R.id.profileName);

            row.setTag(holder);
        }
        else{
            holder=(TutorHolder) row.getTag();
        }
        User tutor = tutorList.get(position);
        holder.profileName.setText(tutor.getFullName());
        holder.profilePicture.setImageResource(tutor.getPicture());

        // Set the OnClickListener on each name of the list
        final Intent intent = new Intent(context, PublicProfileActivity.class);
        intent.putExtra("SCIPER_NUMBER",tutor.getSciper());

        holder.profileName.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                v.getContext().startActivity(intent);
            }
        });

        return row;
    }

    static private class TutorHolder{
        ImageView profilePicture;
        TextView profileName;
    }

    public User getItemAtPosition(int position){
        return tutorList.get(position);
    }

}
