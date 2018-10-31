package id.web.fahmikudo.meeting.mom.model;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "meeting")
@Data
public class Meeting implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id_meeting")
    private String id;

    @Column(nullable = false)
    private String agenda;

    @Column(nullable = false, name = "tanggal_waktu")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date tanggalWaktu;

    @Column(nullable = false)
    private String lokasi;

    @Column(nullable = false)
    private String kesimpulan;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_project", nullable = false)
    private Project project;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_notulen", nullable = false)
    private User user;

    @OneToMany(mappedBy = "meeting", orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<PokokBahasan> pokokBahasan;

    @OneToMany(mappedBy = "meeting", fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnore
    private List<DaftarPeserta> daftarPeserta;

    @OneToMany(mappedBy = "meeting", fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnore
    private List<Gallery> gallery;

    public Meeting() {
    }


}
