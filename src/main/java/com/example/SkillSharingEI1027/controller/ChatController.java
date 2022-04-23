package com.example.SkillSharingEI1027.controller;


import com.example.SkillSharingEI1027.dao.ChatDao;
import com.example.SkillSharingEI1027.dao.MessageDao;
import com.example.SkillSharingEI1027.dao.SkillDao;
import com.example.SkillSharingEI1027.modelo.Message;
import com.example.SkillSharingEI1027.modelo.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.Date;

@Controller
@RequestMapping("/chat")
public class ChatController {

    private ChatDao chatDao;
    private MessageDao messageDao;

    @Autowired
    public void setChatDao(ChatDao chatDao){ this.chatDao=chatDao;}

    @Autowired
    public void setMessageDao(MessageDao messageDao){ this.messageDao=messageDao;}

    //Métodos para listar todos los chats del usuario que está autenticado
    @RequestMapping("/list")
    public String listSkills(Model model, HttpSession session){
        Student user = (Student) session.getAttribute("user");
        model.addAttribute("chats", chatDao.getChatsDeStudent(user));
        return "chat/list";
    }


    //Revisar mensajes entre 2 students
    @RequestMapping(value="/messages/{chatId}")
    public String checkMessages(Model model,@PathVariable String chatId){
        model.addAttribute("messages", messageDao.getMessagesFromChat(chatId));
        messageDao.setChat(chatId);
        model.addAttribute("newMessage", new Message());

        return "chat/messages";
    }

    @RequestMapping(value ="/messages", method = RequestMethod.POST)
    public String processMessage(@ModelAttribute("newMessage") Message newMessage, HttpSession session, Model model){

        Student user = (Student) session.getAttribute("user");
        newMessage.setDate(LocalDate.now());
        newMessage.setStudent(user.getIdStudent());
        newMessage.setIdChat(messageDao.getChat());
        newMessage.setNumber(messageDao.messageNumber(newMessage.getIdChat()));



        messageDao.addMessage(newMessage);
        return "redirect:/chat/messages/"+newMessage.getIdChat();
    }
}
