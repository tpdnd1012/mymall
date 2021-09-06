package com.megait.mymall.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("ALBUM")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Album extends Item{
    String artist;
}
