package dev.clownsinformatics.tiendajava.rest.auth.services.authentication;

import dev.clownsinformatics.tiendajava.rest.auth.dto.JwtAuthResponse;
import dev.clownsinformatics.tiendajava.rest.auth.dto.UserSignInRequest;
import dev.clownsinformatics.tiendajava.rest.auth.dto.UserSignUpRequest;

public interface AuthenticationService {
    JwtAuthResponse signUp(UserSignUpRequest request);

    JwtAuthResponse signIn(UserSignInRequest request);
}