package com.auto.di.guan.db.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.auto.di.guan.db.LevelInfo;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "LEVEL_INFO".
*/
public class LevelInfoDao extends AbstractDao<LevelInfo, Long> {

    public static final String TABLENAME = "LEVEL_INFO";

    /**
     * Properties of entity LevelInfo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property LevelId = new Property(1, int.class, "levelId", false, "LEVEL_ID");
        public final static Property IsGroupUse = new Property(2, boolean.class, "isGroupUse", false, "IS_GROUP_USE");
        public final static Property IsLevelUse = new Property(3, boolean.class, "isLevelUse", false, "IS_LEVEL_USE");
        public final static Property IsOtherUse = new Property(4, boolean.class, "isOtherUse", false, "IS_OTHER_USE");
    }


    public LevelInfoDao(DaoConfig config) {
        super(config);
    }
    
    public LevelInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"LEVEL_INFO\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"LEVEL_ID\" INTEGER NOT NULL ," + // 1: levelId
                "\"IS_GROUP_USE\" INTEGER NOT NULL ," + // 2: isGroupUse
                "\"IS_LEVEL_USE\" INTEGER NOT NULL ," + // 3: isLevelUse
                "\"IS_OTHER_USE\" INTEGER NOT NULL );"); // 4: isOtherUse
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"LEVEL_INFO\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, LevelInfo entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getLevelId());
        stmt.bindLong(3, entity.getIsGroupUse() ? 1L: 0L);
        stmt.bindLong(4, entity.getIsLevelUse() ? 1L: 0L);
        stmt.bindLong(5, entity.getIsOtherUse() ? 1L: 0L);
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, LevelInfo entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getLevelId());
        stmt.bindLong(3, entity.getIsGroupUse() ? 1L: 0L);
        stmt.bindLong(4, entity.getIsLevelUse() ? 1L: 0L);
        stmt.bindLong(5, entity.getIsOtherUse() ? 1L: 0L);
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public LevelInfo readEntity(Cursor cursor, int offset) {
        LevelInfo entity = new LevelInfo( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getInt(offset + 1), // levelId
            cursor.getShort(offset + 2) != 0, // isGroupUse
            cursor.getShort(offset + 3) != 0, // isLevelUse
            cursor.getShort(offset + 4) != 0 // isOtherUse
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, LevelInfo entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setLevelId(cursor.getInt(offset + 1));
        entity.setIsGroupUse(cursor.getShort(offset + 2) != 0);
        entity.setIsLevelUse(cursor.getShort(offset + 3) != 0);
        entity.setIsOtherUse(cursor.getShort(offset + 4) != 0);
     }
    
    @Override
    protected final Long updateKeyAfterInsert(LevelInfo entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(LevelInfo entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(LevelInfo entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
