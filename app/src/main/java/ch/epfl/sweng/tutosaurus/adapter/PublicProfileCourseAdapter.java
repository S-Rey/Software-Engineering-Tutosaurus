package ch.epfl.sweng.tutosaurus.adapter;

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
import ch.epfl.sweng.tutosaurus.R;
import ch.epfl.sweng.tutosaurus.model.Course;

/**
 * Created by albertochiappa on 24/11/16.
 */

public class PublicProfileCourseAdapter extends ArrayAdapter<Course> {
    Context context;
    int layoutResourceId;
    ArrayList<Course> courses = null;

    public PublicProfileCourseAdapter(Context context, int layoutResourceId, ArrayList<Course> courses){
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
            holder.courseSymbol = (ImageView) row.findViewById(R.id.coursePicture);
            holder.courseName = (TextView) row.findViewById(R.id.courseName);
            holder.courseDescription = (TextView)  row.findViewById((R.id.courseDescription));
            row.setTag(holder);
        }
        else{
            holder=(CourseHolder) row.getTag();
        }

        Course course = courses.get(position);
        holder.courseName.setText(course.getName());
        holder.courseSymbol.setImageResource(course.getPictureId());
        holder.courseDescription.setText(course.getDescription());

        final View currentRow = row;
        holder.courseName.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                PublicProfileCourseAdapter.openDescription(currentRow);
            }
        });

        holder.courseSymbol.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                PublicProfileCourseAdapter.openDescription(currentRow);

            }
        });

        return row;
    }

    static private void openDescription(final View row){
        row.findViewById(R.id.courseDescription).setVisibility(View.VISIBLE);
        setCloseDescriptionListener(row);
    }

    static private void closeDescription(final View row){
        row.findViewById(R.id.courseDescription).setVisibility(View.GONE);
        setOpenDescriptionListener(row);
    }

    static private void setCloseDescriptionListener(final View row) {
        final View.OnClickListener closeDescriptionListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDescription(row);
            }
        };
        row.findViewById(R.id.courseName).setOnClickListener(closeDescriptionListener);
        row.findViewById(R.id.coursePicture).setOnClickListener(closeDescriptionListener);
    }

    static private void setOpenDescriptionListener(final View row) {
        final View.OnClickListener openDescriptionListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDescription(row);
            }
        };
        row.findViewById(R.id.courseName).setOnClickListener(openDescriptionListener);
        row.findViewById(R.id.coursePicture).setOnClickListener(openDescriptionListener);
    }
    static private class CourseHolder{
        ImageView courseSymbol;
        TextView courseName;
        TextView courseDescription;
    }

    public Course getItemAtPosition(int position){
        return courses.get(position);
    }

}
