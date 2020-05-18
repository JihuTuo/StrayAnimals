package com.shiep.vo;

import com.shiep.entity.AnimalAdoptComment;
import com.shiep.entity.User;
import lombok.Data;

@Data
public class AnimalAdoptCommentVo {
    private AnimalAdoptComment adoptComment;
    private User user;
}
