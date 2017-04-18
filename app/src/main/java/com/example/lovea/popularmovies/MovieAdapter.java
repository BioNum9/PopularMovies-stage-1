package com.example.lovea.popularmovies;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.lovea.popularmovies.data.DefaultValue;
import com.example.lovea.popularmovies.domain.MovieModel;
import com.example.lovea.popularmovies.utilities.NormalUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by lovea on 2017/4/8.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    public List<MovieModel> movieModelList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView moviePicImageView;
        public ViewHolder(ImageView imageView) {
            super(imageView);
            moviePicImageView = imageView;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ImageView imageView = (ImageView) LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(imageView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieAdapter.ViewHolder holder, int position) {
        //use Picasso
        Picasso.with(holder.moviePicImageView.getContext()).load(NormalUtil.getFullPicPath(movieModelList.get(position).getPosterPath())).into(holder.moviePicImageView);
    }

    @Override
    public int getItemCount() {
        return movieModelList.size();
    }

    public MovieAdapter(List<MovieModel> movieModelList){
        this.movieModelList = movieModelList;
    }



}
