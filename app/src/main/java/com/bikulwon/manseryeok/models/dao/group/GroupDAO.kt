package com.bikulwon.manseryeok.models.dao.group

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.bikulwon.manseryeok.models.user.groups.Group

@Dao
interface GroupDAO {
    @Insert
    fun insertGroup(group: Group): Long

    @Update
    fun updateGroup(group: Group)

    @Delete
    fun deleteGroup(group: Group)

    @Query("SELECT * FROM `group`")
    fun getAllGroups(): List<Group>

    @Query("SELECT * FROM `group` WHERE name = :groupName")
    fun getGroupByName(groupName: String): Group?
}