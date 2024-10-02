package com.example.nationapp.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.nationapp.local.entity.LocalNation.Companion.NATION_INFO_TABLE

@Entity(tableName = NATION_INFO_TABLE)
data class LocalNation(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_NATION_ID)
    val nationalId: Int? = null,
    @ColumnInfo(name = COLUMN_NATION_COMMON_NAME)
    val common : String = "",
    @ColumnInfo(name = COLUMN_NATION_OFFICIAL_NAME)
    val official: String = "",
    @ColumnInfo(name = COLUMN_NATION_FLAG)
    val png: String = "",
    @ColumnInfo(name = COLUMN_NATION_DETAIL_FLAG)
    val svg: String = "",
    @ColumnInfo(name = COLUMN_NATION_FLAG_DESCRIPTION)
    val alt: String = ""
) {
    companion object {
        internal const val NATION_INFO_TABLE = "Nation"
        internal const val COLUMN_NATION_ID = "Nation Id"
        internal const val COLUMN_NATION_OFFICIAL_NAME = "Nation Official Name"
        internal const val COLUMN_NATION_COMMON_NAME = "Nation Common Name"
        internal const val COLUMN_NATION_FLAG = "Nation Flag"
        internal const val COLUMN_NATION_DETAIL_FLAG = "Nation Detail Flag"
        internal const val COLUMN_NATION_FLAG_DESCRIPTION = "Nation Flag Description"
    }
}