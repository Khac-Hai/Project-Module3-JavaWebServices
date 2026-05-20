package re.edu.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import re.edu.dto.request.EvaluationCriteriaRequest;
import re.edu.dto.response.ApiResponse;
import re.edu.service.EvaluationCriteriaService;
import re.edu.mapper.MapToAPIResponse;

@RestController
@RequestMapping("/api/evaluation_criteria")
@RequiredArgsConstructor
public class EvaluationCriteriaController {

    private final EvaluationCriteriaService
            evaluationCriteriaService;

    // 23. GET ALL
    @GetMapping
    @PreAuthorize(
            "hasAnyRole('ADMIN','MENTOR','STUDENT')"
    )
    public ResponseEntity<ApiResponse>
    getAllCriteria() {

        return ResponseEntity.ok(
                MapToAPIResponse.mapTo(
                        evaluationCriteriaService
                                .getAllCriteria(),
                        null,
                        200,
                        "Get evaluation criteria successfully"
                )
        );
    }

    // 24. GET BY ID
    @GetMapping("/{criterionId}")
    @PreAuthorize(
            "hasAnyRole('ADMIN','MENTOR','STUDENT')"
    )
    public ResponseEntity<ApiResponse>
    getCriterionById(
            @PathVariable Integer criterionId
    ) {

        return ResponseEntity.ok(
                MapToAPIResponse.mapTo(
                        evaluationCriteriaService
                                .getCriterionById(
                                        criterionId
                                ),
                        null,
                        200,
                        "Get evaluation criteria successfully"
                )
        );
    }

    // 25. CREATE
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse>
    createCriterion(
            @RequestBody
            EvaluationCriteriaRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        MapToAPIResponse.mapTo(
                                evaluationCriteriaService
                                        .createCriterion(
                                                request
                                        ),
                                null,
                                201,
                                "Create evaluation criteria successfully"
                        )
                );
    }

    // 26. UPDATE
    @PutMapping("/{criterionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse>
    updateCriterion(
            @PathVariable Integer criterionId,
            @RequestBody
            EvaluationCriteriaRequest request
    ) {

        return ResponseEntity.ok(
                MapToAPIResponse.mapTo(
                        evaluationCriteriaService
                                .updateCriterion(
                                        criterionId,
                                        request
                                ),
                        null,
                        200,
                        "Update evaluation criteria successfully"
                )
        );
    }

    // 27. DELETE
    @DeleteMapping("/{criterionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse>
    deleteCriterion(
            @PathVariable Integer criterionId
    ) {

        evaluationCriteriaService
                .deleteCriterion(criterionId);

        return ResponseEntity.ok(
                MapToAPIResponse.mapTo(
                        null,
                        null,
                        200,
                        "Delete evaluation criteria successfully"
                )
        );
    }
}
