package com.gmarussi.backend.controller;

import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class RootController {

    @GetMapping("/")
    public ResponseEntity<Void> redirectToDocs(HttpServletResponse response) throws IOException {
        response.sendRedirect("/swagger-ui/index.html");
        return ResponseEntity.status(HttpStatus.FOUND).build();
    }
}
