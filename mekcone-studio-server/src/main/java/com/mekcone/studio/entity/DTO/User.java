package com.mekcone.studio.entity.DTO;

import lombok.Data;

/*
 * Author: Tenton Lien
 * Date: 9/20/2020
 */

@Data
public class User {
    private String id;
    private String username;
    private String password;
    private String type;
}
