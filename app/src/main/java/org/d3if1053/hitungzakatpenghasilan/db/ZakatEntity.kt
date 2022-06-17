package org.d3if1053.hitungzakatpenghasilan.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "zakat")
data class ZakatEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    val hargaEmas: Long,
    val gajiBulanan: Long,
    val bonusGaji: Long,
    var totalZakat: Long,
)
