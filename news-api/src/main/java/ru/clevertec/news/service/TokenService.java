package ru.clevertec.news.service;

import jakarta.validation.Valid;
import ru.clevertec.news.dto.request.AuthenticationData;
import ru.clevertec.news.dto.response.ResponseToken;

public interface TokenService {
    ResponseToken createToken(@Valid AuthenticationData authenticationData);
}
