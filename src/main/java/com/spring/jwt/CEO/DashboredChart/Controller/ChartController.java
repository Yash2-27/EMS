package com.spring.jwt.CEO.DashboredChart.Controller;

import com.spring.jwt.CEO.DashboredChart.Service.ChartService;
import com.spring.jwt.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/chart")
@RequiredArgsConstructor
public class ChartController {

    private final ChartService chartService;

    @GetMapping("/pieChart")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getPieChart() {
        return ResponseEntity.ok(
                ApiResponse.success("Pie chart data fetched successfully", chartService.getPieChart())
        );
    }

    @GetMapping("/barChart")
    public ResponseEntity<ApiResponse<Map<String, List<Integer>>>> getBarChart() {
        return ResponseEntity.ok(
                ApiResponse.success("Bar chart data fetched successfully", chartService.getBarChart())
        );
    }
}

