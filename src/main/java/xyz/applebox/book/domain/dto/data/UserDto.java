package xyz.applebox.book.domain.dto.data;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import xyz.applebox.book.domain.enumerate.Sex;

@Data
public class UserDto {
    @Schema(description = "이메일", required = true) private String email;
    @Schema(description = "비밀번호", required = true) private String password;
    @Schema(description = "이름", required = true) private String name;
    @Schema(description = "생년월일(yyyyMMdd)") private String birthDate;
    @Schema(description = "성별") private Sex sex;
}
