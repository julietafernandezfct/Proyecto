package Controllers;

import org.springframework.web.bind.annotation.*;

import com.example.droneserver.Position;


@RestController
public class MoveController {

    @PostMapping("/move")
    public void Move(@RequestBody Position pos) {
        System.out.println("Movimiento recibido: " + pos);
    }
}
