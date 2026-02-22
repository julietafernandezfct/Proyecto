package com.example.droneserver;

import org.springframework.web.bind.annotation.*;

@RestController
public class MoveController {

    @PostMapping("/move")
    public void Move(@RequestBody Position pos) {
        System.out.println("Movimiento recibido: " + pos);
    }
}
