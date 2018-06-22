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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private CheeseDao cheeseDao;

    @Autowired
    private MenuDao menuDao;

    // Request path: /category
    @RequestMapping(value = "")
    public String index(Model model) {
        model.addAttribute("categories", categoryDao.findAll());
        model.addAttribute("title", "Categories");

        return "/category/index";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String displayAddCategoryForm(Model model) {
        model.addAttribute("title", "Add Category");
        model.addAttribute(new Category());


        return "category/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAddCategoryForm(Model model, @ModelAttribute @Valid Category newCategory, Errors errors) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Category");
            return "category/add";
        }

        categoryDao.save(newCategory);
        return "redirect:";
    }

    @RequestMapping(value = "remove", method = RequestMethod.GET)
    public String displayRemoveCategoryForm(Model model) {
        model.addAttribute("categories", categoryDao.findAll());
        model.addAttribute("title", "Remove Categories");

        return "category/remove";
    }

    @RequestMapping(value = "remove", method = RequestMethod.POST)
    public String processRemoveCategoryForm(Model model, @RequestParam int[] categoryIds) {

        Iterable<Menu> m = menuDao.findAll();


        for (int categoryId : categoryIds) {
            for (Menu i : m) {
                List<Cheese> c = i.getCheeses();
                for (Cheese x : c) {
                    if(x.getCategory().equals(categoryDao.findOne(categoryId))){
                        i.removeItem(x);
                    }
                }
            }
        categoryDao.delete(categoryId);
        }

        return "redirect:/cheese";

    }
}