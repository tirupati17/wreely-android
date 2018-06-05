package com.celerstudio.wreelysocial.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.celerstudio.wreelysocial.persistence.DatabaseUtils;

import java.util.List;

public class SkyAppDataViewModel extends AndroidViewModel {

    public SkyAppDataViewModel(@NonNull Application application) {
        super(application);
    }
}
