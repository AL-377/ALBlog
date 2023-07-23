package com.aidan.alblogserver.controller.admin;

import com.aidan.alblogserver.pojo.Type;
import com.aidan.alblogserver.service.TypeService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class TypeController {

    @Autowired
    private TypeService typeService;

    @GetMapping("/types")
    public String types(@RequestParam(defaultValue = "1") int pageNum, Model model)
    {
        // 页码和每一页的最大尺寸
        Page<Type> page = new Page(pageNum,5);
        Page<Type> types = typeService.listType(page);
        types.getRecords().forEach(System.out::println);
        // 然后等下给前端
        model.addAttribute("page",types);
        int pagesNum = (int) page.getPages();
        System.out.println("pageNUm: "+pagesNum);
        model.addAttribute("pagesNum",pagesNum);
        return "admin/types";
    }

    @GetMapping("/types/input")
    public String input(Model model)
    {
        model.addAttribute("type",new Type());
        return "admin/types-input";
    }

    @GetMapping("/types/{id}/input")
    public String editInput(@PathVariable Long id, Model model)
    {
        model.addAttribute("type",typeService.getType(id));
        return "admin/types-input";
    }

    @PostMapping("/types/{id}")
    public String editPost(@Validated Type type, BindingResult result,@PathVariable Long id, RedirectAttributes attributes)
    {
        Type ori = typeService.getTypeByName(type.getName());
        if( ori!= null){
            result.rejectValue("name","nameError","不可重复添加");
        }
        if(result.hasErrors())
            return "admin/types-input";
        Type t = typeService.updateType(id,type);
        if(t == null){
            attributes.addFlashAttribute("message","更新失败,类别重复");
        }else{
            attributes.addFlashAttribute("message","更新成功");
        }
        return "redirect:/admin/types";
    }


    @PostMapping("/types")
    public String post(@Validated Type type, BindingResult result,RedirectAttributes attributes)
    {
        Type ori = typeService.getTypeByName(type.getName());
        if( ori!= null){
            result.rejectValue("name","nameError","不可重复添加");
        }
        if(result.hasErrors())
                return "admin/types-input";
        Type t = typeService.saveType(type);
        if(t == null){
            attributes.addFlashAttribute("message","添加失败");
        }else{
            attributes.addFlashAttribute("message","添加成功");
        }
        return "redirect:/admin/types";
    }

    @GetMapping("/types/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes attributes)
    {
        typeService.deleteType(id);
        attributes.addFlashAttribute("message","删除成功");

        return "redirect:/admin/types";
    }



}
