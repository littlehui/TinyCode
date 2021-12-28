package org.tinycode.web.app.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO
 *
 * @author littlehui
 * @version 1.0
 * @date 2021/12/08 16:27
 */
@RestController
@RequestMapping("/tinycode")
public class TinyCodeController {

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity selectAll() {
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity add() {
        return ResponseEntity.ok().build();
    }
}
