package xyz.silverspoon;

public class Constants {
    // Controller 相关的字符串信息以及错误码
    public static final String FILE_EXCEPTION = "文件保存出错";
    public static final String BAD_PASSWORDORUSERNAME = "用戶名或密码错误";
    public static final String EXIST_USER = "用户名已存在";
    public static final String NOTEXIST_USER = "用户不存在";
    public static final String DUPLICATE_REQUEST = "请求已发送";
    public static final String IS_FRIEND = "您与该用户已经是好友了.";
    public static final String IM_REQUESTID = "requestID";
    public static final String IM_RECEIVERID = "receiverID";
    public static final int CODE_NOT_FOUND = 404;
    public static final int CODE_EXIST = 501;

    // service 关于数据库的表字段
    // All collections's uuid
    public static final String IM_UUID = "UUID";
    // ImMessage collections
    public static final String MESSAGE_SENDERID = "SenderID";
    public static final String MESSAGE_RECEIVERID = "ReceiverID";
    public static final String MESSAGE_TIME = "time";
    public static final String MESSAGE_STATUS = "status";
    // ImUserRelationRequest collections
    public static final String REQUEST_STATUS = "status";
    public static final int REQUEST_DEFAULT_STATUS = 0;
    public static final int REQUEST_ACCEPT_STATUS = 1;
    public static final int REQUEST_REJECT_STATUS = 2;
    public static final String REQUEST_ACCEPTID = "acceptID";
    // ImUserRelation collections
    public static final String RELATION_USER1 = "user1";
    public static final String RELATION_USER2 = "user2";
    // ImUser collections
    public static final String USER_AVATAR = "avatar";
    public static final String USER_USERNAME = "username";
    // ImNotification collections
    public static final int TYPE_SYSTEM=0;
    public static final int TYPE_REQUEST = 1;
    public static final int NOTIFICATION_UNREAD = 0;
    public static final int NOTIFICATION_READ = 1;
    public static final String NOTIFICATION_USERID = "userID";
    public static final String NOTIFICATION_STATUS = "status";


    // 文件格式后缀
    public static final String JPG = ".jpg";
    public static final String PNG = ".png";
    public static final String JPEG = ".jpeg";
}
