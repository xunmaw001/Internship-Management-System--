package com.entity.view;

import org.apache.tools.ant.util.DateUtils;
import com.annotation.ColumnInfo;
import com.entity.ZhaopinEntity;
import com.baomidou.mybatisplus.annotations.TableName;
import org.apache.commons.beanutils.BeanUtils;
import java.lang.reflect.InvocationTargetException;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import com.utils.DateUtil;

/**
* 职位招聘
* 后端返回视图实体辅助类
* （通常后端关联的表或者自定义的字段需要返回使用）
*/
@TableName("zhaopin")
public class ZhaopinView extends ZhaopinEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	//当前表
	/**
	* 招聘岗位的值
	*/
	@ColumnInfo(comment="招聘岗位的字典表值",type="varchar(200)")
	private String zhaopinValue;
	/**
	* 招聘类型的值
	*/
	@ColumnInfo(comment="招聘类型的字典表值",type="varchar(200)")
	private String leixingValue;
	/**
	* 是否上架的值
	*/
	@ColumnInfo(comment="是否上架的字典表值",type="varchar(200)")
	private String shangxiaValue;

	//级联表 公司
		/**
		* 公司名称
		*/

		@ColumnInfo(comment="公司名称",type="varchar(200)")
		private String gongsiName;
		/**
		* 公司类型
		*/
		@ColumnInfo(comment="公司类型",type="int(11)")
		private Integer gongsiTypes;
			/**
			* 公司类型的值
			*/
			@ColumnInfo(comment="公司类型的字典表值",type="varchar(200)")
			private String gongsiValue;
		/**
		* 联系方式
		*/

		@ColumnInfo(comment="联系方式",type="varchar(200)")
		private String gongsiPhone;
		/**
		* 邮箱
		*/

		@ColumnInfo(comment="邮箱",type="varchar(200)")
		private String gongsiEmail;
		/**
		* 公司logo
		*/

		@ColumnInfo(comment="公司logo",type="varchar(200)")
		private String gongsiPhoto;
		/**
		* 公司成立时间
		*/
		@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
		@DateTimeFormat
		@ColumnInfo(comment="公司成立时间",type="timestamp")
		private Date gongsiChenglishijianTime;
		/**
		* 公司介绍
		*/

		@ColumnInfo(comment="公司介绍",type="text")
		private String gongsiContent;
		/**
		* 逻辑删除
		*/

		@ColumnInfo(comment="逻辑删除",type="int(11)")
		private Integer gongsiDelete;



	public ZhaopinView() {

	}

	public ZhaopinView(ZhaopinEntity zhaopinEntity) {
		try {
			BeanUtils.copyProperties(this, zhaopinEntity);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	//当前表的
	/**
	* 获取： 招聘岗位的值
	*/
	public String getZhaopinValue() {
		return zhaopinValue;
	}
	/**
	* 设置： 招聘岗位的值
	*/
	public void setZhaopinValue(String zhaopinValue) {
		this.zhaopinValue = zhaopinValue;
	}
	//当前表的
	/**
	* 获取： 招聘类型的值
	*/
	public String getLeixingValue() {
		return leixingValue;
	}
	/**
	* 设置： 招聘类型的值
	*/
	public void setLeixingValue(String leixingValue) {
		this.leixingValue = leixingValue;
	}
	//当前表的
	/**
	* 获取： 是否上架的值
	*/
	public String getShangxiaValue() {
		return shangxiaValue;
	}
	/**
	* 设置： 是否上架的值
	*/
	public void setShangxiaValue(String shangxiaValue) {
		this.shangxiaValue = shangxiaValue;
	}


	//级联表的get和set 公司

		/**
		* 获取： 公司名称
		*/
		public String getGongsiName() {
			return gongsiName;
		}
		/**
		* 设置： 公司名称
		*/
		public void setGongsiName(String gongsiName) {
			this.gongsiName = gongsiName;
		}
		/**
		* 获取： 公司类型
		*/
		public Integer getGongsiTypes() {
			return gongsiTypes;
		}
		/**
		* 设置： 公司类型
		*/
		public void setGongsiTypes(Integer gongsiTypes) {
			this.gongsiTypes = gongsiTypes;
		}


			/**
			* 获取： 公司类型的值
			*/
			public String getGongsiValue() {
				return gongsiValue;
			}
			/**
			* 设置： 公司类型的值
			*/
			public void setGongsiValue(String gongsiValue) {
				this.gongsiValue = gongsiValue;
			}

		/**
		* 获取： 联系方式
		*/
		public String getGongsiPhone() {
			return gongsiPhone;
		}
		/**
		* 设置： 联系方式
		*/
		public void setGongsiPhone(String gongsiPhone) {
			this.gongsiPhone = gongsiPhone;
		}

		/**
		* 获取： 邮箱
		*/
		public String getGongsiEmail() {
			return gongsiEmail;
		}
		/**
		* 设置： 邮箱
		*/
		public void setGongsiEmail(String gongsiEmail) {
			this.gongsiEmail = gongsiEmail;
		}

		/**
		* 获取： 公司logo
		*/
		public String getGongsiPhoto() {
			return gongsiPhoto;
		}
		/**
		* 设置： 公司logo
		*/
		public void setGongsiPhoto(String gongsiPhoto) {
			this.gongsiPhoto = gongsiPhoto;
		}

		/**
		* 获取： 公司成立时间
		*/
		public Date getGongsiChenglishijianTime() {
			return gongsiChenglishijianTime;
		}
		/**
		* 设置： 公司成立时间
		*/
		public void setGongsiChenglishijianTime(Date gongsiChenglishijianTime) {
			this.gongsiChenglishijianTime = gongsiChenglishijianTime;
		}

		/**
		* 获取： 公司介绍
		*/
		public String getGongsiContent() {
			return gongsiContent;
		}
		/**
		* 设置： 公司介绍
		*/
		public void setGongsiContent(String gongsiContent) {
			this.gongsiContent = gongsiContent;
		}

		/**
		* 获取： 逻辑删除
		*/
		public Integer getGongsiDelete() {
			return gongsiDelete;
		}
		/**
		* 设置： 逻辑删除
		*/
		public void setGongsiDelete(Integer gongsiDelete) {
			this.gongsiDelete = gongsiDelete;
		}


	@Override
	public String toString() {
		return "ZhaopinView{" +
			", zhaopinValue=" + zhaopinValue +
			", leixingValue=" + leixingValue +
			", shangxiaValue=" + shangxiaValue +
			", gongsiName=" + gongsiName +
			", gongsiPhone=" + gongsiPhone +
			", gongsiEmail=" + gongsiEmail +
			", gongsiPhoto=" + gongsiPhoto +
			", gongsiChenglishijianTime=" + DateUtil.convertString(gongsiChenglishijianTime,"yyyy-MM-dd") +
			", gongsiContent=" + gongsiContent +
			", gongsiDelete=" + gongsiDelete +
			"} " + super.toString();
	}
}
