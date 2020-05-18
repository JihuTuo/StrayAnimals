package com.shiep.service;

import com.shiep.entity.PhotoAnimalAdopt;
import com.shiep.entity.PhotoAnimalSearch;

public interface IUploadPhotoService {
    boolean saveAdoptPhoto(PhotoAnimalAdopt adopt);

    boolean saveSearchPhoto(PhotoAnimalSearch search);
}
