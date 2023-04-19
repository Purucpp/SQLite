package com.yesandroid.sqlite.base.classes;

import androidx.annotation.NonNull;
import androidx.lifecycle.HasDefaultViewModelProviderFactory;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;

import java.lang.reflect.InvocationTargetException;

import in.yesandroid.base_android.BaseApp;

public class BaseViewStoreOwner implements ViewModelStoreOwner, HasDefaultViewModelProviderFactory {
    private static BaseViewStoreOwner INSTANCE;
    private BaseApp application;

    public BaseViewStoreOwner(BaseApp application) {
        this.application = application;
    }

    public static ViewModelStoreOwner getInstance(BaseApp application) {
        if (INSTANCE != null) {
            return INSTANCE;
        }
        synchronized (BaseViewStoreOwner.class) {
            if (INSTANCE != null) {
                return INSTANCE;
            }
            return INSTANCE = new BaseViewStoreOwner(application);

        }
    }

    @NonNull
    @Override
    public ViewModelStore getViewModelStore() {
        return new BaseModelStore();
    }

    @NonNull
    @Override
    public ViewModelProvider.Factory getDefaultViewModelProviderFactory() {
        return new BaseViewModelFactory(application);
    }

    private static class BaseViewModelFactory implements ViewModelProvider.Factory {
        private BaseApp application;

        public BaseViewModelFactory(BaseApp application) {
            this.application = application;

        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            //noinspection TryWithIdenticalCatches
            try {
                return modelClass.getDeclaredConstructor(BaseApp.class).newInstance(application);
            } catch (InstantiationException e) {
                throw new RuntimeException("Cannot create an instance of " + modelClass, e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Cannot create an instance of " + modelClass, e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("Unable to find the required constructor (Application.class) of" + modelClass, e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException("Cannot invoke an instance of " + modelClass, e);
            }
        }
    }

    private static class BaseModelStore extends ViewModelStore {


    }
}
