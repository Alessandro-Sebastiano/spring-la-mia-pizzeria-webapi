package org.lessons.java.springlamiapizzeriacrud.controller;

import jakarta.validation.Valid;
import org.lessons.java.springlamiapizzeriacrud.model.AlertMessages;
import org.lessons.java.springlamiapizzeriacrud.model.Pizza;
import org.lessons.java.springlamiapizzeriacrud.repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/pizzas")
public class PizzaController {

    @Autowired
    private PizzaRepository repo;

    @GetMapping
    public String index(Model model, @RequestParam(name = "input") Optional<String> keyword) {

        List<Pizza> result;

        if (keyword.isEmpty()) {
            result = repo.findAll();
        } else {
            result = repo.findByNameContainingIgnoreCase(keyword.get());
        }
        model.addAttribute("list", result);
        return "/pizzas/index";
    }

    @GetMapping("/{pizzaId}")
    public String show(@PathVariable("pizzaId") Integer id, Model model) {
        Optional<Pizza> result = repo.findById(id);
        if (result.isPresent()) {
            model.addAttribute("pizza", result.get());
            return "/pizzas/show";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pizza with id " + id + " not found");
        }
    }


    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("pizza", new Pizza());
        return "/pizzas/create";
    }

    @PostMapping("/create")
    public String store(@Valid @ModelAttribute("pizza") Pizza formPizza, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "/pizzas/create";
        }

        Pizza persistPizza = new Pizza();
        persistPizza.setName(formPizza.getName());
        persistPizza.setDescription(formPizza.getDescription());
        persistPizza.setPrice(formPizza.getPrice());

        repo.save(persistPizza);
        redirectAttributes.addFlashAttribute("message", new AlertMessages(AlertMessages.typeAlert.SUCCESS, "Created pizza"));
        return "redirect:/pizzas";
    }


    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        Optional<Pizza> result = repo.findById(id);
        if (result.isPresent()) {
            model.addAttribute("pizza", result.get());
            return "/pizzas/edit";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pizza with id " + id + " not found");
        }
    }


    @PostMapping("/edit/{id}")
    public String update(@PathVariable Integer id, @Valid @ModelAttribute("pizza") Pizza formPizza, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "/pizzas/edit";
        }

        Pizza updatedPizza = repo.getById(id);
        updatedPizza.setName(formPizza.getName());
        updatedPizza.setDescription(formPizza.getDescription());
        updatedPizza.setPrice(formPizza.getPrice());

        repo.save(updatedPizza);
        redirectAttributes.addFlashAttribute("message", new AlertMessages(AlertMessages.typeAlert.SUCCESS, "Edited pizza"));
        return "redirect:/pizzas/" + id;

    }


    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        Optional result = repo.findById(id);
        if (result.isPresent()) {
            repo.deleteById(id);
            redirectAttributes.addFlashAttribute("message", new AlertMessages(AlertMessages.typeAlert.SUCCESS, "Delete success"));
        } else {
            redirectAttributes.addFlashAttribute("message", new AlertMessages(AlertMessages.typeAlert.ERROR, "Not found"));
        }
        return "redirect:/pizzas";
    }


}
