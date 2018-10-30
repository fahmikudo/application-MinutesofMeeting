package id.web.fahmikudo.meeting.mom.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "pokok_bahasan")
@Data
public class PokokBahasan implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id_pokok_bahasan")
    private String id;

    @Column(nullable = false)
    private int nomor;

    @Column(nullable = false)
    private String pokokBahasan;

    @Column(nullable = false)
    private String status;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_meeting", nullable = false)
    private Meeting meeting;

    @OneToMany(mappedBy = "pokokBahasan", orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<DetailPokokBahasan> detailPokokBahasan;

    public PokokBahasan() {
    }
    
}
