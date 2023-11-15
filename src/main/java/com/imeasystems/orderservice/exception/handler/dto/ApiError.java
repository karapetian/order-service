package com.imeasystems.orderservice.exception.handler.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {

  private int code;

  private String message;

  private List<String> errors;
}