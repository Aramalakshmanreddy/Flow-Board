package com.project.authentication.security;

import com.project.authentication.domain.User;

public interface ISecurityTokenGenerate
{
    String createToken(User user);
}
