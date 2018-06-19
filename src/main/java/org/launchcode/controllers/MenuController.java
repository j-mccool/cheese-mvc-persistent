package org.launchcode.controllers;


import org.launchcode.models.Menu;
import org.launchcode.models.data.MenuDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
@RequestMapping("menu")
public class MenuController {

    @Autowired
    private MenuDao menuDao;

    @RequestMapping(value="")
    public String index(Model model) {
        model.addAttribute("title", "Menus");
        model.addAttribute("menus", menuDao.findAll());

        return "/menu/index";
    }

    @RequestMapping(value="add", method = RequestMethod.GET)
    public String displayAddMenu(Model model) {

        model.addAttribute("title", "Add Menu");
        model.addAttribute(new Menu());

        return "menu/add";
    }

    @RequestMapping(value="add", method = RequestMethod.POST)
    public String processAddMenu(Model model, @ModelAttribute @Valid Menu newMenu, Errors errors) {
        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Menu");
            return "menu/add";
        }

        menuDao.save(newMenu);
        return "redirect:";

        }
    @RequestMapping(value="view/{id}", method = RequestMethod.GET)
    public String viewMenu(Model model, @PathVariable("id") int id) {
        model.addAttribute("title", "View Menu");
        model.addAttribute("menu", menuDao.findOne(id));

        return "view/{id}";
    }
}
