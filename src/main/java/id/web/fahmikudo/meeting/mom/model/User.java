package id.web.fahmikudo.meeting.mom.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

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
    @Size(min = 6, max = 255)
    private String password;

    @Column(nullable = false)
    @Size(min = 8, max = 14)
    private String noHp;

    @Column(nullable = false)
    private String jabatan;

    @Column(nullable = false)
    private Boolean enabled;

    @OneToMany(mappedBy = "users", orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Meeting> meeting;

    @ManyToMany
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "id_user"),
            inverseJoinColumns = @JoinColumn(name = "id_role")
    )
    private Set<Role> roles;

    public User() {
    }

}
