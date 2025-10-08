package com.spring.jwt.CEO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ceo")
public class CEOController {

@Autowired
CEOService ceoService;

   //===========================================================//
   //                FOR BATCH TOPPERS STUDENT  LIST            //
   //         /api/ceo/batchTopper/studentClass=?  &  batch=?   //
   //===========================================================//



    @GetMapping("/batchToppers")
    public List<DashboredDTO> getBatchToppers(
            @RequestParam String studentClass,
            @RequestParam String batch
    ) {
        return ceoService.getBatchToppers(studentClass, batch);
    }



    //===========================================================//
    //                FOR BATCH AVERAGE STUDENT  LIST            //
    //         /api/ceo/batchAverage/studentClass=?  &  batch=?  //
    //===========================================================//
    @GetMapping("/batchAverage")
    public List<DashboredDTO> getAverageStudents(
            @RequestParam String studentClass,
            @RequestParam String batch
    ) {
        return ceoService.getAverageStudents(studentClass, batch);
    }


    //=================================================================//
    //                FOR BATCH BELOW AVERAGE STUDENT  LIST            //
    //         /api/ceo/batchBelowAverage/studentClass=?  &  batch=?   //
    //=================================================================//

    @GetMapping("/batchBelowAverage")
    public List<DashboredDTO> getBelowAverageStudents(
            @RequestParam String studentClass,
            @RequestParam String batch
    ) {
        return ceoService.getBelowAverageStudents(studentClass, batch);
    }



    //========================================================================//
    //                FOR COUNT OF TOPPER , AVERAGE & BELOW AVERAGE STUDENTS  //
    //         /api/ceo/batchBelowAverage/studentClass=?  &  batch=?          //
    //========================================================================//


//    @GetMapping("/batchCounts")
//    public Map<String, Integer> getBatchStudentCounts(
//            @RequestParam String studentClass,
//            @RequestParam String batch
//    ) {
//        int toppers = ceoService.getBatchToppers(studentClass, batch).size();
//        int average = ceoService.getAverageStudents(studentClass, batch).size();
//        int belowAvg = ceoService.getBelowAverageStudents(studentClass, batch).size();
//
//        Map<String, Integer> counts = new HashMap<>();
//        counts.put("toppersCount", toppers);
//        counts.put("averageCount", average);
//        counts.put("belowAverageCount", belowAvg);
//
//        return counts;
//    }
}
