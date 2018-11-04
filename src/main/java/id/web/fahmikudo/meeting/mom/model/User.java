package id.web.fahmikudo.meeting.mom.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import id.web.fahmikudo.meeting.mom.security.model.Role;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id_user")
    private String id;

    @Column(nullable = false)
    @NotEmpty
    @NotNull
    @Size(min = 2, max = 50)
    private String namaLengkap;

    @NotEmpty
    @NotNull
    @Size(min = 2, max = 20)
    private String username;

    @NotEmpty
    @NotNull
    @Size(min = 6, max = 20)
    private String password;

    @Column(nullable = false)
    @Size(min = 8, max = 14)
    private String noHp;

    @Column(nullable = false)
    private String jabatan;

    @OneToMany(mappedBy = "users", orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Meeting> meeting;

    @Enumerated(EnumType.STRING)
    private Role roles;

    private Boolean enabled;

    public User() {
    }

}
