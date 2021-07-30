package com.jxx.auth.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.sql.Timestamp;

@TableName("account")
@Data
public class AccountDO implements Serializable {
    @Id
    @TableId(type = IdType.AUTO)
    private Long id;

    private String device;
    private Timestamp createTime;
    private Timestamp lastLoginTime;
    private String username;
    private String phone;
    private String password;
    private String salt;
    private String roleName = "user";
}
