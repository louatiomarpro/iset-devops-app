package tn.iset.sfax.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Bonjour depuis le Backend — ISET Sfax DevOps ! Automatique";
    }

    @GetMapping("/status")
    public String status() {
        return "{ \"status\": \"UP\", \"version\": \"1.0.0\" }";
    }
}
