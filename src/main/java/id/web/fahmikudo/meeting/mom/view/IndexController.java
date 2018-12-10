package id.web.fahmikudo.meeting.mom.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    @RequestMapping("/")
    public String index(){
        return "/pages/index";
    }

    @RequestMapping(value = "project/list")
    public String getAllProjects(Model model){
        return "/pages/project/projectView";
    }

}
