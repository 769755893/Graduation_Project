package com.app.project.hotel.room

import androidx.room.*

@Entity(tableName = "rememberUser")
data class TableColumn(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "m_id")
    val mId: Int = 0,
    @ColumnInfo(name = "user_name")
    val userName: String,
    @ColumnInfo(name = "user_pass")
    val userPass: String
)
@Dao
interface MasterDao {
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("select user_name, user_pass from rememberUser where m_id = " +
            "(select max(m_id) from rememberUser )")
    suspend fun queryUser(): TmpUser?

    @Ignore
    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = TableColumn::class)
    suspend fun keepUser(vararg user: TmpUser)

    @Query("update rememberUser set user_pass = ''")
    suspend fun clearTable()
}

@Database(version = 1, exportSchema = false, entities = [TableColumn::class])
abstract class DataBase: RoomDatabase() {
    val dao: MasterDao by lazy { createDao() }

    abstract fun createDao(): MasterDao
}

data class TmpUser(
    @ColumnInfo(name = "user_name") val userName: String,
    @ColumnInfo(name = "user_pass") val userPass: String
)


