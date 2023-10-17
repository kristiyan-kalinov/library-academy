package com.kodar.academy.Library.controller;

import com.kodar.academy.Library.model.dto.rent.RentResponseDTO;
import com.kodar.academy.Library.service.RentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RentController {

    private final RentService rentService;

    @Autowired
    public RentController(RentService rentService) {
        this.rentService = rentService;
    }

    @GetMapping("/rents")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<RentResponseDTO>> getAllRents() {
        return new ResponseEntity<>(rentService.getAllRents(), HttpStatus.OK);
    }

    @GetMapping("/rents/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or @rentService.checkAuth(#id) == authentication.name")
    public ResponseEntity<RentResponseDTO> getRentById(@PathVariable("id") int id) {
        return new ResponseEntity<>(rentService.getRentById(id), HttpStatus.OK);
    }

}
