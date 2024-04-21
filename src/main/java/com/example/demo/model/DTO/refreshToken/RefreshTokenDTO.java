package com.example.demo.model.DTO.refreshToken;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenDTO {
    @JsonProperty("refresh_token")
    private String refreshToken;
}
