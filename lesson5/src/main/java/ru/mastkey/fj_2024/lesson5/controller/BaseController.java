package ru.mastkey.fj_2024.lesson5.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

public interface BaseController<R, A> {

    @GetMapping("/{id}")
    public ResponseEntity<A> getById(@PathVariable UUID id);

    @GetMapping
    public ResponseEntity<List<A>> getAll();

    @PostMapping
    public ResponseEntity<A> create(@RequestBody R request);

    @PutMapping("/{id}")
    public ResponseEntity<A> update(@PathVariable UUID id, @RequestBody R request);

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id);
}
