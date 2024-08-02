package nhs.uhdb.NHS_project.questionnaire.controller;
import nhs.uhdb.NHS_project.accounts.model.User;
import nhs.uhdb.NHS_project.accounts.service.UserService;
import nhs.uhdb.NHS_project.questionnaire.model.PreappointmentResponse;
import nhs.uhdb.NHS_project.questionnaire.model.QolResponse;
import nhs.uhdb.NHS_project.questionnaire.service.PreappointmentResponseService;
import nhs.uhdb.NHS_project.questionnaire.service.QolResponseService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.time.LocalDate;

@Controller
public class QolQuestionnaireController {
    private UserService userService;
    private QolResponseService qolResponseService;

    //Constructor to initialise QolResponseService and UserService
    public QolQuestionnaireController(QolResponseService qolResponseService, UserService userService){
        this.qolResponseService = qolResponseService;
        this.userService = userService;
    }

    //Method to handle GET requests for the Quality of Life (qol) questionnaire page
    @GetMapping("/questionnaires/qol-questionnaire")
    public ModelAndView qolQuestionnaire() {
        ModelAndView mav = new ModelAndView("questionnaires/qolQuestionnaire");
        QolResponse form = new QolResponse();
        mav.addObject("qolQuestionnaireForm", form);
        return mav;
    }

    //Method to handle POST requests for submitting the QoL questionnaire form
    @PostMapping("/questionnaires/qol-questionnaire")
    public ModelAndView qolSubmit(Principal principal, @ModelAttribute("qolQuestionnaireForm") QolResponse form) {
        //Retrieve the logged-in user's email from the Principal object
        String loggedInUserEmail = principal.getName();

        //Fetch the User object using the email
        User loggedInUser = userService.getUserByEmail(loggedInUserEmail);

        //Set the user ID and current date on the form
        form.setUser_id(loggedInUser.getUser_id());
        form.setCreatedAt(LocalDate.now());

        //Save the QOL Response and retrieve the response ID
        Long id = qolResponseService.saveResponse(form);

        //Return the success view
        return new ModelAndView("redirect:/questionnaires/success");
    }

}