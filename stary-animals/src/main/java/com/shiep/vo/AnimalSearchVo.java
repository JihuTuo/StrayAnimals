package com.shiep.vo;

import com.shiep.entity.*;
import lombok.Data;

import java.util.List;

@Data
public class AnimalSearchVo {
    private AnimalSearch animalSearch;
    private Location location;
    private String animalStatus;
    private Category category;
    private User user;
    private List<PhotoAnimalSearch> photoAnimalSearch;
}
