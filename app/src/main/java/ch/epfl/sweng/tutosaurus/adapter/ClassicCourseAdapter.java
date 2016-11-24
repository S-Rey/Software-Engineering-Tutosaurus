package ch.epfl.sweng.tutosaurus.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ch.epfl.sweng.tutosaurus.FindTutorResult;
import ch.epfl.sweng.tutosaurus.PublicProfileActivity;
import ch.epfl.sweng.tutosaurus.R;
import ch.epfl.sweng.tutosaurus.model.Course;
import ch.epfl.sweng.tutosaurus.model.User;

/**
 * Created by albertochiappa on 23/11/16.
 */

public class ClassicCourseAdapter extends ArrayAdapter<Course> {
    Context context;
    int layoutResourceId;
    ArrayList<Course> courses = null;

    public ClassicCourseAdapter(Context context, int layoutResourceId, ArrayList<Course> courses){
        super(context,layoutResourceId,courses);
        this.layoutResourceId=layoutResourceId;
        this.context=context;
        this.courses=courses;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View row=convertView;
        CourseHolder holder;

        if(row==null){
            LayoutInflater inflater=(LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row=inflater.inflate(layoutResourceId, parent, false);

            holder=new CourseHolder();
            holder.courseSymbol=(ImageView)row.findViewById(R.id.coursePicture);
            holder.courseName=(TextView) row.findViewById(R.id.courseName);

            row.setTag(holder);
        }
        else{
            holder=(CourseHolder) row.getTag();
        }

        Course course = courses.get(position);
        holder.courseName.setText(course.getName());
        holder.courseSymbol.setImageResource(course.getPictureId());

        // Set the OnClickListener on each name of the list
        final Intent intent = new Intent(context,FindTutorResult.class);
        Bundle extras = new Bundle();
        extras.putString("METHOD_TO_CALL","findTutorByCourse");
        extras.putString("COURSE_ID",course.getId());
        intent.putExtras(extras);

        holder.courseName.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                v.getContext().startActivity(intent);
            }
        });

        holder.courseSymbol.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                v.getContext().startActivity(intent);
            }
        });

        return row;
    }

    static private class CourseHolder{
        ImageView courseSymbol;
        TextView courseName;
    }

    public Course getItemAtPosition(int position){
        return courses.get(position);
    }

}
