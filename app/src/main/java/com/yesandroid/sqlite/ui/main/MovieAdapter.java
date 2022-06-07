package com.yesandroid.sqlite.ui.main;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.yesandroid.sqlite.R;
import com.yesandroid.sqlite.data.movie.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ali Asadi on 24/03/2018.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    public interface MovieListener {
        void onMovieClicked(Movie movie);
    }

    private List<Movie> items;
    private MovieListener listener;

    public MovieAdapter(MovieListener listener) {
        this.listener = listener;
        items = new ArrayList<>();
    }

    public void setItems(List<Movie> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private Movie getItem(int position) {
        return items.get(position);
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.image)
        AppCompatImageView image;
        @BindView(R.id.title) TextView title;
        @BindView(R.id.desc) TextView desc;

        MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(int position) {
            Movie movie = getItem(position);

            setClickListener(movie);
            setTitle(movie.getTitle());
            setImage(movie.getImage());
            setDescription(movie.getDescription());
        }

        private void setTitle(String title) {
            this.title.setText(title);
        }

        private void setImage(String imageUrl) {
            Picasso.get().load(imageUrl).into(image);
        }

        private void setDescription(String description) {
            desc.setText(description);
        }

        private void setClickListener(Movie movie) {
            itemView.setTag(movie);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onMovieClicked((Movie) view.getTag());
        }
    }
}
