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

    /**
     * Повертає улюблений товар з бази даних по його індексу
     * @param id
     * @return
     */
    public ProductFavorite getFavouriteProductByID(int id) {
        try {
            return new GetFavouriteProductTask().execute(id).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Повертає список улюблених товарів
     * @return
     */
    public LiveData<List<ProductFavorite>> getFavouriteProducts() {
        return favouriteProducts;
    }

    /**
     * Додає улюблений товар в базу даних
     * @param productFavorite
     */
    public void insertFavouriteProduct(ProductFavorite productFavorite) {
        new InsertFavouriteProductsTask().execute(productFavorite);
    }

    /**
     * Видаляє улюблений товар з бази даних
     * @param productFavorite
     */
    public void deleteFavouriteProduct(ProductFavorite productFavorite) {
        new DeleteFavouriteProductsTask().execute(productFavorite);
    }

    /**
     * Видаляє улюблений товар з бази даних по його індексу
     * @param id
     */
    public void deleteFavouriteProduct(int id) {
        new DeleteFavouriteProductsByIDTask().execute(id);
    }

    /**
     * Перевіряє чи є улюблений товар в базі даних по його індексу
     * @param id
     * @return
     */
    public Boolean isExist(int id) {
        try {
            return new IsExistTask().execute(id).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
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

    private static class IsExistTask extends AsyncTask<Integer, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Integer... integers) {
            if (integers != null && integers.length > 0) {
                return database.movieDao().isExist(integers[0]);
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
