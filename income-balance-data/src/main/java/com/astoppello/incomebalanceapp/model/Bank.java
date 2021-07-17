package com.astoppello.incomebalanceapp.model;

import lombok.*;
import org.springframework.lang.NonNull;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;
import java.util.StringJoiner;

/** Created by @author stopp on 21/11/2020 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "banks")
public class Bank extends AbstractBaseEntity {

  @NonNull
  private String name;

  @Builder
  public Bank(Long id, String name) {
    super(id);
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Bank)) return false;
    if (!super.equals(o)) return false;
    Bank bank = (Bank) o;
    return Objects.equals(name, bank.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), name);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", Bank.class.getSimpleName() + "[", "]")
        .merge(super.getStringJoiner())
        .add("name=" + name)
        .toString();
  }
}
