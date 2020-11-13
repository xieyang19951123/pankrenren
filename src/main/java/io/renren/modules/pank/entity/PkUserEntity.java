package io.renren.modules.pank.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户管理
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-08-26 22:42:52
 */
@Data
@TableName("pk_user")
public class PkUserEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;

	private String openid;

	private String headimgurl;

	private String nickname;

	private  Date createTime;

}
