package com.glowroad.photosexample.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import com.glowroad.photosexample.data.NetworkState;
import com.glowroad.photosexample.data.dataInputs.UsersDataSource;
import com.glowroad.photosexample.data.dataInputs.UsersDataSourceFactory;
import com.glowroad.photosexample.entites.Photo;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Mahesh Kumar on 8/14/2018.
 */
public class UsersViewModel extends ViewModel {

    LiveData<PagedList<Photo>> userList;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private static final int pageSize = 15;

    private UsersDataSourceFactory usersDataSourceFactory;

    public UsersViewModel() {
        usersDataSourceFactory = new UsersDataSourceFactory(compositeDisposable);
        PagedList.Config config = new PagedList.Config.Builder()
                .setPageSize(pageSize)
                .setInitialLoadSizeHint(pageSize * 2)
                .setEnablePlaceholders(false)
                .build();

        userList = new LivePagedListBuilder<>(usersDataSourceFactory, config).build();
    }

    public void retry() {
        usersDataSourceFactory.getUsersDataSourceLiveData().getValue().retry();
    }

    public void refresh() {
        usersDataSourceFactory.getUsersDataSourceLiveData().getValue().invalidate();
    }

    public LiveData<NetworkState> getNetworkState() {
        return Transformations.switchMap(usersDataSourceFactory.getUsersDataSourceLiveData(), UsersDataSource::getNetworkState);
    }

    public LiveData<NetworkState> getRefreshState() {
        return Transformations.switchMap(usersDataSourceFactory.getUsersDataSourceLiveData(), UsersDataSource::getInitialLoad);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }

}
