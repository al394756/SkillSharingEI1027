package com.example.SkillSharingEI1027.controller;

import com.example.SkillSharingEI1027.dao.OffeRequestDao;
import com.example.SkillSharingEI1027.dao.SkillDao;
import com.example.SkillSharingEI1027.dao.StatisticsDao;
import com.example.SkillSharingEI1027.dao.StudentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class EstadisticsController {
    private StatisticsDao statisticsDao;
    private SkillDao skillDao;

    @Autowired
    public void setOffeRequestDao(StatisticsDao statisticsDao, SkillDao skillDao) {
        this.statisticsDao=statisticsDao;
        this.skillDao=skillDao;
    }

    @RequestMapping("/statistics")
    public String statistics(Model model){
        int numberOfDDistinctSkillsOffer=statisticsDao.numberOfDistinctSkillsInOffeRequest("offer");
        int numberOfDDistinctSkillsRequest=statisticsDao.numberOfDistinctSkillsInOffeRequest("request");
        int nSkills=statisticsDao.numberOfDistinctSkills();
        int pOfDDistinctSkillsOffer=numberOfDDistinctSkillsOffer/nSkills*100;
        int pOfDDistinctSkillsRequest=numberOfDDistinctSkillsRequest/nSkills*100;

        model.addAttribute("pOfDDistinctSkillsOffer",pOfDDistinctSkillsOffer);
        model.addAttribute("pOfDDistinctSkillsRequest",pOfDDistinctSkillsRequest);
        return "statistics";
    }
}
