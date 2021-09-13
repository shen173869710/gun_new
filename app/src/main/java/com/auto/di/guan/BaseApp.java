package com.auto.di.guan;

import android.app.Application;
import android.content.Context;
import android.content.Entity;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.auto.di.guan.basemodel.model.respone.LoginRespone;
import com.auto.di.guan.db.GroupInfo;
import com.auto.di.guan.db.User;
import com.auto.di.guan.db.greendao.DaoMaster;
import com.auto.di.guan.db.greendao.DaoSession;
import com.auto.di.guan.db.sql.GroupInfoSql;
import com.auto.di.guan.db.sql.UserSql;
import com.auto.di.guan.db.update.MySQLiteOpenHelper;
import com.auto.di.guan.rtm.ChatManager;
import com.auto.di.guan.utils.CrashHandler;
import com.auto.di.guan.utils.FloatStatusUtil;
import com.auto.di.guan.utils.FloatWindowUtil;
import com.auto.di.guan.utils.GsonUtil;
import com.auto.di.guan.utils.LogUtils;
import com.auto.di.guan.utils.SPUtils;
import com.github.moduth.blockcanary.BlockCanary;
import com.github.moduth.blockcanary.BlockCanaryContext;
import com.github.moduth.blockcanary.internal.BlockInfo;
import com.google.gson.Gson;
//import com.tencent.bugly.crashreport.CrashReport;

import org.greenrobot.greendao.identityscope.IdentityScopeType;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
/**
 * Created by Administrator on 2017/6/28.
 */

public class BaseApp extends MultiDexApplication {

    public static String TAG = "BaseApp";
    public static String DB_NAME = "guan.db";
    private static MySQLiteOpenHelper mSQLiteOpenHelper;
    public static DaoSession mDaoWriteSession;
    private static DaoSession mDaoReadSession;
    private static BaseApp instance;

    public static User getUser() {
        if (user == null) {
            user = UserSql.queryUserInfo();
        }
        return user;
    }

    public static void setUser(User user) {
        LogUtils.e(TAG, "初始化用户信息" + (new Gson().toJson(user)));
        BaseApp.user = user;
//        user.setMemberId(158l);
    }

    private static User user;
    public static boolean groupIsStart;

    public SerialPortFinder mSerialPortFinder;
    private SerialPort mSerialPort = null;

    private static Context mContext=null;//上下文

    private static boolean webLogin;

    private ChatManager mChatManager;


    @Override
    public void onCreate() {
        super.onCreate();
        this.instance = this;
        mContext = getApplicationContext();
        mSerialPortFinder = new SerialPortFinder();
        LogUtils.setFilterLevel(LogUtils.ALL);
        FloatWindowUtil.getInstance().initFloatWindow(this);
        FloatStatusUtil.getInstance().initFloatWindow(this);
//        CrashReport.initCrashReport(getApplicationContext(), "d1930c180d", false);
        CrashHandler.getInstance().init(this);
        mChatManager = new ChatManager(this);
        mChatManager.init();
        BlockCanary.install(this, new AppBlockCanaryContext()).start();
    }



    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
        LogUtils.e(TAG, "attachBaseContext");
    }
    public static BaseApp getInstance() {
        return instance;
    }

    public static Context getContext() {
        return mContext;
    }


    public static DaoSession getDaoWriteSession(){
        if(null == mSQLiteOpenHelper){
            initSQLiteOpenHelper();
        }
        if(null == mDaoWriteSession){
            synchronized (DaoSession.class){
                if(null == mDaoWriteSession){
                    SQLiteDatabase sqLiteDatabase = mSQLiteOpenHelper.getWritableDatabase();
                    if(null !=sqLiteDatabase && sqLiteDatabase.isOpen()){
                        mDaoWriteSession = new DaoMaster(sqLiteDatabase).newSession(IdentityScopeType.None);
                    }else{
                        mDaoWriteSession = null;
                        return null;
                    }
                }
            }
        }
        return  mDaoWriteSession;
    }

    /**
     * 磁盘满了依然可读,
     * newSession 不能使用默认级别,否则写进去读取有问题
     *
     * @return
     */
    public static DaoSession getDaoReadSession(){
        if(null == mSQLiteOpenHelper){
            initSQLiteOpenHelper();
        }

        if(null == mDaoReadSession){
            synchronized (DaoSession.class){
                if(null == mDaoReadSession){
                    SQLiteDatabase sqLiteDatabase = mSQLiteOpenHelper.getReadableDatabase();
                    //sqLiteDatabase.setLocale(Locale.getDefault()); //设置数据库使用的语言
                    if(null!=sqLiteDatabase && sqLiteDatabase.isOpen()){
                        mDaoReadSession = new DaoMaster(sqLiteDatabase).newSession(IdentityScopeType.None);
                    }else{
                        mDaoReadSession = null;
                    }
                }
            }
        }
        return  mDaoReadSession;
    }


    public static MySQLiteOpenHelper initSQLiteOpenHelper(){
        try{
            if(null == mSQLiteOpenHelper){
                synchronized (MySQLiteOpenHelper.class){
                    if(null ==mSQLiteOpenHelper){
                        mSQLiteOpenHelper = new MySQLiteOpenHelper(mContext, DB_NAME, null);//建库
                    }
                }
            }
            return mSQLiteOpenHelper;
        }catch (Exception e){
            LogUtils.e(TAG,e.getMessage());
            return null;
        }
    }


    public SerialPort getSerialPort() throws SecurityException, IOException, InvalidParameterException {
        if (mSerialPort == null) {
			/* Read serial port parameters */
//            SharedPreferences sp = getSharedPreferences("android_serialport_api.sample_preferences", MODE_PRIVATE);
//            String path = sp.getString("DEVICE", "");
//            int baudrate = Integer.decode(sp.getString("BAUDRATE", "-1"));
//
//			/* Check parameters */
//            if ( (path.length() == 0) || (baudrate == -1)) {
//                throw new InvalidParameterException();
//            }

			/* Open the serial port */
//            mSerialPort = new SerialPort(new File(path), baudrate, 0);
            mSerialPort = new SerialPort(new File("/dev/ttyS1"), 115200, 0);
        }

        return mSerialPort;
    }

    public void closeSerialPort() {
         if (mSerialPort != null) {
                mSerialPort.close();
            mSerialPort = null;
        }
    }

    public void exit() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                System.exit(0);
            }
        }, 200);
    }


    public static boolean isGroupStart() {
        List<GroupInfo> groupInfos = GroupInfoSql.queryGroupList();
        if (groupInfos != null && groupInfos.size() > 0) {
            int size = groupInfos.size();
            for (int i = 0; i < size; i++) {
                if (groupInfos.get(i).getGroupStatus() > 0) {
                    Toast.makeText(getInstance(), getInstance().getString(R.string.group_open_error),Toast.LENGTH_LONG).show();
                    return true;
                }
            }
        }
        return false;
    }

    public static GroupInfo isGroupInfoStart() {
        List<GroupInfo> groupInfos = GroupInfoSql.queryGroupList();
        if (groupInfos != null && groupInfos.size() > 0) {
            int size = groupInfos.size();
            for (int i = 0; i < size; i++) {
                if (groupInfos.get(i).getGroupStatus() > 0) {
                    Toast.makeText(getInstance(), getInstance().getString(R.string.group_open_error),Toast.LENGTH_LONG).show();
                    return groupInfos.get(i);
                }
            }
        }
        return null;
    }


    /**
     * 判断网络是否连接
     */
    public static boolean isConnectNomarl() {
        return true;
//        try {
//            ConnectivityManager connectivityManager = (ConnectivityManager) getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
//            if (null == connectivityManager) {
//                return false;
//            }
//
//            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
//                if (capabilities != null) {
//                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
//                        return true;
//                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
//                        return true;
//                    }  else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)){
//                        return true;
//                    }
//                }
//            }else{
//                try {
//                    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
//                    if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
//                        return true;
//                    }
//                } catch (Exception e) {
//                    LogUtils.e(TAG,e.getMessage());
//                }
//            }
//
//            //根据Android版本判断网络是否可用：6.0以后系统提供API可用，6.0之前使用ping命令
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//                NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
//                if(null !=networkCapabilities){
//                    return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
//                }
//            } else {
//                Process ipProcess = null;
//                try {
//                    Runtime runtime = Runtime.getRuntime();
//                    if(null !=runtime){
//                        ipProcess = runtime.exec("ping -c 3 t.wuzhenpay.com");
//                    }
//
//                    if(null !=ipProcess){
//                        int exitValue = ipProcess.waitFor();
//                        LogUtils.i(TAG, "Process:" + exitValue);
//                        return (exitValue == 0);
//                    }
//                } catch (Exception e) {
//                    LogUtils.e(TAG, e.getMessage());
//                    return false;
//                }finally {
//                    if(null !=ipProcess){
//                        ipProcess.destroy();
//                    }
//                }
//            }
//
//            InputStream stream = null;
//            try {
//                URL url = new URL("https://www.baidu.com");
//                stream = url.openStream();
//                if (null != stream) {
//                    return true;
//                }
//            } catch (Exception e) {
//                return false;
//            }finally {
//                if(null !=stream){
//                    stream.close();
//                }
//            }
//            return false;
//        } catch (Exception e) {
//            return false;
//        }
    }
    /**
     * 用户登录的token信息
     */
    private static LoginRespone loginRespone;

    public static LoginRespone getLoginRespone() {
        if (loginRespone == null) {
            String info = SPUtils.getInstance().getString(SPUtils.SAVE_TOKEN_INFO);
            if (!TextUtils.isEmpty(info)) {
                loginRespone = GsonUtil.fromJson(info, LoginRespone.class);
            }
        }
        return loginRespone;
    }


    public static String getProjectId() {
        if (user == null){
            getUser();
        }
      return user.getProjectGroupId();
//        return "00003";
    }


    public ChatManager getChatManager() {
        return mChatManager;
    }


    public static boolean isWebLogin() {
        return webLogin;
    }

    public static void setWebLogin(boolean webLogin) {
        LogUtils.e("BaseApp", "深圳登陆状态"+webLogin);
        BaseApp.webLogin = webLogin;
    }


    public class AppBlockCanaryContext extends BlockCanaryContext {
        // 实现各种上下文，包括应用标示符，用户uid，网络类型，卡慢判断阙值，Log保存位置等

        /**
         * Implement in your project.
         *
         * @return Qualifier which can specify this installation, like version + flavor.
         */
        public String provideQualifier() {
            return "unknown";
        }

        /**
         * Implement in your project.
         *
         * @return user id
         */
        public String provideUid() {
            return "uid";
        }

        /**
         * Network type
         *
         * @return {@link String} like 2G, 3G, 4G, wifi, etc.
         */
        public String provideNetworkType() {
            return "unknown";
        }

        /**
         * Config monitor duration, after this time BlockCanary will stop, use
         * with {@code BlockCanary}'s isMonitorDurationEnd
         *
         * @return monitor last duration (in hour)
         */
        public int provideMonitorDuration() {
            return -1;
        }

        /**
         * Config block threshold (in millis), dispatch over this duration is regarded as a BLOCK. You may set it
         * from performance of device.
         *
         * @return threshold in mills
         */
        public int provideBlockThreshold() {
            return 300;
        }

        /**
         * Thread stack dump interval, use when block happens, BlockCanary will dump on main thread
         * stack according to current sample cycle.
         * <p>
         * Because the implementation mechanism of Looper, real dump interval would be longer than
         * the period specified here (especially when cpu is busier).
         * </p>
         *
         * @return dump interval (in millis)
         */
        public int provideDumpInterval() {
            return provideBlockThreshold();
        }

        /**
         * Path to save log, like "/blockcanary/", will save to sdcard if can.
         *
         * @return path of log files
         */
        public String providePath() {
            return "/blockcanary/";
        }

        /**
         * If need notification to notice block.
         *
         * @return true if need, else if not need.
         */
        public boolean displayNotification() {
            return true;
        }

        /**
         * Implement in your project, bundle files into a zip file.
         *
         * @param src  files before compress
         * @param dest files compressed
         * @return true if compression is successful
         */
        public boolean zip(File[] src, File dest) {
            return false;
        }

        /**
         * Implement in your project, bundled log files.
         *
         * @param zippedFile zipped file
         */
        public void upload(File zippedFile) {
            throw new UnsupportedOperationException();
        }


        /**
         * Packages that developer concern, by default it uses process name,
         * put high priority one in pre-order.
         *
         * @return null if simply concern only package with process name.
         */
        public List<String> concernPackages() {
            return null;
        }

        /**
         * Filter stack without any in concern package, used with @{code concernPackages}.
         *
         * @return true if filter, false it not.
         */
        public boolean filterNonConcernStack() {
            return false;
        }

        /**
         * Provide white list, entry in white list will not be shown in ui list.
         *
         * @return return null if you don't need white-list filter.
         */
        public List<String> provideWhiteList() {
            LinkedList<String> whiteList = new LinkedList<>();
            whiteList.add("org.chromium");
            return whiteList;
        }

        /**
         * Whether to delete files whose stack is in white list, used with white-list.
         *
         * @return true if delete, false it not.
         */
        public boolean deleteFilesInWhiteList() {
            return true;
        }

        /**
         * Block interceptor, developer may provide their own actions.
         */
        public void onBlock(Context context, BlockInfo blockInfo) {

        }
    }

}
