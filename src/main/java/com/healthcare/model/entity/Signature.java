package com.healthcare.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by inimn on 11/10/2017.
 */
@Entity
@Table(name = "signature")
public @Data class Signature implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "signed_by")
	private String signed_by;

	@Column(name = "signed_date_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date signedDateTime;

	@Column(name = "type", nullable = false)
	private String type;

	@OneToOne
	@JoinColumn(name = "signature_file_id")
	private Document signatureFile;
	
	@Column(name = "updated_at")
	private Timestamp updatedAt;
}
