package com.spring.jwt.CEO;

import com.spring.jwt.utils.ApiResponse;
import com.spring.jwt.utils.GenericResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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


    @Operation(summary = "CEO Dashboard - Topper list")
    @GetMapping("/batchToppers")
    public ResponseEntity<ApiResponse<List<DashboredDTO>>> getBatchToppers(
            @RequestParam String studentClass,
            @RequestParam String batch
    ) {
        return ResponseEntity.ok(
                ApiResponse.success("Batch toppers fetched successfully", ceoService.getBatchToppers(studentClass, batch))
        );
    }



    //===========================================================//
    //                FOR BATCH AVERAGE STUDENT  LIST            //
    //         /api/ceo/batchAverage/studentClass=?  &  batch=?  //
    //===========================================================//

    @Operation(summary = "CEO Dashboard - Average  list")
    @GetMapping("/batchAverage")
    public ResponseEntity<ApiResponse<List<DashboredDTO>>> getBatchAverage(
            @RequestParam String studentClass,
            @RequestParam String batch
    )
    {
        return ResponseEntity.ok(
                ApiResponse.success("Batch Average fetched successfully",ceoService.getAverageStudents(studentClass, batch))
        );
    }


    //=================================================================//
    //                FOR BATCH BELOW AVERAGE STUDENT  LIST            //
    //         /api/ceo/batchBelowAverage/studentClass=?  &  batch=?   //
    //=================================================================//

    @Operation(summary = "CEO Dashboard - Below average student  list")
    @GetMapping("/batchBelowAverage")
    public ResponseEntity<ApiResponse<List<DashboredDTO>>> getBatchBelowAverage(
            @RequestParam String studentClass,
            @RequestParam String batch
    ) {
    return ResponseEntity.ok(
            ApiResponse.success("Batch below average successfully ",ceoService.getBelowAverageStudents(studentClass, batch))
    );
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
