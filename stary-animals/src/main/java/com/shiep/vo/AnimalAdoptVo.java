package com.shiep.vo;

import com.shiep.entity.AnimalAdopt;
import com.shiep.entity.AnimalStatus;
import com.shiep.entity.PhotoAnimalAdopt;
import com.shiep.entity.User;
import lombok.Data;

import java.util.List;

@Data
public class AnimalAdoptVo {
    private AnimalAdopt animalAdopt;
    private Location location;
    private Category category;
    private User user;
    private List<PhotoAnimalAdopt> photoAnimalAdopts;
    private AnimalStatus animalStatus;
}
