package com.megait.mymall.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("BOOK")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Book extends Item{
    String isbn;
}
