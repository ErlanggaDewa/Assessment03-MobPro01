package org.d3if1053.hitungzakatpenghasilan.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ZakatDao {
    @Insert
    fun insert(zakat: ZakatEntity)

    @Query("SELECT * FROM zakat")
    fun getData(): LiveData<List<ZakatEntity>>

    @Query("DELETE FROM zakat")
    fun clearData()

    @Delete
    fun deleteData(zakat: ZakatEntity)

}