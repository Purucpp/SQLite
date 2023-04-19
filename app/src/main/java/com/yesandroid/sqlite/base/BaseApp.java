package com.yesandroid.sqlite.base;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import in.yesandroid.base_android.init.ModuleInitializationException;
import in.yesandroid.base_android.module.BaseModule;
import in.yesandroid.base_android.prefs.BaseSharedPreferences;

public class BaseApp extends Application {

    // TODO: 19/05/20 Remvoe this variable of list of databases in the future as it may not be a good approach
    private List<RoomDatabase> listOfDataBase = new ArrayList<>();
    private List<BaseSharedPreferences> listOfSharedPrefs = new ArrayList<>();
    private int appVersion;
    private String appName;
    private List<BaseModule> modules = new ArrayList<>();

    public BaseApp() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }


    public void setAppVersion(int appVersion) {
        this.appVersion = appVersion;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    /**
     * Default class to register a database. View {@link #registerDataBase(RoomDatabase, boolean, RoomDatabase)} for more options
     *
     * @param database The database to register
     * @throws ModuleInitializationException When the database is already built
     */
    public void registerDataBase(RoomDatabase database) throws ModuleInitializationException {
        registerDataBase(database, false, null);
    }


    /**
     * Default class to register a database and snitch to the main application database
     *
     * @param database         The database to register
     * @param snitchData       True if the database needs to be attached to the main application db or False
     * @param mainRoomDataBase The main application database to be attached
     * @throws ModuleInitializationException When the database is already built
     */
    public void registerDataBase(@NonNull RoomDatabase database, @NonNull boolean snitchData, @Nullable RoomDatabase mainRoomDataBase) throws ModuleInitializationException {
        if (checkDatabaseObjects(database) == null) {
            listOfDataBase.add(database);
            if (snitchData) {
                attachDataBaseToMain(database, mainRoomDataBase);
            }
        } else {
            throw new ModuleInitializationException("Re-registering database again");

        }
    }

    /**
     * Used to connect two database together. This will also handle the upgrade of db before attach.
     * Read {@link SupportSQLiteOpenHelper#getReadableDatabase()} for more information
     *
     * @param attachingDataBase The new database
     * @param mainRoomDataBase  The database object to attached to
     */
    private void attachDataBaseToMain(RoomDatabase attachingDataBase, RoomDatabase mainRoomDataBase) {
        SupportSQLiteDatabase readableDatabase = attachingDataBase.getOpenHelper().getReadableDatabase();
        String sql = "ATTACH ? AS ?";

        mainRoomDataBase.query(sql, new String[]{readableDatabase.getPath(), attachingDataBase.getOpenHelper().getDatabaseName()});
    }

    /**
     * Check if the object is already registered
     *
     * @param database The database to be checked
     * @return database object if found or else send null
     */
    private RoomDatabase checkDatabaseObjects(RoomDatabase database) {
        Class<?> clazz = database.getClass();
        for (RoomDatabase roomDatabase : listOfDataBase) {
            if (roomDatabase.getClass() == clazz) {
                return roomDatabase;

            }
        }
        return null;
    }


    /**
     * Check if the database exist with respect to class
     *
     * @param clazz The class to check if the database object exist
     * @return return the database object if found else sends null
     */
    private RoomDatabase checkDatabaseObjects(Class<?> clazz) {

        for (RoomDatabase roomDatabase : listOfDataBase) {
            if (roomDatabase.getClass().getSuperclass() == clazz) {
                return roomDatabase;

            }
        }
        return null;
    }


    /**
     * Used to query a particular database
     *
     * @param clazz The class object of the database
     * @param <T>   The return of the database to accommodate automatic type case
     * @return Return if the object is found else throws {@link ModuleInitializationException}
     * @throws ModuleInitializationException if the object is not found
     */
    public <T extends RoomDatabase> T queryDataBase(Class<T> clazz) throws ModuleInitializationException {
        RoomDatabase database = checkDatabaseObjects(clazz);

        if (database != null) {
            return (T) database;
        } else {
            throw new ModuleInitializationException("Database queried before initialization");
        }
    }

    /**
     * Used to query sharedprefs of a module
     *
     * @param clazz the class of the sharedprefs
     * @param <P>   Return class of the query
     * @return Queried Shared pref
     * @throws ModuleInitializationException if the shared prefs is not registered yet queried
     */
    public <P extends BaseSharedPreferences> P querySharedPrefs(Class<P> clazz) throws ModuleInitializationException {
        BaseSharedPreferences baseSharedPreferences = checkSharedPrefs(clazz);
        if (baseSharedPreferences != null) {
            return (P) baseSharedPreferences;
        } else {
            throw new ModuleInitializationException("Shared Prefs queried before initialization");
        }

    }

    /**
     * Check if shared prefs is registered
     *
     * @param clazz The class of the shared pref
     * @return clazz object if the sharedprefs is found or null in case not found
     */
    private BaseSharedPreferences checkSharedPrefs(Class<?> clazz) {
        for (BaseSharedPreferences baseSharedPreferences : listOfSharedPrefs) {
            if (baseSharedPreferences.getClass() == clazz) {
                return baseSharedPreferences;
            }
        }
        return null;
    }

    /**
     * Register a shared pref wihin a application
     *
     * @param baseSharedPreferences The shared prefs to register
     * @return
     * @throws ModuleInitializationException
     */
    public boolean registerSharedPrefs(BaseSharedPreferences baseSharedPreferences) throws ModuleInitializationException {
        if (checkSharedPrefs(baseSharedPreferences.getClass()) == null) {
            listOfSharedPrefs.add(baseSharedPreferences);
            return true;
        } else {
            throw new ModuleInitializationException("Re-registering sharedprefs again");
        }
    }

    /**
     * Use this function to support module request in an activity or fragment
     */
    public void injectModule(Class<? extends BaseModule> clazz) {

        try {
            BaseModule clazzobj = clazz.getDeclaredConstructor().newInstance();
            clazzobj.init(this);
            registerDataBase(clazzobj.getRoomDatabase());
            modules.add(clazzobj);

        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    public <T extends BaseModule> T queryModule(Class<T> clazz) {

        for (BaseModule module : modules) {
            if (module.getClass() == clazz) {
                return ((T) module);
            }
        }
        return null;
    }

    public void clearDb() {
        for (RoomDatabase roomDatabase : listOfDataBase) {
            roomDatabase.clearAllTables();
        }
    }
}
