package ch.epfl.sweng.tutosaurus.findTutor;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ch.epfl.sweng.tutosaurus.R;

/**
 * Created by albertochiappa on 21/10/16.
 */

public class TutorAdapter extends ArrayAdapter<Tutor> {
    Context context;
    int layoutResourceId;
    ArrayList<Tutor> tutorList = null;

    public TutorAdapter(Context context, int layoutResourceId, ArrayList<Tutor> tutorList){
        super(context,layoutResourceId,tutorList);
        this.layoutResourceId=layoutResourceId;
        this.context=context;
        this.tutorList=tutorList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View row=convertView;
        TutorHolder holder=null;
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
        Tutor tutor = tutorList.get(position);
        holder.profileName.setText(tutor.profileName);
        holder.profilePicture.setImageResource(tutor.profilePicture);

        return row;
    }

    static private class TutorHolder{
        ImageView profilePicture;
        TextView profileName;
    }

}
