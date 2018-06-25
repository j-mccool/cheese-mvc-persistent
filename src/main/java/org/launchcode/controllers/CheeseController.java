package org.launchcode.controllers;

import org.launchcode.models.Category;
import org.launchcode.models.Cheese;
import org.launchcode.models.Menu;
import org.launchcode.models.data.CategoryDao;
import org.launchcode.models.data.CheeseDao;
import org.launchcode.models.data.MenuDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by LaunchCode
 */
@Controller
@RequestMapping("cheese")
public class CheeseController {

    @Autowired
    private CheeseDao cheeseDao;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private MenuDao menuDao;

    // Request path: /cheese
    @RequestMapping(value = "")
    public String index(Model model) {

        model.addAttribute("cheeses", cheeseDao.findAll());
        model.addAttribute("title", "My Cheeses");

        return "cheese/index";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String displayAddCheeseForm(Model model) {
        model.addAttribute("title", "Add Cheese");
        model.addAttribute(new Cheese());
        model.addAttribute("categories", categoryDao.findAll());
        return "cheese/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAddCheeseForm(@RequestParam int categoryId, Model model, @ModelAttribute  @Valid Cheese newCheese,
                                       Errors errors) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Cheese");
            model.addAttribute("categories", categoryDao.findAll());
            return "cheese/add";
        }
        Category cat = categoryDao.findOne(categoryId);
        newCheese.setCategory(cat);
        cheeseDao.save(newCheese);
        return "redirect:";
    }

    @RequestMapping(value = "remove", method = RequestMethod.GET)
    public String displayRemoveCheeseForm(Model model) {
        model.addAttribute("cheeses", cheeseDao.findAll());
        model.addAttribute("title", "Remove Cheese");
        return "cheese/remove";
    }

    @RequestMapping(value = "remove", method = RequestMethod.POST)
    public String processRemoveCheeseForm(@RequestParam int[] cheeseIds) {

        Iterable<Menu> m = menuDao.findAll();

        for (int cheeseId : cheeseIds) {
            for (Menu i : m) {
                if (i.getCheeses().contains(cheeseDao.findOne(cheeseId))) {
                    i.removeItem(cheeseDao.findOne(cheeseId));
                }
            }
            cheeseDao.delete(cheeseId);
        }

        return "redirect:";
    }

    @RequestMapping(value= "category/{categoryId}", method = RequestMethod.GET)
    public String displayByCategory(@PathVariable("categoryId") int categoryId, Model model) {

        model.addAttribute("title", "Cheeses by Category");
        model.addAttribute("cheeses", cheeseDao.findByCategory_Id(categoryId));

        return "cheese/index";
    }

    @RequestMapping(value="edit/{cheeseId}", method = RequestMethod.GET)
    public String displayEditForm(Model model, @PathVariable int cheeseId) {
        model.addAttribute("title", "Edit Cheese");
        Cheese c = cheeseDao.findOne(cheeseId);
        model.addAttribute("cheese", c);

        model.addAttribute("categories", categoryDao.findAll());

        return "cheese/edit";
    }

    @RequestMapping(value="edit", method = RequestMethod.POST)
    public String processEditForm(@RequestParam int categoryId, @ModelAttribute @Valid Cheese c, int cheeseId, Errors errors, Model model) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Edit Cheese");
            model.addAttribute("cheese", c);
            model.addAttribute("categories", categoryDao.findAll());
            return "cheese/edit";
        }

        Category cat = categoryDao.findOne(categoryId);

        cheeseDao.delete(cheeseId);

        c.setName(c.getName());
        c.setDescription(c.getDescription());
        c.setCategory(cat);

        cheeseDao.save(c);

        return "redirect:";
    }

}
