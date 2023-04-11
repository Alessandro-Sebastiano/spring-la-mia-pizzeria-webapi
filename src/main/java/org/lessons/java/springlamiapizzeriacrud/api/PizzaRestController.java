package org.lessons.java.springlamiapizzeriacrud.api;

import jakarta.validation.Valid;
import org.lessons.java.springlamiapizzeriacrud.model.Pizza;
import org.lessons.java.springlamiapizzeriacrud.repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/pizzas")
public class PizzaRestController {

    @Autowired
    private PizzaRepository repo;

    @GetMapping
    public List<Pizza> index(@RequestParam(name = "input") Optional<String> keyword) {

        List<Pizza> result;

        if (keyword.isEmpty()) {
            result = repo.findAll();
        } else {
            result = repo.findByNameContainingIgnoreCase(keyword.get());
        }

        return result;
    }

    @GetMapping("/{id}")
    public Pizza show(@PathVariable Integer id) {
        Optional<Pizza> result = repo.findById(id);
        if (result.isPresent()) {
            return result.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public Pizza create(@Valid @RequestBody Pizza formPizza) {

        Pizza persistPizza = new Pizza();
        persistPizza.setName(formPizza.getName());
        persistPizza.setDescription(formPizza.getDescription());
        persistPizza.setPrice(formPizza.getPrice());

        return repo.save(persistPizza);
    }


    @PutMapping("/{id}")
    public Pizza update(@PathVariable Integer id, @Valid @RequestBody Pizza formPizza) {

        Pizza updatedPizza = repo.getById(id);
        updatedPizza.setName(formPizza.getName());
        updatedPizza.setDescription(formPizza.getDescription());
        updatedPizza.setPrice(formPizza.getPrice());

        return repo.save(updatedPizza);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        Optional<Pizza> result = repo.findById(id);
        if (result.isPresent()) {
            repo.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

}
