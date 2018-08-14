package com.glowroad.photosexample.data.dataInputs;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.ItemKeyedDataSource;
import android.support.annotation.NonNull;

import com.glowroad.photosexample.api.GlowRoadService;
import com.glowroad.photosexample.data.NetworkState;
import com.glowroad.photosexample.entites.Photo;
import com.glowroad.photosexample.utils.LogHelper;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

import static com.glowroad.photosexample.utils.Constants.API_KEY;
import static com.glowroad.photosexample.utils.Constants.EXTRAS;
import static com.glowroad.photosexample.utils.Constants.METHOD_TYPE;
import static com.glowroad.photosexample.utils.Constants.NON_JSON_CALL;
import static com.glowroad.photosexample.utils.Constants.RESPONSE_FORMAT;
import static com.glowroad.photosexample.utils.Constants.TEXT_TYPE;

/**
 * Created by Mahesh Kumar on 8/14/2018.
 */
public class UsersDataSource extends ItemKeyedDataSource<Long, Photo> {

    private GlowRoadService glowRoadService;

    private CompositeDisposable compositeDisposable;

    private MutableLiveData<NetworkState> networkState = new MutableLiveData<>();

    private MutableLiveData<NetworkState> initialLoad = new MutableLiveData<>();

    /**
     * Keep Completable reference for the retry event
     */
    private Completable retryCompletable;

    UsersDataSource(CompositeDisposable compositeDisposable) {
        this.glowRoadService = GlowRoadService.getService();
        this.compositeDisposable = compositeDisposable;
    }

    public void retry() {
        if (retryCompletable != null) {
            compositeDisposable.add(retryCompletable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                    }, throwable -> LogHelper.e(throwable.getMessage())));
        }
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Long> params, @NonNull LoadInitialCallback<Photo> callback) {
        // update network states.
        // we also provide an initial load state to the listeners so that the UI can know when the
        // very first list is loaded.
        networkState.postValue(NetworkState.LOADING);
        initialLoad.postValue(NetworkState.LOADING);

        //get the initial users from the api
        compositeDisposable.add(glowRoadService.getUsers(METHOD_TYPE, API_KEY, RESPONSE_FORMAT, NON_JSON_CALL, TEXT_TYPE, EXTRAS).subscribe(users -> {
                    // clear retry since last request succeeded
                    setRetry(null);
                    networkState.postValue(NetworkState.LOADED);
                    initialLoad.postValue(NetworkState.LOADED);
                    callback.onResult(users.getPhotos().getPhoto());
                },
                throwable -> {
                    // keep a Completable for future retry
                    setRetry(() -> loadInitial(params, callback));
                    NetworkState error = NetworkState.error(throwable.getMessage());
                    // publish the error
                    networkState.postValue(error);
                    initialLoad.postValue(error);
                }));
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Photo> callback) {
        // set network value to loading.
        networkState.postValue(NetworkState.LOADING);

        //get the users from the api after id
        compositeDisposable.add(glowRoadService.getUsers(METHOD_TYPE, API_KEY, RESPONSE_FORMAT, NON_JSON_CALL, TEXT_TYPE, EXTRAS).subscribe(users -> {
                    // clear retry since last request succeeded
                    setRetry(null);
                    networkState.postValue(NetworkState.LOADED);
                    callback.onResult(users.getPhotos().getPhoto());
                },
                throwable -> {
                    // keep a Completable for future retry
                    setRetry(() -> loadAfter(params, callback));
                    // publish the error
                    networkState.postValue(NetworkState.error(throwable.getMessage()));
                }));
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Photo> callback) {
        // ignored, since we only ever append to our initial load
    }

    @NonNull
    @Override
    public Long getKey(@NonNull Photo item) {
        return Long.parseLong(item.getId());
    }

    @NonNull
    public MutableLiveData<NetworkState> getNetworkState() {
        return networkState;
    }

    @NonNull
    public MutableLiveData<NetworkState> getInitialLoad() {
        return initialLoad;
    }

    private void setRetry(final Action action) {
        if (action == null) {
            this.retryCompletable = null;
        } else {
            this.retryCompletable = Completable.fromAction(action);
        }
    }

}
