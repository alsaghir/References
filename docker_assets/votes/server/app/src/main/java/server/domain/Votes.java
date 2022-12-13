package server.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.TableGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;


@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "APP_VOTES")
public class Votes implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "Votes_SEQ_GENERATOR")
    @TableGenerator(
            name = "Votes_SEQ_GENERATOR",
            table = "APP_SEQ_GENERATOR",
            pkColumnName = "SEQ_NAME",
            pkColumnValue = "Votes_SEQ_PK",
            valueColumnName = "SEQ_VALUE",
            initialValue = 1,
            allocationSize = 1)
    private Integer id;

    private int yes;
    private int no;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Votes current = (Votes) o;

        return getId() != null && getId().equals(current.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
