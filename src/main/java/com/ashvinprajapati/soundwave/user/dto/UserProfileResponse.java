package com.ashvinprajapati.soundwave.user.dto;

import com.ashvinprajapati.soundwave.auth.entity.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfileResponse {
    private long id;
    private String fullName;
    private String email;
    private Role role;

}
