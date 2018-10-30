package id.web.fahmikudo.meeting.mom.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "daftar_peserta")
@Data
public class DaftarPeserta implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id_daftar_peserta")
    private String id;

    @Column(nullable = false)
    private String nama;

    @Column(nullable = false)
    private String instansi;

    @Column(nullable = false)
    private String absen;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_meeting", nullable = false)
    private Meeting meeting;

    public DaftarPeserta() {
    }
}
