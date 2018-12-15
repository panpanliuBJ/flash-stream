package com.finley.flash.stream.domain;

import java.util.Date;
import lombok.Data;


@Data
public class BaseUserShare {

    private Long userId;

    private Date createTime;

    private Long share;
}
