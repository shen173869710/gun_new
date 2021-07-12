package com.auto.di.guan.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import java.io.Serializable;
import java.util.Date;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Property;

@Entity
public class User implements Serializable {
    static final long serialVersionUID=1L;
    @Id(autoincrement = true)
    private Long id;
    /** 用户ID */
    @Property(nameInDb = "userId")
    private Long userId;
    @Property(nameInDb = "memberId")
    private Long memberId;

    /** 部门ID */
    @Property(nameInDb = "deptId")
    private Long deptId;

    /** 部门父ID */
    @Property(nameInDb = "parentId")
    private Long parentId;

    /** 角色ID */
    @Property(nameInDb = "roleId")
    private Long roleId;
    /** 登录名称 */
    @Property(nameInDb = "loginName")
    private String loginName;
    /** 用户名称 */
    @Property(nameInDb = "userName")
    private String userName;
    /** 用户类型（00系统用户,01注册用户,02=组长用户,03=管水员用户） */
    @Property(nameInDb = "userType")
    private String userType;
    /** 用户邮箱 */
    @Property(nameInDb = "email")
    private String email;
    /** 手机号码 */
    @Property(nameInDb = "phonenumber")
    private String phonenumber;
    /** 用户性别 */
    @Property(nameInDb = "sex")
    private String sex;
    /** 用户头像 */
    @Property(nameInDb = "avatar")
    private String avatar;
    /** 密码 */
    @Property(nameInDb = "password")
    private String password;
    /** 盐加密 */
    @Property(nameInDb = "salt")
    private String salt;
    /** 帐号状态（0正常 1停用） */
    @Property(nameInDb = "status")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    @Property(nameInDb = "delFlag")
    private String delFlag;

    /** 最后登陆IP */
    @Property(nameInDb = "loginIp")
    private String loginIp;
    /** 最后登陆时间 */
    @Property(nameInDb = "loginDate")
    private Date loginDate;
    // mac地址
    @Property(nameInDb = "mac")
    private String mac;
    // 项目id
    @Property(nameInDb = "projectId")
    private String projectId;
    // 项目组id
    @Property(nameInDb = "projectGroupId")
    private String projectGroupId;
    // 分干管(数量)
    @Property(nameInDb = "trunkPipeNum")
    private int trunkPipeNum;
    // 分干管(列)
    @Property(nameInDb = "trunkPipeMaxNum")
    private int trunkPipeMaxNum;
    // 出地桩(数量)
    @Property(nameInDb = "pileOutNum")
    private int pileOutNum;
    // 项目名称
    @Property(nameInDb = "projectName")
    private String projectName;
    // 项目描述
    @Property(nameInDb = "projectDesc")
    private String projectDesc;
    // 项目备注
    @Property(nameInDb = "projectRemarks")
    private String projectRemarks;
    // 项目经纬度
    @Property(nameInDb = "longitudeLatitude")
    private String longitudeLatitude;
    // 项目经纬度
    @Property(nameInDb = "parentLoginName")
    private String parentLoginName;
    @Generated(hash = 1899944912)
    public User(Long id, Long userId, Long memberId, Long deptId, Long parentId,
            Long roleId, String loginName, String userName, String userType,
            String email, String phonenumber, String sex, String avatar,
            String password, String salt, String status, String delFlag,
            String loginIp, Date loginDate, String mac, String projectId,
            String projectGroupId, int trunkPipeNum, int trunkPipeMaxNum,
            int pileOutNum, String projectName, String projectDesc,
            String projectRemarks, String longitudeLatitude,
            String parentLoginName) {
        this.id = id;
        this.userId = userId;
        this.memberId = memberId;
        this.deptId = deptId;
        this.parentId = parentId;
        this.roleId = roleId;
        this.loginName = loginName;
        this.userName = userName;
        this.userType = userType;
        this.email = email;
        this.phonenumber = phonenumber;
        this.sex = sex;
        this.avatar = avatar;
        this.password = password;
        this.salt = salt;
        this.status = status;
        this.delFlag = delFlag;
        this.loginIp = loginIp;
        this.loginDate = loginDate;
        this.mac = mac;
        this.projectId = projectId;
        this.projectGroupId = projectGroupId;
        this.trunkPipeNum = trunkPipeNum;
        this.trunkPipeMaxNum = trunkPipeMaxNum;
        this.pileOutNum = pileOutNum;
        this.projectName = projectName;
        this.projectDesc = projectDesc;
        this.projectRemarks = projectRemarks;
        this.longitudeLatitude = longitudeLatitude;
        this.parentLoginName = parentLoginName;
    }
    @Generated(hash = 586692638)
    public User() {
    }
    public Long getUserId() {
        return this.userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public Long getMemberId() {
        return this.memberId;
    }
    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }
    public Long getDeptId() {
        return this.deptId;
    }
    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }
    public Long getParentId() {
        return this.parentId;
    }
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
    public Long getRoleId() {
        return this.roleId;
    }
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
    public String getLoginName() {
        return this.loginName;
    }
    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
    public String getUserName() {
        return this.userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getUserType() {
        return this.userType;
    }
    public void setUserType(String userType) {
        this.userType = userType;
    }
    public String getEmail() {
        return this.email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhonenumber() {
        return this.phonenumber;
    }
    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }
    public String getSex() {
        return this.sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    public String getAvatar() {
        return this.avatar;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getSalt() {
        return this.salt;
    }
    public void setSalt(String salt) {
        this.salt = salt;
    }
    public String getStatus() {
        return this.status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getDelFlag() {
        return this.delFlag;
    }
    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }
    public String getLoginIp() {
        return this.loginIp;
    }
    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }
    public Date getLoginDate() {
        return this.loginDate;
    }
    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }
    public String getMac() {
        return this.mac;
    }
    public void setMac(String mac) {
        this.mac = mac;
    }
    public String getProjectId() {
        return this.projectId;
    }
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
    public String getProjectGroupId() {
        return this.projectGroupId;
    }
    public void setProjectGroupId(String projectGroupId) {
        this.projectGroupId = projectGroupId;
    }
    public int getTrunkPipeNum() {
        return this.trunkPipeNum;
    }
    public void setTrunkPipeNum(int trunkPipeNum) {
        this.trunkPipeNum = trunkPipeNum;
    }
    public int getTrunkPipeMaxNum() {
        return this.trunkPipeMaxNum;
    }
    public void setTrunkPipeMaxNum(int trunkPipeMaxNum) {
        this.trunkPipeMaxNum = trunkPipeMaxNum;
    }
    public int getPileOutNum() {
        return this.pileOutNum;
    }
    public void setPileOutNum(int pileOutNum) {
        this.pileOutNum = pileOutNum;
    }
    public String getProjectName() {
        return this.projectName;
    }
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    public String getProjectDesc() {
        return this.projectDesc;
    }
    public void setProjectDesc(String projectDesc) {
        this.projectDesc = projectDesc;
    }
    public String getProjectRemarks() {
        return this.projectRemarks;
    }
    public void setProjectRemarks(String projectRemarks) {
        this.projectRemarks = projectRemarks;
    }
    public String getLongitudeLatitude() {
        return this.longitudeLatitude;
    }
    public void setLongitudeLatitude(String longitudeLatitude) {
        this.longitudeLatitude = longitudeLatitude;
    }
    public String getParentLoginName() {
        return this.parentLoginName;
    }
    public void setParentLoginName(String parentLoginName) {
        this.parentLoginName = parentLoginName;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }


}
