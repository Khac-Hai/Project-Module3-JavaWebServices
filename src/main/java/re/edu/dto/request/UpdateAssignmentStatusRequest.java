package re.edu.dto.request;

import lombok.Getter;
import lombok.Setter;
import re.edu.util.enums.Status;

@Getter
@Setter
public class UpdateAssignmentStatusRequest {
    private Status status;
}