package com.example.backend.Validators;

import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

public interface CustomArgumentValidator<T> {
    public void validate(T target);

    default BindingResult getBindingResult(T target) {
        return new BeanPropertyBindingResult(target, target.getClass().getSimpleName());
    }
}
