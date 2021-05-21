package com.project.searchproducts.viewmodels;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.project.searchproducts.db.ProductDatabase;
import com.project.searchproducts.models.ProductFavorite;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class FavoriteProductViewModel extends AndroidViewModel {
    private static ProductDatabase database;
    private final LiveData<List<ProductFavorite>> favouriteProducts;

    public FavoriteProductViewModel(@NonNull Application application) {
        super(application);
        database = ProductDatabase.getInstance(application);
        favouriteProducts = database.movieDao().getAllFavouriteProducts();
    }

    public ProductFavorite getFavouriteProductByID(int id) {
        try {
            return new GetFavouriteProductTask().execute(id).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public LiveData<List<ProductFavorite>> getFavouriteProducts() {
        return favouriteProducts;
    }

    public void insertFavouriteProduct(ProductFavorite productFavorite) {
        new InsertFavouriteProductsTask().execute(productFavorite);
    }

    public void deleteFavouriteProduct(ProductFavorite productFavorite) {
        new DeleteFavouriteProductsTask().execute(productFavorite);
    }

    public void deleteFavouriteProduct(int id) {
        new DeleteFavouriteProductsByIDTask().execute(id);
    }

    private static class DeleteFavouriteProductsTask extends AsyncTask<ProductFavorite, Void, Void> {
        @Override
        protected Void doInBackground(ProductFavorite... movies) {
            if (movies != null && movies.length > 0) {
                database.movieDao().deleteFavouriteProduct(movies[0]);
            }
            return null;
        }
    }

    private static class DeleteFavouriteProductsByIDTask extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... integers) {
            if (integers != null && integers.length > 0) {
                database.movieDao().deleteFavouriteProductById(integers[0]);
            }
            return null;
        }
    }

    private static class InsertFavouriteProductsTask extends AsyncTask<ProductFavorite, Void, Void> {
        @Override
        protected Void doInBackground(ProductFavorite... movies) {
            if (movies != null && movies.length > 0) {
                database.movieDao().insertFavouriteProduct(movies[0]);
            }
            return null;
        }
    }

    private static class GetFavouriteProductTask extends AsyncTask<Integer, Void, ProductFavorite> {
        @Override
        protected ProductFavorite doInBackground(Integer... integers) {
            if (integers != null && integers.length > 0) {
                return database.movieDao().getFavouriteProductById(integers[0]);
            }
            return null;
        }
    }
}
