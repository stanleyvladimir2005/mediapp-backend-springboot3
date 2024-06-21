package com.mitocode.controller;

import com.mitocode.dto.MenuDTO;
import com.mitocode.service.IMenuService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;
import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/menus")
@RequiredArgsConstructor
public class MenuController {

    private final ModelMapper modelMapper;
    private final IMenuService service;

    @PostMapping("/user")
    public ResponseEntity<List<MenuDTO>> getMenusByUser(@RequestBody String username) {
        val menus = service.getMenusByUsername(username);
        val menusDTO = menus.stream()
                .map(m -> {
                    m.setRoles(new ArrayList<>()); //Necesario por nativeQuery y devuelve PersistenBag
                    return modelMapper.map(m, MenuDTO.class);
                }).collect(toList());
        return new ResponseEntity<>(menusDTO, HttpStatus.OK);
    }
}
