package com.auto.di.guan.db.sql;

import com.auto.di.guan.db.User;
import com.auto.di.guan.db.greendao.DaoSession;
import com.auto.di.guan.db.greendao.UserDao;
import com.auto.di.guan.utils.CopyObject;
import com.auto.di.guan.utils.LogUtils;
import com.google.gson.Gson;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class UserSql extends BaseSql {


    /**
     * 清空用户
     */
//    public static void cleanUser() {
//        DaoSession daoSession = getDaoWriteSession();
//        UserDao userDao = daoSession.getUserDao();
//        userDao.
//    }

    /**
     * 插入一条记录
     *
     * @param user
     */
    public static void insertUser(User user) {
        DaoSession daoSession = getDaoWriteSession();
        UserDao userDao = daoSession.getUserDao();
        userDao.insert(user);
    }

    /**
     * 插入用户集合
     *
     * @param users
     */
    public static void insertUserList(List<User> users) {
        if (users == null || users.isEmpty()) {
            return;
        }
        DaoSession daoSession = getDaoWriteSession();
        UserDao userDao = daoSession.getUserDao();
        userDao.insertInTx(users);
    }

    /**
     * 删除一条记录
     *
     * @param user
     */
    public static void deleteUser(User user) {
        DaoSession daoSession = getDaoWriteSession();
        UserDao userDao = daoSession.getUserDao();
        userDao.delete(user);
    }

    public static void deleteAllUser() {
        DaoSession daoSession = getDaoWriteSession();
        UserDao userDao = daoSession.getUserDao();
        userDao.deleteAll();
    }

    /**
     * 更新一条记录
     *
     * @param user
     */
    public static void updateUser(User user) {
        DaoSession daoSession = getDaoWriteSession();
        UserDao userDao = daoSession.getUserDao();
        QueryBuilder<User> qb = userDao.queryBuilder();
        User u = qb.where(UserDao.Properties.UserId.eq(user.getUserId())).list().get(0);
        CopyObject.copyUserData(u, user);
        LogUtils.e("------登陆成功更新用户信息", "" + (new Gson().toJson(u)));
        userDao.update(u);
    }


    /**
     * 查询用户列表
     */
    public static User queryUserInfo() {
        DaoSession daoSession = getDaoReadSession();
        UserDao userDao = daoSession.getUserDao();
        QueryBuilder<User> qb = userDao.queryBuilder();
        if (qb.list() != null && qb.list().size() > 0) {
            return  qb.list().get(0);
        }
        return null;
    }



    /**
     * 查询用户列表
     */
    public static List<User> queryUserList() {
        DaoSession daoSession = getDaoReadSession();
        UserDao userDao = daoSession.getUserDao();
        QueryBuilder<User> qb = userDao.queryBuilder();
        return qb.list();
    }

    /**
     * 查询用户列表
     */
    public static List<User> queryUserList(String account) {
        DaoSession daoSession = getDaoReadSession();
        UserDao userDao = daoSession.getUserDao();
        QueryBuilder<User> qb = userDao.queryBuilder();
        qb.where(UserDao.Properties.LoginName.eq(account)).orderAsc(UserDao.Properties.LoginName);
        List<User> list = qb.list();
        return list;
    }
}
