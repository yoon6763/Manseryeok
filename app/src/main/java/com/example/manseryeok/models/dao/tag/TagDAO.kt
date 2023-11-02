package com.example.manseryeok.models.dao.tag

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.manseryeok.models.user.groups.Group
import com.example.manseryeok.models.user.tags.Tag

@Dao
interface TagDAO {
    @Insert
    fun insertTag(tag: Tag): Long

    @Update
    fun updateTga(tag: Tag)

    @Delete
    fun deleteTag(tag: Tag)

    @Query("SELECT * FROM tag")
    fun getAllTags(): List<Tag>
}
