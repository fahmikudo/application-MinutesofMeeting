package id.web.fahmikudo.meeting.mom.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "project")
@Data
public class Project implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id_project")
    private String id;

    @Column(nullable = false)
    @NotNull
    @NotEmpty
    @Size(min=2, max=255)
    private String namaProject;

    @OneToMany(mappedBy = "project", orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Meeting> meeting;

    public Project() {
    }

}
