package id.web.fahmikudo.meeting.mom.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "notulen")
@Data
public class Notulen implements Serializable {

	@Id
	@GeneratedValue(generator = "uuid", strategy = GenerationType.AUTO)
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(name = "id_notulen")
	private String id;

	@Column(nullable = false)
	private String nama;

	@Column(nullable = false)
	@Size(min = 8, max = 14)
	private String noHp;

	@Column(nullable = false)
	private String jabatan;

	@OneToMany(mappedBy = "notulen", orphanRemoval = true, fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Meeting> meeting;

	public Notulen() {
	}

}
