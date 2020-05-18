package com.shiep.vo;

import com.shiep.entity.AnimalSearchComment;
import com.shiep.entity.User;
import lombok.Data;

@Data
public class AnimalSearchCommentVo {
    private AnimalSearchComment searchComment;
    private User user;
}
