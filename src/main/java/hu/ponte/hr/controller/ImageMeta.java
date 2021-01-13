package hu.ponte.hr.controller;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * @author zoltan
 */
@Entity
@Table(name = "imagemeta")
@EntityListeners(AuditingEntityListener.class)
public class ImageMeta
{
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "uuid")
	private String id;
	private String name;
	private String mimeType;
	private long size;
	private String digitalSign;
	
	public ImageMeta(String name, String mimeType, long size) {
		super();
		this.name = name;
		this.mimeType = mimeType;
		this.size = size;
	}
	
	public ImageMeta(String id, String name, String mimeType, long size, String digitalSign) {
		super();
		this.id = id;
		this.name = name;
		this.mimeType = mimeType;
		this.size = size;
		this.digitalSign = digitalSign;
	}

	public ImageMeta() {
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMimeType() {
		return mimeType;
	}
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public String getDigitalSign() {
		return digitalSign;
	}
	public void setDigitalSign(String digitalSign) {
		this.digitalSign = digitalSign;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ImageMeta [id=").append(id)
			.append(", name=").append(name)
			.append(", mimeType=").append(mimeType)
			.append(", size=").append(size)
			.append(", digitalSign=").append(digitalSign)
			.append("]");
		return builder.toString();
	}
	
}
