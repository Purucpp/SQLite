package com.yesandroid.sqlite.base.data;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Transaction;
import androidx.room.Update;

public abstract class BaseDao<T> {


    @Insert
    public abstract long[] insertData(T... data);

    @Insert
    public abstract long insertSingleData(T data);


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract long[] ignoreInsert(T... data);


    @Delete
    public abstract int deleteData(T... data);

    @Delete
    public abstract int deleteData(T data);


    @Update
    public abstract int updateData(T... data);

    @Update
    public abstract int singleUpdateData(T data);


    @Transaction
    public long[] upsertData(T... data) {
        long[] actualReturns = new long[data.length];

        long[] insertedData = ignoreInsert(data);

        for (int i = 0; i < insertedData.length; i++) {
            if (insertedData[i] == -1) {
                if (data[i] != null) {
                    //noinspection unchecked
                    actualReturns[i] = singleUpdateData(data[i]);
                }
            } else {
                actualReturns[i] = insertedData[i];
            }
        }

        return actualReturns;

    }


}
