package com.github.alsaghir.server.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import java.io.Serial;
import java.io.Serializable;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "APP_VALUE")
public class Value implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "Value_SEQ_GENERATOR")
    @TableGenerator(
            name = "Value_SEQ_GENERATOR",
            table = "APP_SEQ_GENERATOR",
            pkColumnName = "SEQ_NAME",
            pkColumnValue = "VALUE_SEQ_PK",
            valueColumnName = "SEQ_VALUE",
            initialValue = 1,
            allocationSize = 1)
    private Integer id;

    private String value;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Value current = (Value) o;

        return getId() != null && getId().equals(current.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
