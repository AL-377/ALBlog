package com.aidan.alblogserver.controller.admin;


import com.aidan.alblogserver.pojo.Tag;
import com.aidan.alblogserver.service.TagService;
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
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("/tags")
    public String tags(@RequestParam(defaultValue = "1") int pageNum, Model model)
    {
        // 页码和每一页的最大尺寸
        Page<Tag> page = new Page(pageNum,5);
        Page<Tag> tags = tagService.listTag(page);
        tags.getRecords().forEach(System.out::println);
        // 然后等下给前端
        model.addAttribute("page",tags);
        int pagesNum = (int) page.getPages();
        System.out.println("pageNUm: "+pagesNum);
        model.addAttribute("pagesNum",pagesNum);
        return "admin/tags";
    }

    @GetMapping("/tags/input")
    public String input(Model model)
    {
        model.addAttribute("tag",new Tag());
        return "admin/tags-input";
    }

    @GetMapping("/tags/{id}/input")
    public String editInput(@PathVariable Long id, Model model)
    {
        model.addAttribute("tag",tagService.getTag(id));
        return "admin/tags-input";
    }

    @PostMapping("/tags/{id}")
    public String editPost(@Validated Tag tag, BindingResult result, @PathVariable Long id, RedirectAttributes attributes)
    {
        Tag ori = tagService.getTagByName(tag.getName());
        if( ori!= null){
            result.rejectValue("name","nameError","不可重复添加");
        }
        if(result.hasErrors())
            return "admin/tags-input";
        Tag t = tagService.updateTag(id,tag);
        if(t == null){
            attributes.addFlashAttribute("message","更新失败,标签重复");
        }else{
            attributes.addFlashAttribute("message","更新成功");
        }
        return "redirect:/admin/tags";
    }

    @PostMapping("/tags")
    public String post(@Validated Tag tag, BindingResult result,RedirectAttributes attributes)
    {
        Tag ori = tagService.getTagByName(tag.getName());
        if( ori!= null){
            result.rejectValue("name","nameError","不可重复添加");
        }
        if(result.hasErrors())
            return "admin/tags-input";
        Tag t = tagService.saveTag(tag);
        if(t == null){
            attributes.addFlashAttribute("message","添加失败");
        }else{
            attributes.addFlashAttribute("message","添加成功");
        }
        return "redirect:/admin/tags";
    }


    @GetMapping("tags/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes attributes)
    {
        tagService.deleteTag(id);
        attributes.addFlashAttribute("message","删除成功");

        return "redirect:/admin/tags";
    }



}
