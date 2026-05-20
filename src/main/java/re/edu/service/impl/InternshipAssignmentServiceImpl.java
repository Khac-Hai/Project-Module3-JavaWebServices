package re.edu.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import re.edu.exception.ResourceNotFoundException;
import re.edu.dto.request.InternshipAssignmentRequest;
import re.edu.dto.request.UpdateAssignmentStatusRequest;
import re.edu.dto.response.InternshipAssignmentResponse;
import re.edu.entity.InternshipAssignments;
import re.edu.entity.InternshipPhases;
import re.edu.entity.Mentors;
import re.edu.entity.Students;
import re.edu.util.enums.Status;
import re.edu.repository.InternshipAssignmentRepository;
import re.edu.repository.InternshipPhaseRepository;
import re.edu.repository.MentorRepository;
import re.edu.repository.StudentRepository;
import re.edu.service.InternshipAssignmentService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InternshipAssignmentServiceImpl implements InternshipAssignmentService {

    private final InternshipAssignmentRepository assignmentRepository;
    private final StudentRepository studentRepository;
    private final MentorRepository mentorRepository;
    private final InternshipPhaseRepository phaseRepository;

    @Override
    public List<InternshipAssignmentResponse> getAllAssignments() {
        // Lấy tất cả phân công
        return assignmentRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public InternshipAssignmentResponse getAssignmentById(Integer assignmentId) {
        // Tìm phân công theo ID, nếu không có thì báo lỗi
        InternshipAssignments assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phân công"));
        return toResponse(assignment);
    }

    @Override
    public InternshipAssignmentResponse createAssignment(InternshipAssignmentRequest request) {
        validateRequest(request);

        // Kiểm tra sinh viên đã được phân công trong phase này chưa
        boolean exists = assignmentRepository.findByStudent_IdAndPhase_PhaseId(
                request.getStudentId(), request.getPhaseId()
        ).isPresent();
        if (exists) {
            throw new IllegalArgumentException("Sinh viên đã được phân công trong giai đoạn này");
        }

        // Lấy dữ liệu liên quan
        Students student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sinh viên"));
        Mentors mentor = mentorRepository.findById(request.getMentorId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy mentor"));
        InternshipPhases phase = phaseRepository.findById(request.getPhaseId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giai đoạn thực tập"));

        // Tạo mới phân công
        InternshipAssignments assignment = new InternshipAssignments();
        assignment.setStudent(student);
        assignment.setMentor(mentor);
        assignment.setPhase(phase);
        assignment.setAssignedDate(LocalDateTime.now());
        assignment.setStatus(Status.PENDING);
        assignment.setCreatedAt(LocalDateTime.now());
        assignment.setUpdatedAt(LocalDateTime.now());

        return toResponse(assignmentRepository.save(assignment));
    }

    @Override
    public InternshipAssignmentResponse updateAssignmentStatus(Integer assignmentId, UpdateAssignmentStatusRequest request) {
        InternshipAssignments assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phân công"));

        if (request.getStatus() == null) {
            throw new IllegalArgumentException("Trạng thái là bắt buộc");
        }

        assignment.setStatus(request.getStatus());
        assignment.setUpdatedAt(LocalDateTime.now());

        return toResponse(assignmentRepository.save(assignment));
    }

    // Convert entity -> response DTO
    private InternshipAssignmentResponse toResponse(InternshipAssignments assignment) {
        InternshipAssignmentResponse response = new InternshipAssignmentResponse();
        response.setAssignmentId(assignment.getAssignmentId());
        response.setStudentId(assignment.getStudent().getId());
        response.setStudentName(assignment.getStudent().getUser().getFullName());
        response.setMentorId(assignment.getMentor().getId());
        response.setMentorName(assignment.getMentor().getUser().getFullName());
        response.setPhaseId(assignment.getPhase().getPhaseId());
        response.setPhaseName(assignment.getPhase().getPhaseName());
        response.setAssignedDate(assignment.getAssignedDate());
        response.setStatus(assignment.getStatus());
        return response;
    }

    // Validate dữ liệu đầu vào khi tạo phân công
    private void validateRequest(InternshipAssignmentRequest request) {
        if (request.getStudentId() == null) {
            throw new IllegalArgumentException("Mã sinh viên là bắt buộc");
        }
        if (request.getMentorId() == null) {
            throw new IllegalArgumentException("Mã mentor là bắt buộc");
        }
        if (request.getPhaseId() == null) {
            throw new IllegalArgumentException("Mã giai đoạn là bắt buộc");
        }
    }
}
