package id.web.fahmikudo.meeting.mom.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "user")
@Data
public class User implements Serializable {

	@Id
	@GeneratedValue(generator = "uuid", strategy = GenerationType.AUTO)
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(name = "id_user")
	private String id;

	@Column(nullable = false)
	@NotEmpty @NotNull
	@Size(min = 2, max = 50)
	private String namaLengkap;

	@NotEmpty @NotNull
	@Size(min = 2, max = 20)
	private String username;

	@NotEmpty @NotNull
	@Size(min = 6, max = 20)
	private String password;

	@Column(nullable = false)
	@Size(min = 8, max = 14)
	private String noHp;

	@Column(nullable = false)
	private String jabatan;

	@OneToMany(mappedBy = "user", orphanRemoval = true, fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Meeting> meeting;

	public User() {
	}

}
