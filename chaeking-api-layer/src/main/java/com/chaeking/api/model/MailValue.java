package com.chaeking.api.model;

import javax.validation.constraints.Email;

public class MailValue {

    public record MailRequest(
            String email,
            String title,
            String content
    ){}

    public record MailTemporaryPasswordRequest(
            @Email String email
    ){}

}
