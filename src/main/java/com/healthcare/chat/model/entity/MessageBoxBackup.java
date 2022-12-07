package com.healthcare.chat.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.healthcare.model.entity.Admin;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;
import java.util.Date;

/**
 * @author tungn
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Data
@DynamicInsert
@DynamicUpdate
@Table(name = "message_box_backup")
public class MessageBoxBackup implements java.io.Serializable {

	@Id
	@Column(name = "id", nullable = false)
	private long id;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "from_user", referencedColumnName = "id", nullable = false)
	private Admin fromUser;

	@Basic
	@Column(name = "to_user", nullable = false)
	private Long toUser;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "group_id", referencedColumnName = "id", nullable = true)
	private ChatGroup group;

	@Basic
	@Column(name = "message", nullable = false)
	private String message;

	@Basic
	@Column(name = "created_at", nullable = false)
	private Date createdAt;

	@Basic
	@Column(name = "updated_at", nullable = false)
	private Date updatedAt;

	@Basic
	@Column(name = "status", nullable = false)
	private long status;

	public MessageBoxBackup(MessageBox messageBox) {
		BeanUtils.copyProperties(messageBox, this);
	}

	public MessageBoxBackup() {

	}

}
