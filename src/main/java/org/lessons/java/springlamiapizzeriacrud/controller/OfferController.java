package org.lessons.java.springlamiapizzeriacrud.controller;

import jakarta.validation.Valid;
import org.lessons.java.springlamiapizzeriacrud.model.AlertMessages;
import org.lessons.java.springlamiapizzeriacrud.model.Offer;
import org.lessons.java.springlamiapizzeriacrud.model.Pizza;
import org.lessons.java.springlamiapizzeriacrud.repository.OfferRepository;
import org.lessons.java.springlamiapizzeriacrud.repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequestMapping("/offers")
public class OfferController {

    @Autowired
    private OfferRepository repo;

    @Autowired
    private PizzaRepository pizzaRepo;

    @GetMapping("/create")
    public String create(@RequestParam(name = "pizzaId") Optional<Integer> id, Model model) {
        Offer offer = new Offer();
        offer.setStartDate(LocalDate.now());
        if (id.isPresent()) {
            Pizza pizza = pizzaRepo.getById(id.get());
            offer.setPizza(pizza);
        }
        model.addAttribute("offer", offer);
        return "/offers/create";
    }

    @PostMapping("/create")
    public String store(@Valid @ModelAttribute Offer formOffer, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "/offers/create";
        }
        repo.save(formOffer);

        redirectAttributes.addFlashAttribute("message", new AlertMessages(AlertMessages.typeAlert.SUCCESS, "Created offer"));

        return "redirect:/pizzas/" + formOffer.getPizza().getId();
    }


    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        Optional<Offer> result = repo.findById(id);
        if (result.isPresent()) {
            model.addAttribute("offer", result.get());
            return "/offers/edit";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pizza with id " + id + " not found");
        }
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Integer id, @Valid @ModelAttribute("offer") Offer formOffer, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "/offers/edit";
        }

        repo.save(formOffer);
        redirectAttributes.addFlashAttribute("message", new AlertMessages(AlertMessages.typeAlert.SUCCESS, "Edited offer"));
        return "redirect:/pizzas/" + formOffer.getPizza().getId();
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        Optional<Offer> result = repo.findById(id);
        if (result.isPresent()) {
            repo.deleteById(id);
            redirectAttributes.addFlashAttribute("message", new AlertMessages(AlertMessages.typeAlert.SUCCESS, "Delete success"));
        } else {
            redirectAttributes.addFlashAttribute("message", new AlertMessages(AlertMessages.typeAlert.ERROR, "Not found"));
        }
        return "redirect:/pizzas/" + result.get().getPizza().getId();
    }

}

